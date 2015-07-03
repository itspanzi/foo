package com.staples.runatic.service;

import com.sun.deploy.net.HttpResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.core.Is;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class RunaticReportServiceTest extends JerseyTest {

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
