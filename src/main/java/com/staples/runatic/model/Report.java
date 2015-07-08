package com.staples.runatic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.staples.runatic.data.ExternalSessionData;
import com.staples.runatic.data.StaplesSessionData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Report {
    public static final String SESSION_TYPE_DESC = "session-type-desc";

    private final Map<String, Summary> summaries = new HashMap<>();

    @JsonIgnore private final StaplesSessionData staplesSessionData;
    @JsonIgnore private final ExternalSessionData externalSessionData;

    public Report(StaplesSessionData staplesSessionData, ExternalSessionData externalSessionData) {
        this.staplesSessionData = staplesSessionData;
        this.externalSessionData = externalSessionData;
    }

    public void generateReport(String orderBy) {
        Stream<SessionEntry> externalData = readExternalSessionData();
        Stream<SessionEntry> staplesData = readStaplesSessionData();

//        Map<Long, List<SessionEntry>> orderToSessionData = staplesData.collect(Collectors.groupingBy(SessionEntry::getOrderId));
//        Stream<Map.Entry<Long, List<SessionEntry>>> sortedStream = orderToSessionData.entrySet().stream().sorted((o1, o2) -> sessionTypeFor(o1).compareTo(sessionTypeFor(o2)));
//        Stream<Long> orderIds = sortedStream.map(Map.Entry::getKey);

        computeSummary(staplesData, "runa-summary");
        computeSummary(externalData, "merchant-summary");
    }

    private void computeSummary(Stream<SessionEntry> entryStream, String summaryFor) {
        Summary summary = new Summary();
        entryStream.forEach(entry -> {
            summary.addSummaryData(entry.getUnitPriceInCents(), entry.getMerchantDiscountInCents(),
                    entry.getRunaDiscountInCents());
        });

        summaries.put(summaryFor, summary);
    }

    private String sessionTypeFor(Map.Entry<Long, List<SessionEntry>> o1) {
        return o1.getValue().get(0).getSessionType();
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
}
