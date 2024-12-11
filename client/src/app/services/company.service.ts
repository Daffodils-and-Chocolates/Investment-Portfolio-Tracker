import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
    private token = environment.finnhubToken;
    private baseUrl = 'https://finnhub.io/api/v1';

  constructor(private http: HttpClient) {}

  getCompanyProfile(symbol: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/stock/profile2`, {
      params: {
        symbol,
        token: this.token
      }
    });
  }

  getCompanyNews(symbol: string,from: string, to: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/company-news`, {
      params: {
        symbol,
        from,
        to,
        token: this.token,
      }
    });
  }
}
