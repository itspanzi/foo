package com.staples.runatic.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.staples.runatic.data.ExternalSessionData;
import com.staples.runatic.data.StaplesSessionData;
import com.staples.runatic.model.Report;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.staples.runatic.model.Report.SESSION_TYPE_DESC;

@Path("/runatic")
public class RunaticReportService {

    private static final String ERROR_JSON = "{ 'message' : 'There was an error while processing the request. Please check the logs for more in.' }";

    private final StaplesSessionData staplesSessionData;
    private final ExternalSessionData externalSessionData;

    public RunaticReportService() {
        staplesSessionData = new StaplesSessionData();
        externalSessionData = new ExternalSessionData();
    }

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@DefaultValue(SESSION_TYPE_DESC) @QueryParam("order_by") String orderBy) throws IOException {
        Report report = prepareReport(orderBy);
        try {
            return Response.status(Response.Status.OK).entity(toJson(report)).build();
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private Report prepareReport(String orderBy) {
        Report report = new Report(staplesSessionData, externalSessionData);
        report.generateReport(orderBy);
        return report;
    }

    private Response errorResponse() {
        return Response.status(422).entity(ERROR_JSON).build();
    }

    private String toJson(Report report) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE) ;
        return objectMapper.writeValueAsString(report);
    }
}
