package com.staples.runatic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a report summary
 */
public class Summary {

    @JsonIgnore private int unitPriceInCents;
    @JsonIgnore private int merchantDiscountInCents;
    @JsonIgnore private int runaDiscountInCents;

    public Summary() {
        this(0, 0, 0);
    }

    public Summary(int unitPriceInCents, int merchantDiscountInCents, int runaDiscountInCents) {
        this.unitPriceInCents = unitPriceInCents;
        this.merchantDiscountInCents = merchantDiscountInCents;
        this.runaDiscountInCents = runaDiscountInCents;
    }

    public void addSummaryData(int unitPriceInCents, int merchantDiscountInCents, int runaDiscountInCents) {
        this.unitPriceInCents += unitPriceInCents;
        this.merchantDiscountInCents += merchantDiscountInCents;
        this.runaDiscountInCents += runaDiscountInCents;
    }

    @JsonProperty("unit-price-dollars")
    public double getUnitPriceInDollars() {
        return ((double) unitPriceInCents) / 100;
    }


    @JsonProperty("merchant-discount-dollars")
    public double getMerchantDiscountDollars() {
        return ((double) merchantDiscountInCents) / 100;
    }


    @JsonProperty("runa-discount-dollars")
    public double getRunaDiscountDollars() {
        return ((double) runaDiscountInCents) / 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Summary summary = (Summary) o;

        if (unitPriceInCents != summary.unitPriceInCents) return false;
        if (merchantDiscountInCents != summary.merchantDiscountInCents) return false;
        return runaDiscountInCents == summary.runaDiscountInCents;

    }

    @Override
    public int hashCode() {
        int result = unitPriceInCents;
        result = 31 * result + merchantDiscountInCents;
        result = 31 * result + runaDiscountInCents;
        return result;
    }
}
