package com.apex.trade.exchange.service;

import com.apex.trade.exchange.Model.PricePoint;
import com.apex.trade.exchange.Model.Stock;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StockService {

    private final List<Stock> stocks = new ArrayList<>();

    private final Random random = new Random();

    public StockService() {
        initializeStocks();
        startPriceUpdates();
    }

    private void initializeStocks() {
        stocks.add(new Stock("AAPL", "Apple Inc.", 175.50));
        stocks.add(new Stock("GOOGL", "Alphabet Inc.", 2850.75));
        stocks.add(new Stock("MSFT", "Microsoft Corp.", 410.20));
        stocks.add(new Stock("TSLA", "Tesla Inc.", 245.80));
        stocks.add(new Stock("NVDA", "NVIDIA Corp.", 875.25));
        stocks.add(new Stock("AMZN", "Amazon.com Inc.", 3500.10));
        stocks.add(new Stock("META", "Meta Platforms", 315.45));
        stocks.add(new Stock("ORCL", "Oracle Corp.", 100.75));
        stocks.add(new Stock("IBM", "IBM Corp.", 145.80));
        stocks.add(new Stock("CRM", "Salesforce", 210.35));
        // Add more if needed

        // Initialize priceHistory
        long now = System.currentTimeMillis();
        for (Stock stock : stocks) {
            stock.setPreviousPrice(stock.getPrice());
            stock.setHigh24h(stock.getPrice());
            stock.setLow24h(stock.getPrice());
            stock.getPriceHistory().add(new PricePoint(now, stock.getPrice(), random.nextInt(1000000)));
        }
    }

    private void startPriceUpdates() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::updatePrices, 0, 2, TimeUnit.SECONDS);
    }

    private void updatePrices() {
        long now = System.currentTimeMillis();

        for (Stock stock : stocks) {
            stock.setPreviousPrice(stock.getPrice());

            // Random independent fluctuation for each stock
            double volatility = 0.01; // 1% per update
            double change = (random.nextDouble() - 0.5) * 2 * volatility * stock.getPrice();
            double newPrice = Math.max(stock.getPrice() + change, 0.1); // price cannot be negative

            stock.setPrice(Math.round(newPrice * 100.0) / 100.0);
            stock.setChange(stock.getPrice() - stock.getPreviousPrice());
            stock.setChangePercent((stock.getChange() / stock.getPreviousPrice()) * 100);

            // Update 24h high/low
            stock.setHigh24h(Math.max(stock.getHigh24h(), stock.getPrice()));
            stock.setLow24h(Math.min(stock.getLow24h(), stock.getPrice()));

            // Update price history
            stock.getPriceHistory().add(new PricePoint(now, stock.getPrice(), random.nextInt(1000000)));
            if (stock.getPriceHistory().size() > 100) {
                stock.getPriceHistory().remove(0);
            }
        }
    }

    public List<Stock> getStocks() {
        return new ArrayList<>(stocks); // return copy to avoid modification
    }

    public Stock getStock(String symbol) {
        return stocks.stream().filter(s -> s.getSymbol().equals(symbol)).findFirst().orElse(null);
    }
}
