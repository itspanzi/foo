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
 *  3rd party vendor etc).
 */
public class SessionDao {

    private final File dataStore;

    private SessionDao(String dataStoreName) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(dataStoreName);
        if (resource == null) {
            throw new RuntimeException(String.format("Oh oh! The data store '%s' is not found. Something is not right here.", dataStoreName));
        }
        this.dataStore = new File(resource.getFile());
    }

    public static SessionDao runaStore() {
        return new SessionDao("staples_data.csv");
    }

    public List<SessionEntry> sessionEntries() {
        try (Stream<String> lines = Files.lines(Paths.get(dataStore.toURI()))) {
            try (Stream<SessionEntry> mapStream = lines.skip(1).map(rowMapper())) {
                return mapStream.collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("There was an error while reading the session entries from the data store: %s", dataStore.getName()));
        }
    }

    private Function<String, SessionEntry> rowMapper() {
        return line -> SessionEntry.fromStaplesStore(line.split(","));
    }
}
