import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HomeService {
  private baseUrl = 'https://finnhub.io/api/v1';
  private token = environment.finnhubToken;

  constructor(private http: HttpClient) {}

  // Get market status
  getMarketStatus(exchange: string = 'US'): Observable<any> {
    const url = `${this.baseUrl}/stock/market-status?exchange=${exchange}&token=${this.token}`;
    return this.http.get(url);
  }

  // Get market holidays
  getMarketHolidays(exchange: string = 'US'): Observable<any> {
    const url = `${this.baseUrl}/stock/market-holiday?exchange=${exchange}&token=${this.token}`;
    return this.http.get(url);
  }

  // Get news by category
  getNews(category: string): Observable<any> {
    const url = `${this.baseUrl}/news?category=${category}&token=${this.token}`;
    return this.http.get(url);
  }

  // Get IPO calendar
  getIpoCalendar(from: string, to: string): Observable<any> {
    const url = `${this.baseUrl}/calendar/ipo?from=${from}&to=${to}&token=${this.token}`;
    return this.http.get(url);
  }

  // List supported crypto exchanges
  getCryptoExchanges(): Observable<any> {
    const url = `${this.baseUrl}/crypto/exchange?token=${this.token}`;
    return this.http.get(url);
  }
}
