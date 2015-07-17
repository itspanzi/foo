package com.staples.runatic.service;

import com.staples.runatic.model.Order;
import com.staples.runatic.model.Report;
import com.staples.runatic.model.SessionEntry;
import com.staples.runatic.persistence.ExternalSessionPersistence;
import com.staples.runatic.persistence.StaplesSessionPersistence;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
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
        Map<Long, SessionEntry> staplesOrderData = staplesPersistence.sessionByOrderId();
        Map<Long, SessionEntry> externalOrderData = externalPersistence.sessionByOrderId();

        Report report = new Report();

        sortedEntryIds(staplesOrderData, orderBy).forEach(orderId -> {
            SessionEntry staplesEntry = staplesOrderData.get(orderId);
            SessionEntry externalEntry = externalOrderData.get(orderId);
            report.addOrder(new Order(staplesEntry, externalEntry));
            report.addRunaSummary(staplesEntry);
            report.addMerchantSummary(externalEntry);
        });

        return report;
    }

    private Stream<Long> sortedEntryIds(Map<Long, SessionEntry> orderToSessionData, String orderBy) {
        return orderToSessionData.entrySet().stream().sorted(comparatorFor(orderBy)).map(Entry::getKey);
    }

    private Comparator<Entry<Long, SessionEntry>> comparatorFor(String orderBy) {
        if (Report.ORDER_ID_ASC.equals(orderBy)) {
            return orderIdComparator();
        }
        if (Report.UNIT_PRICE_DOLLARS_ASC.equals(orderBy)) {
            return unitPriceComparator();
        }
        return sessionComparator().reversed();
    }

    private Comparator<Entry<Long, SessionEntry>> unitPriceComparator() {
        return (o1, o2) -> o1.getValue().getUnitPriceInCents() - o2.getValue().getUnitPriceInCents();
    }

    private Comparator<Entry<Long, SessionEntry>> orderIdComparator() {
        return (o1, o2) -> (int) (o1.getValue().getOrderId() - o2.getValue().getOrderId());
    }

    private Comparator<Entry<Long, SessionEntry>> sessionComparator() {
        return (o1, o2) -> sessionTypeFor(o1).compareTo(sessionTypeFor(o2));
    }

    private String sessionTypeFor(Entry<Long, SessionEntry> o1) {
        return o1.getValue().getSessionType();
    }
}
