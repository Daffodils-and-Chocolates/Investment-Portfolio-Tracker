import { Component, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject, Subscription } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { MarketUpdate, Stock } from '../../models/stock.interface';
import { WatchlistService } from '../../services/watchlist.service';
import { FinnhubService } from '../../services/finnhub.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit, OnDestroy {
  private stocksSubject = new BehaviorSubject<Stock[]>([]);
  stocks$ = this.stocksSubject.asObservable();
  groupedStocks: { [key: string]: Stock[] } = {};
  groupNames: string[] = [];
  selectedGroup: string = 'All';
  private marketDataSubscription?: Subscription;
  private watchlistSubscription?: Subscription;

  constructor(
    private watchlistService: WatchlistService,
    private finnhubService: FinnhubService,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.fetchGroupNames();
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
          this.stocksSubject.next(stocks);
          this.setupMarketDataStream(stocks);
        },
        error: (error) => console.error('Error loading stocks:', error)
      });
  }

  loadStocksByGroup(groupName: string) {
    this.selectedGroup = groupName;
    
    if (groupName === 'All') {
      this.loadAllStocks();
    } else {
      this.watchlistSubscription = this.watchlistService.getStocksByGroupName(groupName)
        .subscribe({
          next: (stocks) => {
            this.groupedStocks[groupName] = stocks;
            this.setupMarketDataStream(stocks);
          },
          error: (error) => console.error('Error fetching stocks by group:', error)
        });
    }
  }
}
