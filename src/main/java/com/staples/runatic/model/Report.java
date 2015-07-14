package com.staples.runatic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.staples.runatic.data.ExternalSessionData;
import com.staples.runatic.data.StaplesSessionData;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Report {
    public static final String SESSION_TYPE_DESC = "session-type-desc";
    public static final String ORDER_ID_ASC = "order-id-asc";
    public static final String UNIT_PRICE_DOLLARS_ASC = "unit-price-dollars-asc";

    @JsonProperty("summaries") private final Map<String, Summary> summaries;
    @JsonProperty("orders") private final List<Order> orders;

    @JsonIgnore
    private final StaplesSessionData staplesSessionData;
    @JsonIgnore
    private final ExternalSessionData externalSessionData;
    @JsonIgnore
    private final Summary runaSummary;
    @JsonIgnore
    private final Summary merchantSummary;

    public Report(StaplesSessionData staplesSessionData, ExternalSessionData externalSessionData) {
        this.staplesSessionData = staplesSessionData;
        this.externalSessionData = externalSessionData;
        orders = new ArrayList<>();

        runaSummary = new Summary();
        merchantSummary = new Summary();
        summaries = new HashMap<>();
        summaries.put("runa-summary", runaSummary);
        summaries.put("merchant-summary", merchantSummary);
    }

    public void generateReport(String orderBy) {
        Stream<SessionEntry> externalData = readExternalSessionData();
        Stream<SessionEntry> staplesData = readStaplesSessionData();

        Map<String, List<SessionEntry>> staplesOrderData = staplesData.collect(groupingBy(SessionEntry::getOrderId));
        Map<String, List<SessionEntry>> externalOrderData = externalData.collect(groupingBy(SessionEntry::getOrderId));
        Stream<String> orderIds = sortEntries(staplesOrderData, orderBy).map(Map.Entry::getKey);

        orderIds.forEach(orderId -> {
            SessionEntry staplesEntry = staplesOrderData.get(orderId).get(0);
            SessionEntry externalEntry = externalOrderData.get(orderId).get(0);
            orders.add(new Order(staplesEntry, externalEntry));
            computeSummary(staplesEntry, runaSummary);
            computeSummary(externalEntry, merchantSummary);
        });
    }

    private Stream<Map.Entry<String, List<SessionEntry>>> sortEntries(Map<String, List<SessionEntry>> orderToSessionData, String orderBy) {
        Comparator<Map.Entry<String, List<SessionEntry>>> comparator = sessionComparator().reversed();
        if (ORDER_ID_ASC.equals(orderBy)) {
            comparator = orderIdComparator();
        } else if (UNIT_PRICE_DOLLARS_ASC.equals(orderBy)) {
            comparator = unitPriceComparator();
        }
        return orderToSessionData.entrySet().stream().sorted(comparator);
    }

    private Comparator<Map.Entry<String, List<SessionEntry>>> unitPriceComparator() {
        return (o1, o2) -> entryFrom(o1).getUnitPriceInCents() - entryFrom(o2).getUnitPriceInCents();
    }

    private Comparator<Map.Entry<String, List<SessionEntry>>> orderIdComparator() {
        return (o1, o2) -> {
            long o1Id = Long.parseLong(entryFrom(o1).getOrderId());
            long o2Id = Long.parseLong(entryFrom(o2).getOrderId());
            return (int) (o1Id - o2Id);
        };
    }

    private Comparator<Map.Entry<String, List<SessionEntry>>> sessionComparator() {
        return (o1, o2) -> sessionTypeFor(o1).compareTo(sessionTypeFor(o2));
    }

    private String sessionTypeFor(Map.Entry<String, List<SessionEntry>> o1) {
        return entryFrom(o1).getSessionType();
    }

    private SessionEntry entryFrom(Map.Entry<String, List<SessionEntry>> o1) {
        return o1.getValue().get(0);
    }

    private void computeSummary(SessionEntry entry, Summary summary) {
        summary.addSummaryData(entry.getUnitPriceInCents(), entry.getMerchantDiscountInCents(),
                entry.getRunaDiscountInCents());
    }

    private Stream<SessionEntry> readStaplesSessionData() {
        return staplesSessionData.entriesStream();
    }

    private Stream<SessionEntry> readExternalSessionData() {
        return externalSessionData.entriesStream();
    }

    Summary summaryFor(String summaryType) {
        return summaries.get(summaryType);
    }

    List<Order> getOrders() {
        return orders;
    }
}
