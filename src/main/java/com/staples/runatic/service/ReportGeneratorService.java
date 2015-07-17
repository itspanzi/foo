package com.staples.runatic.service;

import com.staples.runatic.model.Order;
import com.staples.runatic.model.Report;
import com.staples.runatic.model.SessionEntry;
import com.staples.runatic.persistence.ExternalSessionPersistence;
import com.staples.runatic.persistence.StaplesSessionPersistence;
import com.staples.runatic.service.cache.SessionEntryCache;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.staples.runatic.service.cache.SessionEntryCache.EXTERNAL_ORDER_DATA_KEY;
import static com.staples.runatic.service.cache.SessionEntryCache.STAPLES_ORDER_DATA_KEY;

public class ReportGeneratorService {
    private static final Logger logger = Logger.getLogger(ReportGeneratorService.class.getName());

    public StaplesSessionPersistence staplesPersistence;
    public ExternalSessionPersistence externalPersistence;
    private final SessionEntryCache cache;

    public ReportGeneratorService(StaplesSessionPersistence staplesPersistence, ExternalSessionPersistence externalPersistence) {
        this.staplesPersistence = staplesPersistence;
        this.externalPersistence = externalPersistence;
        cache = new SessionEntryCache();
    }

    public Report generateReport(String orderBy) {
        // The grouped session entries are cached. The sorting however happens each time this method is called.
        Map<Long, SessionEntry> staplesOrderData = cache.get(STAPLES_ORDER_DATA_KEY, staplesPersistence::sessionByOrderId);
        Map<Long, SessionEntry> externalOrderData = cache.get(EXTERNAL_ORDER_DATA_KEY, externalPersistence::sessionByOrderId);

        Report report = new Report();

        sortedOrderIds(staplesOrderData, orderBy).forEach(orderId -> {
            SessionEntry staplesEntry = staplesOrderData.get(orderId);
            SessionEntry externalEntry = externalOrderData.get(orderId);
            report.addOrder(new Order(staplesEntry, externalEntry));
            report.addRunaSummary(staplesEntry);
            report.addMerchantSummary(externalEntry);
        });
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Successfully computed the report");
        }
        return report;
    }

    private Stream<Long> sortedOrderIds(Map<Long, SessionEntry> orderToSessionData, String orderBy) {
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
