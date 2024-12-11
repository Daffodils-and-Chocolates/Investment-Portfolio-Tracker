import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class FinnhubService {
  private baseUrl = 'http://localhost:5000/api/finnhub';
  private eventSource: EventSource | null = null;

  constructor() {}

  getMarketDataStream(): Observable<any> {
    return new Observable((observer) => {
      if (this.eventSource) {
        this.eventSource.close();
      }

      this.eventSource = new EventSource(`${this.baseUrl}/stream`);

      this.eventSource.onopen = () => {
        console.log('SSE connection established');
      };

      this.eventSource.addEventListener('INIT', (event: any) => {
        console.log('Initialization event received:', event.data);
      });

      this.eventSource.addEventListener('message', (event: any) => {
        try {
          const data = JSON.parse(event.data);
          observer.next(data);
        } catch (error) {
          console.error('Error parsing SSE data:', error);
        }
      });

      this.eventSource.onerror = (error) => {
        console.error('SSE connection error:', error);
        if (this.eventSource) {
          this.eventSource.close();
          this.eventSource = null;
        }
        observer.error(error);
      };

      return () => {
        if (this.eventSource) {
          this.eventSource.close();
          this.eventSource = null;
        }
      };
    }).pipe(
      retry({ count: 5, delay: 2000 }), 
      catchError((error) => {
        console.error('Fatal SSE error after retries:', error);
        return throwError(() => error);
      })
    );
  }

  async subscribeToSymbols(symbols: string[]): Promise<void> {
    try {
      const response = await fetch(`${this.baseUrl}/subscribe`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(symbols),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
    } catch (error) {
      console.error('Error subscribing to symbols:', error);
      throw error;
    }
  }

  async unsubscribeFromSymbol(symbols: string[]): Promise<void> {
    try {
      const response = await fetch(`${this.baseUrl}/unsubscribe`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(symbols),
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
    } catch (error) {
      console.error('Error unsubscribing from symbol:', error);
      throw error;
    }
  }
}
