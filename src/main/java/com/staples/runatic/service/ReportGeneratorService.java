package com.staples.runatic.service;

import com.staples.runatic.model.Order;
import com.staples.runatic.model.Report;
import com.staples.runatic.model.SessionEntry;
import com.staples.runatic.persistence.ExternalSessionPersistence;
import com.staples.runatic.persistence.StaplesSessionPersistence;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class ReportGeneratorService {
    public StaplesSessionPersistence staplesPersistence;
    public ExternalSessionPersistence externalPersistence;

    public ReportGeneratorService(StaplesSessionPersistence staplesPersistence, ExternalSessionPersistence externalPersistence) {
        this.staplesPersistence = staplesPersistence;
        this.externalPersistence = externalPersistence;
    }

    public Report generateReport(String orderBy) {
        Stream<SessionEntry> externalData = externalPersistence.entriesStream();
        Stream<SessionEntry> staplesData = staplesPersistence.entriesStream();

        Map<String, List<SessionEntry>> staplesOrderData = staplesData.collect(groupingBy(SessionEntry::getOrderId));
        Map<String, List<SessionEntry>> externalOrderData = externalData.collect(groupingBy(SessionEntry::getOrderId));
        Stream<String> orderIds = sortEntries(staplesOrderData, orderBy).map(Map.Entry::getKey);

        Report report = new Report();

        orderIds.forEach(orderId -> {
            SessionEntry staplesEntry = staplesOrderData.get(orderId).get(0);
            SessionEntry externalEntry = externalOrderData.get(orderId).get(0);
            report.addOrder(new Order(staplesEntry, externalEntry));
            report.addRunaSummary(staplesEntry);
            report.addMerchantSummary(externalEntry);
        });

        return report;
    }

    private Stream<Map.Entry<String, List<SessionEntry>>> sortEntries(Map<String, List<SessionEntry>> orderToSessionData, String orderBy) {
        return orderToSessionData.entrySet().stream().sorted(comparatorFor(orderBy));
    }

    private Comparator<Map.Entry<String, List<SessionEntry>>> comparatorFor(String orderBy) {
        if (Report.ORDER_ID_ASC.equals(orderBy)) {
            return orderIdComparator();
        }
        if (Report.UNIT_PRICE_DOLLARS_ASC.equals(orderBy)) {
            return unitPriceComparator();
        }
        return sessionComparator().reversed();
    }

    private Comparator<Map.Entry<String, List<SessionEntry>>> unitPriceComparator() {
        return (o1, o2) -> entryFrom(o1).getUnitPriceInCents() - entryFrom(o2).getUnitPriceInCents();
    }

    private Comparator<Map.Entry<String, List<SessionEntry>>> orderIdComparator() {
        return (o1, o2) -> {
            String orderId = entryFrom(o1).getOrderId();
            String orderId1 = entryFrom(o2).getOrderId();
            return (int) (Long.parseLong(orderId) - Long.parseLong(orderId1));
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
}
