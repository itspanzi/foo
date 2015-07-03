package com.staples.runatic.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/runatic")
public class RunaticReportService {

    @GET
    @Path("/report")
    public String report() {
        return "";
    }
}
