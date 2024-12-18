import { AfterViewInit, Component, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { FinnhubService } from '../../services/finnhub.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})      
export class HomeComponent implements AfterViewInit,OnDestroy {
  private subscription: Subscription | null = null;
  marketUpdates: any[] = [];

  constructor(private finnhubService: FinnhubService) {}

  ngAfterViewInit() {
    // setTimeout(() => {
    //   const symbols = [
    //     // 'BINANCE:BTCUSDT'
    //   ];
  
    //   this.finnhubService.subscribeToSymbols(symbols)
    //     .then(() => {
    //       this.subscription = this.finnhubService.getMarketDataStream()
    //         .subscribe({
    //           next: (update) => {
    //             this.marketUpdates.unshift(update);
    //             if (this.marketUpdates.length > 10) {
    //               this.marketUpdates.pop();
    //             }
    //           },
    //           error: (error) => {
    //             console.error('Stream error:', error);
    //           },
    //         });
    //     })
    //     .catch((error) => {
    //       console.error('Error from ngAfterViewInit - setting up market data:', error);
    //     });
    // }, 2000);
  }
  
  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
