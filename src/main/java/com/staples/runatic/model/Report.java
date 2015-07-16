package com.staples.runatic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Report {
    public static final String SESSION_TYPE_DESC = "session-type-desc";
    public static final String ORDER_ID_ASC = "order-id-asc";
    public static final String UNIT_PRICE_DOLLARS_ASC = "unit-price-dollars-asc";

    @JsonProperty("summaries") private final Map<String, Summary> summaries;
    @JsonProperty("orders") private final List<Order> orders;

    @JsonIgnore
    private final Summary runaSummary;
    @JsonIgnore
    private final Summary merchantSummary;

    public Report() {
        orders = new ArrayList<>();
        runaSummary = new Summary();
        merchantSummary = new Summary();
        summaries = new HashMap<>();
        summaries.put("runa-summary", runaSummary);
        summaries.put("merchant-summary", merchantSummary);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void addMerchantSummary(SessionEntry entry) {
        merchantSummary.addSummaryData(entry.getUnitPriceInCents(), entry.getMerchantDiscountInCents(),
                entry.getRunaDiscountInCents());
    }

    public void addRunaSummary(SessionEntry entry) {
        runaSummary.addSummaryData(entry.getUnitPriceInCents(), entry.getMerchantDiscountInCents(),
                entry.getRunaDiscountInCents());
    }

    public Summary summaryFor(String summaryType) {
        return summaries.get(summaryType);
    }

    public List<Order> getOrders() {
        return orders;
    }
}
