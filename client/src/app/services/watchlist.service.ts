import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, forkJoin, switchMap } from 'rxjs';
import { Stock, WatchlistRequestDto, WatchlistResponseDto } from '../models/stock.interface';

@Injectable({
  providedIn: 'root',
})
export class WatchlistService {
  private apiUrl = 'http://localhost:5000/api/watchlist';
  private watchlistStocksSubject = new BehaviorSubject<Stock[]>([]);
  watchlistStocks$ = this.watchlistStocksSubject.asObservable();
  stockIds: number[];

  constructor(private http: HttpClient) {
    this.loadUserStocks();
  }

  private loadUserStocks() {
    this.getAllStocks().subscribe({
      next: (stocks) => this.watchlistStocksSubject.next(stocks),
      error: (error) => console.error('Error loading watchlist stocks:', error),
    });
  }

  getAllStocks(): Observable<Stock[]> {
    return this.http.get<Stock[]>(`${this.apiUrl}/user`);
  }

  getStocksByGroupName(groupName: string): Observable<Stock[]> {
    return this.http.get<Stock[]>(`${this.apiUrl}/user/${groupName}`);
  }

  addStocksToGroup(groupName: string, stocks: Stock[]): Observable<Stock[]> {
    return this.http.post<Stock[]>(
      `${this.apiUrl}/${groupName}/add-stocks`,
      stocks
    );
  }

  removeStocksFromGroup(groupName: string, stocks: Stock[]): Observable<Stock[]> {
    const stockIdObservables = stocks.map((stock) => {
        return this.getStockBySymbol(stock.symbol);
    });
  
    return forkJoin(stockIdObservables).pipe(
      switchMap((stockIds) =>
        this.http.delete<Stock[]>(`${this.apiUrl}/${groupName}/remove-stocks`, {
          body: stockIds,
        })
      )
    );
  }
  
  isStockInWatchlist(symbol: string): boolean {
    const stocks = this.watchlistStocksSubject.value;
    return stocks.some((stock) => stock.symbol === symbol);
  }

  getStockBySymbol(stockName: string): Observable<Stock> {
    return this.http.get<Stock>(`http://localhost:5000/api/stock`, {
      params: { stockName }
    });
  }
  
  refreshWatchlist() {
    this.loadUserStocks();
  }

  getGroupNames(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/user/groups`);
  }

  createWatchlistEntry(request :WatchlistRequestDto) : Observable<WatchlistResponseDto>{
    return this.http.post<WatchlistResponseDto>(`${this.apiUrl}`,{
      body: request
    });
  }

  getGroupNamesByStock(stockId : number ): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/user/groups`,{
      params: {stockId}
    });
  }

}
