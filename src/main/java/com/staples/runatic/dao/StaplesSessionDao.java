package com.staples.runatic.dao;

import com.staples.runatic.model.SessionEntry;

import java.util.function.Function;

public class StaplesSessionDao extends SessionDao {
    public StaplesSessionDao() {
        super("staples_data.csv", RowHandler.COMMA);
    }

    protected Function<String, SessionEntry> rowMapper(String[] headers, RowHandler rowHandler) {
        return line -> SessionEntry.fromRunaDataStore(headers, rowHandler.handle(line));
    }

}
