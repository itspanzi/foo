package com.staples.runatic.dao;

import com.staples.runatic.model.SessionEntry;

import java.util.function.Function;

public class ExternalSessionDao extends SessionDao {
    public ExternalSessionDao() {
        super("external_data.psv", RowHandler.PIPE);
    }
    protected Function<String, SessionEntry> rowMapper(String[] headers, RowHandler rowHandler) {
        return line -> SessionEntry.fromExternalDataStore(headers, rowHandler.handle(line));
    }
}
