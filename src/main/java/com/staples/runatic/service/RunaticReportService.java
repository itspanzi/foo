package com.staples.runatic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.staples.runatic.dao.ExternalSessionDao;
import com.staples.runatic.dao.StaplesSessionDao;
import com.staples.runatic.model.Report;
import com.staples.runatic.model.SessionEntry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/runatic")
public class RunaticReportService {

    private static final String ERROR_JSON = "{ 'message' : 'There was an error while processing the request. Please check the logs for more in.' }";

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@DefaultValue("session-type-desc") @QueryParam("order_by") String orderBy) throws IOException {
        List<SessionEntry> externalData =  readExternalSessionData();
        List<SessionEntry> internalData =  readStaplesSessionData();
        Report report = prepareReport(externalData, internalData);
        try {
            return Response.status(Response.Status.OK).entity(toJson(report)).build();
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private Report prepareReport(List<SessionEntry> externalData, List<SessionEntry> internalData) {
        return new Report();
    }

    private List<SessionEntry> readStaplesSessionData() throws IOException {
        return new StaplesSessionDao().sessionEntries();
    }

    private List<SessionEntry> readExternalSessionData() {
        return new ExternalSessionDao().sessionEntries();
    }

    private Response errorResponse() {
        return Response.status(422).entity(ERROR_JSON).build();
    }

    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper.writeValueAsString(object);
    }
}
