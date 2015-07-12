package com.staples.runatic.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.staples.runatic.data.ExternalSessionData;
import com.staples.runatic.data.StaplesSessionData;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.staples.runatic.model.Report.SESSION_TYPE_DESC;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportTest {

    @Test
    public void shouldReturnANewReportWithSummaries() throws Exception {
        StaplesSessionData staplesMock = mock(StaplesSessionData.class);
        ExternalSessionData externalMock = mock(ExternalSessionData.class);
        when(staplesMock.entriesStream()).thenReturn(staplesEntries());
        when(externalMock.entriesStream()).thenReturn(enternalEntries());

        Report report = new Report(staplesMock, externalMock);

        report.generateReport(SESSION_TYPE_DESC);

        assertThat(report.summaryFor("runa-summary"), is(new Summary(16100, 1518, 3115)));
        assertThat(report.summaryFor("merchant-summary"), is(new Summary(16200, 1010, 3110)));
    }

    @Test
    public void shouldReturnOrdersSortedBySessionType() throws Exception {
        StaplesSessionData staplesMock = mock(StaplesSessionData.class);
        ExternalSessionData externalMock = mock(ExternalSessionData.class);
        when(staplesMock.entriesStream()).thenReturn(staplesEntries());
        when(externalMock.entriesStream()).thenReturn(enternalEntries());

        Report report = new Report(staplesMock, externalMock);

        report.generateReport(SESSION_TYPE_DESC);

        assertThat(report.orders(), is(Arrays.asList(
                new Order(new SessionEntry(4455, 12500, 1500, 3000, "unmanaged"), new SessionEntry(4455, 12500, 1000, 3000, "unmanaged")),
                new Order(new SessionEntry(5678, 3500, 0, 100, "test"), new SessionEntry(5678, 3500, 0, 100, "test")),
                new Order(new SessionEntry(1234, 100, 18, 15, "control"), new SessionEntry(1234, 200, 10, 10, "control")))));
    }

    @Test
    public void shouldSerialiseWithTheExpectedNames() throws Exception {
        StaplesSessionData staplesMock = mock(StaplesSessionData.class);
        ExternalSessionData externalMock = mock(ExternalSessionData.class);
        when(staplesMock.entriesStream()).thenReturn(staplesEntries());
        when(externalMock.entriesStream()).thenReturn(enternalEntries());

        Report report = new Report(staplesMock, externalMock);
        report.generateReport(SESSION_TYPE_DESC);

        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(report), Map.class);
        assertThat(map.containsKey("orders"), is(true));
        assertThat(map.containsKey("summaries"), is(true));
    }

    private Stream<SessionEntry> enternalEntries() {
        return Arrays.asList(new SessionEntry(1234, 200, 10, 10, "control"), new SessionEntry(5678, 3500, 0, 100, "test"),
                new SessionEntry(4455, 12500, 1000, 3000, "unmanaged")).stream();
    }

    private Stream<SessionEntry> staplesEntries() {
        return Arrays.asList(new SessionEntry(1234, 100, 18, 15, "control"), new SessionEntry(5678, 3500, 0, 100, "test"),
                new SessionEntry(4455, 12500, 1500, 3000, "unmanaged")).stream();
    }
}