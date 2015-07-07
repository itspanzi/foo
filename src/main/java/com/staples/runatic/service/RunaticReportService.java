package com.staples.runatic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.staples.runatic.data.ExternalSessionData;
import com.staples.runatic.data.StaplesSessionData;
import com.staples.runatic.model.Report;
import com.staples.runatic.model.SessionEntry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/runatic")
public class RunaticReportService {

    private static final String ERROR_JSON = "{ 'message' : 'There was an error while processing the request. Please check the logs for more in.' }";

    @GET
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@DefaultValue("session-type-desc") @QueryParam("order_by") String orderBy) throws IOException {
        Stream<SessionEntry> externalData =  readExternalSessionData();
        Stream<SessionEntry> internalData =  readStaplesSessionData();
        Report report = prepareReport(externalData, internalData, orderBy);
        try {
            return Response.status(Response.Status.OK).entity(toJson(report)).build();
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private Report prepareReport(Stream<SessionEntry> externalData, Stream<SessionEntry> internalData, String orderBy) {

        Stream<SessionEntry> entries = Stream.concat(externalData, internalData).
                sorted(Comparator.comparing(SessionEntry::getSessionType).reversed());

        Map<Long, List<SessionEntry>> map = entries.collect(Collectors.groupingBy(SessionEntry::getOrderId));

        System.out.println("Map : " + map);

        return new Report();
    }

    private Stream<SessionEntry> readStaplesSessionData() throws IOException {
        return new StaplesSessionData().entriesStream();
    }

    private Stream<SessionEntry> readExternalSessionData() {
        return new ExternalSessionData().entriesStream();
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
