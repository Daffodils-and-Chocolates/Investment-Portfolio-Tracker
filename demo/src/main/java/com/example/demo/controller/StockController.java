package com.example.demo.controller;

import com.example.demo.models.entity.Stock;
import com.example.demo.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    @Operation(summary = "get all stocks")
    @GetMapping("/all")
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @Operation(summary = "find stock Id by Stock Name")
    @GetMapping
    public Stock getStockBySymbol(@RequestParam String stockName) {
        return stockService.getStockBySymbol(stockName);
    }
}
