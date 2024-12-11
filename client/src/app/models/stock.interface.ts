export interface Stock {
  stockId: number;
  symbol: string;
  description: string;
  type: string;
  currency: string;
  price?: number;
  change?: number;
  percentChange?: number;
  highPrice?: number;
  lowPrice?: number;
  openPrice?: number;
  previousClosePrice?: number;
}

export interface WatchlistGroup {
  groupName: string;
}

export interface MarketUpdate {
  data: [
    {
      s: string;
      p: number;
      v: number;
    }
  ];
}

export interface WatchlistResponseDto {
  id: number;
  stock: Stock;
  group: WatchlistGroup;
  createdAt: string;
}

export interface WatchlistRequestDto {
  stock: Stock;
  group: WatchlistGroup;
}
