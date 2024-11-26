package com.example.demo.implementation;

import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.models.entity.Stock;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.StockService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public Stock createStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public Stock updateStock(Long stockId, Stock stockDetails) {
        Stock existingStock = stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException("Stock", stockId));

        if (stockDetails.getSymbol() != null) {
            existingStock.setSymbol(stockDetails.getSymbol());
        }
        if (stockDetails.getDescription() != null) {
            existingStock.setDescription(stockDetails.getDescription());
        }
        if (stockDetails.getType() != null) {
            existingStock.setType(stockDetails.getType());
        }
        if (stockDetails.getCurrency() != null) {
            existingStock.setCurrency(stockDetails.getCurrency());
        }

        return stockRepository.save(existingStock);
    }

    @Override
    public Stock getStockById(Long stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException("Stock", stockId));
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol)
                .orElseThrow(() -> new EntityNotFoundException("Stock: ", symbol));
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public void deleteStock(Long stockId) {
        if (!stockRepository.existsById(stockId)) {
            throw new EntityNotFoundException("Stock", stockId);
        }
        stockRepository.deleteById(stockId);
    }
}
