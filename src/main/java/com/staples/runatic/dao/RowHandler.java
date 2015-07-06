package com.staples.runatic.dao;

import java.util.HashMap;
import java.util.Map;

public abstract class RowHandler {
    public static final String COMMA = ",";
    public static final String PIPE = "|";
    private static final String PIPE_SPLITTER_REGEX = String.format("\\%s", PIPE);

    private static final Map<String, RowHandler> rowHandlers = new HashMap<>();

    static {
        initializeRowHandlers();
    }

    private static void initializeRowHandlers() {
        rowHandlers.put(COMMA, new RowHandler() {
            @Override
            public String[] handle(String row) {
                return row.split(COMMA);
            }
        });
        rowHandlers.put(PIPE, new RowHandler() {
            @Override
            public String[] handle(String row) {
                return row.split(PIPE_SPLITTER_REGEX);
            }
        });
    }

    public abstract String[] handle(String row);

    public static RowHandler handlerFor(String delimiter) {
        return rowHandlers.get(delimiter);
    }
}
