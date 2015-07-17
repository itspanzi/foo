package com.staples.runatic.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staples.runatic.persistence.ExternalSessionPersistence;
import com.staples.runatic.persistence.StaplesSessionPersistence;
import com.staples.runatic.service.ReportGeneratorService;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import static com.staples.runatic.model.Report.ORDER_ID_ASC;
import static com.staples.runatic.model.Report.SESSION_TYPE_DESC;
import static com.staples.runatic.model.Report.UNIT_PRICE_DOLLARS_ASC;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportTest {


    @Test
    public void shouldSerialiseWithTheExpectedNames() throws Exception {
        Report report = initReport();

        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(report), Map.class);

        assertThat(map.containsKey("orders"), is(true));
        assertThat(map.containsKey("summaries"), is(true));
    }

    private Report initReport() {
        Report report = new Report();
        report.addMerchantSummary(new SessionEntry("1234", 200, 10, 10, "control"));
        report.addMerchantSummary(new SessionEntry("5678", 3500, 0, 100, "test"));
        report.addRunaSummary(new SessionEntry("1234", 100, 18, 15, "control"));
        report.addRunaSummary(new SessionEntry("5678", 3500, 0, 100, "test"));
        report.addOrder(new Order(new SessionEntry("1234", 200, 10, 10, "control"), new SessionEntry("1234", 100, 18, 15, "control")));
        return report;
    }
}
