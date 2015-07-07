package com.staples.runatic.data;

import com.staples.runatic.model.SessionEntry;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents streaming session data that is stored in a file.
 */
public abstract class AbstractSessionDao {

    private final File dataStore;
    private final RowHandler rowHandler;

    public AbstractSessionDao(String dataStoreName, String separator) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(dataStoreName);
        if (resource == null) {
            throw new RuntimeException(String.format("Oh oh! The data store '%s' is not found. Something is not right here.", dataStoreName));
        }
        this.dataStore = new File(resource.getFile());
        this.rowHandler = RowHandler.handlerFor(separator);
    }

    protected abstract Function<String, SessionEntry> rowMapper(RowHandler rowHandler);

    protected abstract String[] expectedHeaders();

    public Stream<SessionEntry> entriesStream() {
        try (Stream<String> firstLineStream = Files.lines(Paths.get(dataStore.toURI()))) {
            validateHeaders(headers(firstLineStream));
            Stream<String> lines = Files.lines(Paths.get(dataStore.toURI()));
            return lines.skip(1).map(rowMapper(rowHandler));
        } catch (IOException e) {
            throw new RuntimeException(String.format("There was an error while reading the session entries from the data store: %s", dataStore.getName()));
        }
    }

    private void validateHeaders(String[] headers) {
        if (!Arrays.equals(headers, expectedHeaders())) {
            throw new RuntimeException("The format of the external file has changed. Cannot parse this file.");
        }
    }

    private String[] headers(Stream<String> firstLineStream) {
        return rowHandler.handle(firstLineStream.findFirst().get());
    }

}
