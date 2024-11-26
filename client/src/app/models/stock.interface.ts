export interface Stock {
  stockId: number;
  symbol: string;
  description: string;
  type: string;
  currency: string;
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
