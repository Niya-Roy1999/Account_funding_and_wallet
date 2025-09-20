package com.apex.trade.exchange.controller;

import com.apex.trade.exchange.Model.Stock;
import com.apex.trade.exchange.service.StockService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@RestController
@CrossOrigin(
        origins = "http://localhost:5173",  // React dev server
        allowedHeaders = "*",
        allowCredentials = "true",
        maxAge = 3600
)
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping(value = "/stocks/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<List<Stock>>> streamStocks() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(tick -> {
                    List<Stock> stocks = stockService.getStocks();
                    System.out.println("Emitting stocks at tick: " + tick + " -> " + stocks.size());
                    return ServerSentEvent.<List<Stock>>builder()
                            .id(String.valueOf(tick))
                            .event("stocks-update")
                            .data(stocks)
                            .build();
                });
    }
}
