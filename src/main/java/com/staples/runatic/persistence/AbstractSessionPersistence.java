package com.staples.runatic.persistence;

import com.staples.runatic.model.SessionEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

/**
 * This class represents streaming session data that is stored in a file.
 */
public abstract class AbstractSessionPersistence {
    private static final Logger logger = Logger.getLogger(AbstractSessionPersistence.class.getName());

    private final File dataStore;
    private final RowHandler rowHandler;

    public AbstractSessionPersistence(String dataStoreName, String separator) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(dataStoreName);
        if (resource == null) {
            String message = String.format("Oh oh! The data store '%s' is not found. Something is not right here.", dataStoreName);
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        }
        this.dataStore = new File(resource.getFile());
        this.rowHandler = RowHandler.handlerFor(separator);
    }

    protected abstract Function<String, SessionEntry> rowMapper(RowHandler rowHandler);

    protected abstract String[] expectedHeaders();

    public Map<Long, SessionEntry> sessionByOrderId() {
        Map<Long, SessionEntry> groupedByOrder = new HashMap<>();
        // We assume that the order id is unique in the given store.
        for (Entry<Long, List<SessionEntry>> entry : groupedSessions().entrySet()) {
            groupedByOrder.put(entry.getKey(), entry.getValue().get(0));
        }
        return groupedByOrder;
    }

    Stream<SessionEntry> entriesStream() {
        try (Stream<String> firstLineStream = Files.lines(Paths.get(dataStore.toURI()))) {
            validateHeaders(headers(firstLineStream));
            Stream<String> lines = Files.lines(Paths.get(dataStore.toURI()));
            return lines.skip(1).map(rowMapper(rowHandler));
        } catch (IOException e) {
            String message = String.format("There was an error while reading the session entries from the data store: %s", dataStore.getName());
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        }
    }

    private Map<Long, List<SessionEntry>> groupedSessions() {
        return entriesStream().collect(groupingBy(SessionEntry::getOrderId));
    }

    private void validateHeaders(String[] headers) {
        if (!Arrays.equals(headers, expectedHeaders())) {
            String message = "The format of the file or the column order has changed. Cannot parse this file.";
            logger.log(Level.SEVERE, message);
            throw new RuntimeException(message);
        }
    }

    private String[] headers(Stream<String> firstLineStream) {
        return rowHandler.handle(firstLineStream.findFirst().get());
    }

}
