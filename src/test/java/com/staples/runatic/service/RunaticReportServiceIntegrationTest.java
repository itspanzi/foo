package com.staples.runatic.service;

import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.jetty.JettyTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerException;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RunaticReportServiceIntegrationTest extends JerseyTest {

    // This runs a Jetty instance and tests the integration via the HTTP endpoint
    @Override
    protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
        return new JettyTestContainerFactory();
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(RunaticReportService.class);
    }

    @Test
    public void shouldReturnHttpSuccessWhenAccessingReport() {
        Response response = target("/runatic/report").request().get();
        assertThat(response.getStatus(), is(HttpStatus.OK_200));
    }

}