import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Subscription } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MarketUpdate, Stock } from '../../models/stock.interface';
import { WatchlistService } from '../../services/watchlist.service';
import { FinnhubService } from '../../services/finnhub.service';
import { ManageAccountService } from '../../services/manage-account.service';
import { HomeService } from '../../services/home.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit, OnDestroy, AfterViewInit {
  private stocksSubject = new BehaviorSubject<Stock[]>([]);
  stocks$ = this.stocksSubject.asObservable();
  groupedStocks: { [key: string]: Stock[] } = {};
  groupNames: string[] = [];
  selectedGroup: string = 'All';
  userName: string = '';
  visibleEdit : boolean = false;
  tempGroupName : string = this.selectedGroup;
  changingGroupName : boolean = false;
  private marketDataSubscription?: Subscription;
  private watchlistSubscription?: Subscription;

  constructor(
    private watchlistService: WatchlistService,
    private finnhubService: FinnhubService,
    private http: HttpClient,
    private manageAccountService : ManageAccountService,
    private homeService : HomeService,
    private toastr : ToastrService
  ) {}

  ngOnInit(): void {
    this.fetchUserName();
    this.fetchGroupNames();
    // console.log("ngOnInit  :- selectedGroup : "+ this.selectedGroup + "; tempGroupName : "+ this.tempGroupName);
  }

  ngAfterViewInit() : void{
    this.loadAllStocks();
  }

  ngOnDestroy(): void {
    this.marketDataSubscription?.unsubscribe();
    this.watchlistSubscription?.unsubscribe();
  }

  private setupMarketDataStream(stocks: Stock[]) {
    this.marketDataSubscription?.unsubscribe();

    const symbols = stocks.map(stock => stock.symbol);

    this.finnhubService.subscribeToSymbols(symbols)
      .then(() => {
        this.marketDataSubscription = this.finnhubService.getMarketDataStream()
          .subscribe({
            next: (update: MarketUpdate) => {
              this.updateStockPrice(update);
            },
            error: (error) => {
              console.error('Stream error:', error);
            }
          });
      })
      .catch(error => {
        console.error('Error setting up market data:', error);
      });
  }

  private updateStockPrice(update: MarketUpdate) {
    const currentStocks = this.stocksSubject.value;
    const updatedStocks = currentStocks.map(stock => {
      if (stock.symbol === update.data[0].s) {
        return {
          ...stock,
          price: update.data[0].p,
          volume: update.data[0].v,
        };
      }
      return stock;
    });

    this.stocksSubject.next(updatedStocks);
    
    if (this.selectedGroup !== 'All') {
      this.groupedStocks[this.selectedGroup] = this.groupedStocks[this.selectedGroup].map(stock => {
        if (stock.symbol === update.data[0].s) {
          return {
            ...stock,
            price: update.data[0].p,
            volume: update.data[0].v,
          };
        }
        return stock;
      });
    }
  }

  fetchGroupNames() {
    this.http
      .get<string[]>('http://localhost:5000/api/watchlist/user/groups')
      .subscribe({
        next: (groups) => this.groupNames = groups,
        error: (error) => console.error('Error fetching group names:', error)
      });
  }

  loadAllStocks() {
    this.watchlistSubscription = this.watchlistService.getAllStocks()
      .subscribe({
        next: (stocks) => {
          const validStocks = stocks ?? [];
          this.stocksSubject.next(validStocks);
          // console.log('Fetched stocks:', validStocks);  
          this.setupMarketDataStream(validStocks);
          validStocks.forEach(stock => this.updateStockPriceFromAPI(stock));
        },
        error: (error) => console.error('Error loading stocks:', error)
      });
  }
  

  loadStocksByGroup(groupName: string) {
    this.selectedGroup = groupName;
    this.tempGroupName = groupName;
    // console.log("loadStocksByGroup  :- selectedGroup : "+ this.selectedGroup + "; tempGroupName : "+ this.tempGroupName);
    if (groupName === 'All') {
      this.loadAllStocks();
    } else {
      this.watchlistSubscription = this.watchlistService.getStocksByGroupName(groupName)
        .subscribe({
          next: (stocks) => {
            const validStocks = stocks ?? [];
            this.groupedStocks[groupName] = validStocks;
            this.setupMarketDataStream(validStocks);
            validStocks.forEach(stock => this.updateStockPriceFromAPI(stock));
          },
          error: (error) => console.error('Error fetching stocks by group:', error)
        });
    }
  }  

  fetchUserName(){
    this.manageAccountService.getUserData().subscribe({
      next : (response) => {
        this.userName = response.name;
      },
      error: (err) => {
        console.error('Error fetching userName', err);
      }
    })
  }

  toggleEdit(){
    this.visibleEdit = !this.visibleEdit;
  }

  private updateStockPriceFromAPI(stock: Stock): void {
    this.homeService.getStockQuote(stock.symbol).subscribe({
      next: (quote) => {
        const updatedStock = {
          ...stock,
          price: quote.c,
          change: quote.d,
          percentChange: quote.dp,
          highPrice: quote.h,
          lowPrice: quote.l,
          openPrice: quote.o,
          previousClosePrice: quote.pc
        };
  
        this.updateStockInList(updatedStock);
  
        Object.keys(this.groupedStocks).forEach(groupName => {
          if (this.groupedStocks[groupName]) {
            this.groupedStocks[groupName] = this.groupedStocks[groupName].map(groupStock =>
              groupStock.symbol === updatedStock.symbol ? updatedStock : groupStock
            );
          }
        });
      },
      error: (error) => {
        console.error(`Error fetching price for ${stock.symbol}:`, error);
      }
    });
  }
  
  private updateStockInList(updatedStock: Stock): void {
    const currentStocks = this.stocksSubject.value;
    const updatedStocks = currentStocks.map(stock => 
      stock.symbol === updatedStock.symbol ? updatedStock : stock
    );
    this.stocksSubject.next(updatedStocks);
  }

  onTempGroupNameChange(groupName: string) {
    this.changingGroupName = true;
  }

  onSavingGroupName(saving: boolean) {
    if(saving){
      this.saveGroupName();
    }
    this.visibleEdit = false;
    this.changingGroupName = false;
    // console.log("on sAVINGgroup change in temp group change!!!");
  }

  saveGroupName() {
    this.watchlistService.updateGroupName(this.selectedGroup, this.tempGroupName).subscribe({
      next: (response) => {
        this.tempGroupName = this.selectedGroup;
        this.fetchGroupNames();
        this.toastr.success('Group name saved successfully!', 'Success');
      },
      error: (error) => {
        console.error('Error saving group name:', error);
      }
    })
  }
  
}
