import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private token = environment.finnhubToken;
  private baseUrl = 'https://finnhub.io/api/v1';

  constructor(private http: HttpClient) {}

  searchSymbol(query: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/search`, {
      params: {
        q: query,
        token: this.token,
        exchange: "US"
      }
    });
  }

  getCompanyProfile(symbol: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/stock/profile2`, {
      params: {
        symbol,
        token: this.token
      }
    });
  }

  getCryptoSymbols(exchange: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/crypto/symbol`, {
      params: {
        exchange,
        token: this.token
      }
    });
  }
}
