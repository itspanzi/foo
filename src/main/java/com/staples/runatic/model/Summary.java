package com.staples.runatic.model;

/**
 * This class represents a report summary
 */
public class Summary {

    private int unitPriceInCents;
    private int merchantDiscountInCents;
    private int runaDiscountInCents;

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
