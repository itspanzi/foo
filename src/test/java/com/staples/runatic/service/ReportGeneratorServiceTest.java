package com.staples.runatic.service;

import com.staples.runatic.model.Order;
import com.staples.runatic.model.Report;
import com.staples.runatic.model.SessionEntry;
import com.staples.runatic.model.Summary;
import com.staples.runatic.persistence.ExternalSessionPersistence;
import com.staples.runatic.persistence.StaplesSessionPersistence;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.staples.runatic.model.Report.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReportGeneratorServiceTest {

    @Test
    public void shouldReturnANewReportWithSummaries() throws Exception {
        StaplesSessionPersistence staplesMock = mock(StaplesSessionPersistence.class);
        ExternalSessionPersistence externalMock = mock(ExternalSessionPersistence.class);
        when(staplesMock.sessionByOrderId()).thenReturn(staplesEntries());
        when(externalMock.sessionByOrderId()).thenReturn(enternalEntries());

        ReportGeneratorService reportService = new ReportGeneratorService(staplesMock, externalMock);

        Report report = reportService.generateReport(SESSION_TYPE_DESC);

        assertThat(report.summaryFor("runa-summary"), is(new Summary(16100, 1518, 3115)));
        assertThat(report.summaryFor("merchant-summary"), is(new Summary(16200, 1010, 3110)));
    }

    @Test
    public void shouldReturnOrdersSortedBySessionType() throws Exception {
        StaplesSessionPersistence staplesMock = mock(StaplesSessionPersistence.class);
        ExternalSessionPersistence externalMock = mock(ExternalSessionPersistence.class);
        when(staplesMock.sessionByOrderId()).thenReturn(staplesEntries());
        when(externalMock.sessionByOrderId()).thenReturn(enternalEntries());

        ReportGeneratorService reportService = new ReportGeneratorService(staplesMock, externalMock);

        Report report = reportService.generateReport(SESSION_TYPE_DESC);

        assertThat(report.getOrders(), is(Arrays.asList(
                new Order(new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"), new SessionEntry("4455", 12500, 1000, 3000, "unmanaged")),
                new Order(new SessionEntry("5678", 3500, 0, 100, "test"), new SessionEntry("5678", 3500, 0, 100, "test")),
                new Order(new SessionEntry("1234", 100, 18, 15, "control"), new SessionEntry("1234", 200, 10, 10, "control")))));
    }

    @Test
    public void shouldReturnOrdersSortedBySessionTypeIfNoOrderIsSpecified() throws Exception {
        StaplesSessionPersistence staplesMock = mock(StaplesSessionPersistence.class);
        ExternalSessionPersistence externalMock = mock(ExternalSessionPersistence.class);
        when(staplesMock.sessionByOrderId()).thenReturn(staplesEntries());
        when(externalMock.sessionByOrderId()).thenReturn(enternalEntries());

        ReportGeneratorService reportService = new ReportGeneratorService(staplesMock, externalMock);
        Report report = reportService.generateReport("");


        assertThat(report.getOrders(), is(Arrays.asList(
                new Order(new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"), new SessionEntry("4455", 12500, 1000, 3000, "unmanaged")),
                new Order(new SessionEntry("5678", 3500, 0, 100, "test"), new SessionEntry("5678", 3500, 0, 100, "test")),
                new Order(new SessionEntry("1234", 100, 18, 15, "control"), new SessionEntry("1234", 200, 10, 10, "control")))));
    }

    @Test
    public void shouldReturnOrdersSortedByOrderIdAscending() throws Exception {
        StaplesSessionPersistence staplesMock = mock(StaplesSessionPersistence.class);
        ExternalSessionPersistence externalMock = mock(ExternalSessionPersistence.class);
        when(staplesMock.sessionByOrderId()).thenReturn(staplesEntries());
        when(externalMock.sessionByOrderId()).thenReturn(enternalEntries());

        ReportGeneratorService reportService = new ReportGeneratorService(staplesMock, externalMock);
        Report report = reportService.generateReport(ORDER_ID_ASC);

        assertThat(report.getOrders(), is(Arrays.asList(
                new Order(new SessionEntry("1234", 100, 18, 15, "control"), new SessionEntry("1234", 200, 10, 10, "control")),
                new Order(new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"), new SessionEntry("4455", 12500, 1000, 3000, "unmanaged")),
                new Order(new SessionEntry("5678", 3500, 0, 100, "test"), new SessionEntry("5678", 3500, 0, 100, "test")))));
    }

    @Test
    public void shouldReturnOrdersSortedByUnitPriceAscending() throws Exception {
        StaplesSessionPersistence staplesMock = mock(StaplesSessionPersistence.class);
        ExternalSessionPersistence externalMock = mock(ExternalSessionPersistence.class);
        when(staplesMock.sessionByOrderId()).thenReturn(staplesEntries());
        when(externalMock.sessionByOrderId()).thenReturn(enternalEntries());

        ReportGeneratorService reportService = new ReportGeneratorService(staplesMock, externalMock);
        Report report = reportService.generateReport(UNIT_PRICE_DOLLARS_ASC);


        assertThat(report.getOrders(), is(Arrays.asList(
                new Order(new SessionEntry("1234", 100, 18, 15, "control"), new SessionEntry("1234", 200, 10, 10, "control")),
                new Order(new SessionEntry("5678", 3500, 0, 100, "test"), new SessionEntry("5678", 3500, 0, 100, "test")),
                new Order(new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"), new SessionEntry("4455", 12500, 1000, 3000, "unmanaged")))));
    }

    private static Map<Long, SessionEntry> enternalEntries() {
        Map<Long, SessionEntry> orderIdToEntries = new HashMap<>();
        orderIdToEntries.put(1234L, new SessionEntry("1234", 200, 10, 10, "control"));
        orderIdToEntries.put(5678L, new SessionEntry("5678", 3500, 0, 100, "test"));
        orderIdToEntries.put(4455L, new SessionEntry("4455", 12500, 1000, 3000, "unmanaged"));
        return orderIdToEntries;
    }

    private static Map<Long, SessionEntry> staplesEntries() {
        Map<Long, SessionEntry> orderIdToEntries = new HashMap<>();
        orderIdToEntries.put(1234L, new SessionEntry("1234", 100, 18, 15, "control"));
        orderIdToEntries.put(5678L, new SessionEntry("5678", 3500, 0, 100, "test"));
        orderIdToEntries.put(4455L, new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"));
        return orderIdToEntries;
    }

}
