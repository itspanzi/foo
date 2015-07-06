package com.staples.runatic.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RunaticReportServiceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(RunaticReportService.class);
    }

    @Test
    public void shouldReturnHttpSuccessWhenAccessingReport() {
        assertSuccess(getReport("session-type-desc"));
    }

    @Test
    @Ignore
    public void shouldReturnTheReportSortedBySessionTypeDesc() throws IOException {
        Map expectedJson = expectedJsonForSortedSession();
        Response response = getReport("session-type-desc");
        assertSuccess(response);
        Map actualJson = responseJson(response);
        assertThat(expectedJson, is(actualJson));
    }

    private Response getReport(String orderBy) {
        return target("/runatic/report").queryParam("order_by", orderBy).request().get();
    }

    private void assertSuccess(Response response) {
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    private Map responseJson(Response response) throws IOException {
        return generateJson(response.readEntity(String.class));
    }

    private Map generateJson(String jsonString) throws IOException {
        return new ObjectMapper().readValue(jsonString.getBytes(), Map.class);
    }

    private Map expectedJsonForSortedSession() throws IOException {
        String expectedJson = "{ " +
                "\"summaries\":" +
                "{\"runa-summary\": { \"unit-price-dollars\":360.5, \"merchant-discount-dollars\":15.0, \"runa-discount-dollars\":65.2}, " +
                "\"merchant-summary\":{ \"unit-price-dollars\":360.0, \"merchant-discount-dollars\":12.2, \"runa-discount-dollars\":65.3}}, " +
                "\"orders\":" +
                "[" +
                "{" +
                "\"runa-data\":{\"unit-price-dollars\":50.0, \"merchant-discount-dollars\":5.0, \"runa-discount-dollars\":15.0, \"session-type\":\"unmanaged\", \"order-id\": \"72145239\"}, " +
                "\"merchant-data\":{\"unit-price-dollars\":50.0, \"merchant-discount-dollars\":2.0, \"runa-discount-dollars\":14.8, \"session-type\":\"unmanaged\", \"order-id\": \"72145239\"}" +
                "}, " +
                "{" +
                "\"runa-data\":{\"unit-price-dollars\":200.0, \"merchant-discount-dollars\":0.0, \"runa-discount-dollars\":30.0, \"session-type\":\"test\", \"order-id\": \"72144777\"}, " +
                "\"merchant-data\":{\"unit-price-dollars\":200.0, \"merchant-discount-dollars\":0.0, \"runa-discount-dollars\":30.0, \"session-type\":\"test\", \"order-id\": \"72144777\"}" +
                "}, " +
                "{" +
                "\"runa-data\":{\"unit-price-dollars\":110.5, \"merchant-discount-dollars\":10.0, \"runa-discount-dollars\":20.2, \"session-type\":\"control\", \"order-id\": \"72144305\"}, " +
                "\"merchant-data\":{\"unit-price-dollars\":110.0, \"merchant-discount-dollars\":10.2, \"runa-discount-dollars\":20.5, \"session-type\":\"control\", \"order-id\": \"72144305\"}" +
                "}" +
                "]" +
                "}" +
                "}";
        return generateJson(expectedJson);
    }
}
