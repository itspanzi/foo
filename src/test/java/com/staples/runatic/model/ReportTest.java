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
        StaplesSessionPersistence staplesMock = mock(StaplesSessionPersistence.class);
        ExternalSessionPersistence externalMock = mock(ExternalSessionPersistence.class);
        when(staplesMock.entriesStream()).thenReturn(staplesEntries());
        when(externalMock.entriesStream()).thenReturn(enternalEntries());

        ReportGeneratorService reportService = new ReportGeneratorService(staplesMock, externalMock);
        Report report = reportService.generateReport("");

        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(report), Map.class);
        assertThat(map.containsKey("orders"), is(true));
        assertThat(map.containsKey("summaries"), is(true));
    }

    private static Stream<SessionEntry> enternalEntries() {
        return Arrays.asList(new SessionEntry("1234", 200, 10, 10, "control"), new SessionEntry("5678", 3500, 0, 100, "test"),
                new SessionEntry("4455", 12500, 1000, 3000, "unmanaged")).stream();
    }

    private static Stream<SessionEntry> staplesEntries() {
        return Arrays.asList(new SessionEntry("1234", 100, 18, 15, "control"), new SessionEntry("5678", 3500, 0, 100, "test"),
                new SessionEntry("4455", 12500, 1500, 3000, "unmanaged")).stream();
    }
}
