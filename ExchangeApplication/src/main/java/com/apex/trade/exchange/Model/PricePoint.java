package com.apex.trade.exchange.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PricePoint {
    private long timestamp;
    private  double price;
    private long volume;

    public PricePoint(long timestamp, double price, long volume) {
        this.timestamp = timestamp;
        this.price = price;
        this.volume = volume;
    }
}
