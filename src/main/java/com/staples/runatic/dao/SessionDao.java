package com.staples.runatic.dao;

import com.staples.runatic.model.SessionEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents accessing the session data stored across the different data stores (internal staples,
 * 3rd party vendor etc).
 */
public abstract class SessionDao {

    private final File dataStore;
    private final RowHandler rowHandler;

    public SessionDao(String dataStoreName, String separator) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(dataStoreName);
        if (resource == null) {
            throw new RuntimeException(String.format("Oh oh! The data store '%s' is not found. Something is not right here.", dataStoreName));
        }
        this.dataStore = new File(resource.getFile());
        this.rowHandler = RowHandler.handlerFor(separator);
    }

    protected abstract Function<String, SessionEntry> rowMapper(String[] headers, RowHandler rowHandler);

    public List<SessionEntry> sessionEntries() {
        try (Stream<String> firstLineStream = Files.lines(Paths.get(dataStore.toURI()))) {
            return sessionEntries(headers(firstLineStream));
        } catch (IOException e) {
            throw new RuntimeException(String.format("There was an error while reading the session entries from the data store: %s", dataStore.getName()));
        }
    }

    private List<SessionEntry> sessionEntries(String[] headers) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(dataStore.toURI()))) {
            try (Stream<SessionEntry> mapStream = lines.skip(1).map(rowMapper(headers, rowHandler))) {
                return mapStream.collect(Collectors.toList());
            }
        }
    }

    private String[] headers(Stream<String> firstLineStream) {
        String[] headers = rowHandler.handle(firstLineStream.findFirst().get());
        firstLineStream.close();
        return headers;
    }

}
