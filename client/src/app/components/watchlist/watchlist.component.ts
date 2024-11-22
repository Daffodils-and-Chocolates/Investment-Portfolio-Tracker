import { Component, OnInit, OnDestroy } from '@angular/core';
import { WebsocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css'],
})
export class WatchlistComponent implements OnInit, OnDestroy {
  stockData: any[] = [];
  private wsUrl = 'ws://localhost:5000/ws/finnhub'; // Adjust backend WebSocket URL as needed
  private websocketSubscription: any;

  constructor(private websocketService: WebsocketService) {}

  ngOnInit(): void {
    this.websocketSubscription = this.websocketService
      .connect(this.wsUrl)
      .subscribe((data: any) => {
        if (data.type === 'trade') {
          this.updateStockData(data);
        }
      }); 
      // Mock data for testing
  this.stockData = [
    { symbol: 'AAPL', price: 174.35, change: 1.25 },
    { symbol: 'TSLA', price: 245.67, change: -2.87 },
    { symbol: 'GOOGL', price: 2894.33, change: 0.12 },
    { symbol: 'MSFT', price: 338.77, change: -1.45 },
    { symbol: 'AMZN', price: 125.63, change: 2.78 },
  ];
  }

  updateStockData(data: any): void {
    const stockUpdates = data.data; // Assuming 'data' contains an array of stock updates

    stockUpdates.forEach((update: any) => {
      const stock = this.stockData.find((s) => s.symbol === update.s);

      if (stock) {
        // Update existing stock entry
        stock.price = update.p;
        stock.change = ((update.p - stock.price) / stock.price) * 100;
      } else {
        // Add new stock entry
        this.stockData.push({
          symbol: update.s,
          price: update.p,
          change: 0,
        });
      }
    });
  }

  ngOnDestroy(): void {
    if (this.websocketSubscription) {
      this.websocketSubscription.unsubscribe();
    }
    this.websocketService.disconnect();
  }
}
