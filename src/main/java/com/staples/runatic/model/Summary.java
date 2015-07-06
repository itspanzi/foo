package com.staples.runatic.model;

/**
 * This class represents a report summary
 */
public class Summary {

    private final double unitPriceDollars;
    private final double merchantDiscountDollars;
    private final double runaDiscountDollars;

    public Summary(double unitPriceDollars, double merchantDiscountDollars, double runaDiscountDollars) {
        this.unitPriceDollars = unitPriceDollars;
        this.merchantDiscountDollars = merchantDiscountDollars;
        this.runaDiscountDollars = runaDiscountDollars;
    }
}
