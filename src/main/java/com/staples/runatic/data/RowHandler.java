package com.staples.runatic.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                return trimCells(row.split(COMMA));
            }
        });
        rowHandlers.put(PIPE, new RowHandler() {
            @Override
            public String[] handle(String row) {
                return trimCells(row.split(PIPE_SPLITTER_REGEX));
            }
        });
    }

    private static String[] trimCells(String[] cells) {
        List<String> collect = Arrays.stream(cells).map(String::trim).collect(Collectors.toList());
        return collect.toArray(new String[collect.size()]);
    }

    public abstract String[] handle(String row);

    public static RowHandler handlerFor(String delimiter) {
        RowHandler rowHandler = rowHandlers.get(delimiter);
        if (rowHandler == null) {
            throw new RuntimeException(String.format("No row handlers found for the delimiter '%s'", delimiter));
        }
        return rowHandler;
    }
}
