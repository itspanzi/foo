package com.staples.runatic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a row in the session data store (either the internal or the third party data)
 */
public class SessionEntry {
    @JsonProperty("order-id") private final long orderId;
    @JsonProperty("session-type") private final String sessionType;
    @JsonIgnore private final int unitPriceInCents;
    @JsonIgnore private final int merchantDiscountInCents;
    @JsonIgnore private final int runaDiscountInCents;

    public SessionEntry(long orderId, int unitPriceInCents, int merchantDiscountInCents, int runaDiscountInCents, String sessionType) {
        this.orderId = orderId;
        this.unitPriceInCents = unitPriceInCents;
        this.merchantDiscountInCents = merchantDiscountInCents;
        this.runaDiscountInCents = runaDiscountInCents;
        this.sessionType = sessionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntry that = (SessionEntry) o;

        if (orderId != that.orderId) return false;
        if (unitPriceInCents != that.unitPriceInCents) return false;
        if (merchantDiscountInCents != that.merchantDiscountInCents) return false;
        if (runaDiscountInCents != that.runaDiscountInCents) return false;
        return sessionType.equals(that.sessionType);
    }

    @Override
    public int hashCode() {
        int result = (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + unitPriceInCents;
        result = 31 * result + merchantDiscountInCents;
        result = 31 * result + runaDiscountInCents;
        result = 31 * result + sessionType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SessionEntry{" +
                "orderId=" + orderId +
                ", unitPriceInCents=" + unitPriceInCents +
                ", merchantDiscountInCents=" + merchantDiscountInCents +
                ", runaDiscountInCents=" + runaDiscountInCents +
                ", sessionType='" + sessionType + '\'' +
                '}';
    }

    public long getOrderId() {
        return orderId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public int getUnitPriceInCents() {
        return unitPriceInCents;
    }

    public int getMerchantDiscountInCents() {
        return merchantDiscountInCents;
    }

    public int getRunaDiscountInCents() {
        return runaDiscountInCents;
    }

    @JsonProperty("unit-price-dollars")
    public double getUnitPriceInDollars() {
        return (double) unitPriceInCents / 100;
    }

    @JsonProperty("merchant-discount-dollars")
    public double getMerchantDiscountInDollars() {
        return (double) merchantDiscountInCents / 100;
    }

    @JsonProperty("runa-discount-dollars")
    public double getRunaDiscountInDollars() {
        return (double) runaDiscountInCents / 100;
    }

}
