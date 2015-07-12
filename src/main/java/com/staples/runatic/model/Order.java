package com.staples.runatic.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    @JsonProperty("runa-data")
    private final SessionEntry staplesEntry;
    @JsonProperty("merchant-data")
    private final SessionEntry externalEntry;

    public Order(SessionEntry staplesEntry, SessionEntry externalEntry) {
        this.staplesEntry = staplesEntry;
        this.externalEntry = externalEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!staplesEntry.equals(order.staplesEntry)) return false;
        return externalEntry.equals(order.externalEntry);

    }

    @Override
    public int hashCode() {
        int result = staplesEntry.hashCode();
        result = 31 * result + externalEntry.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "staplesEntry=" + staplesEntry +
                ", externalEntry=" + externalEntry +
                '}';
    }
}
