package com.apex.trade.exchange.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Stock {

    private String symbol;
    private String name;
    private double price;
    private double previousPrice;
    private double change;
    private double changePercent;
    private long volume;
    private double high24h;
    private double low24h;
    private List<PricePoint> priceHistory = new ArrayList<>();

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}
