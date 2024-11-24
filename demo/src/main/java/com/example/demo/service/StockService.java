package com.example.demo.service;

import com.example.demo.models.entity.Stock;

import java.util.List;

public interface StockService {
    Stock createStock(Stock stock);
    Stock updateStock(Long stockId, Stock stockDetails);
    Stock getStockById(Long stockId);
    Stock getStockBySymbol(String symbol);
    List<Stock> getAllStocks();
    void deleteStock(Long stockId);
}