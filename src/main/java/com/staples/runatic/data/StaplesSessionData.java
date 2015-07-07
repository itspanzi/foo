package com.staples.runatic.data;

import com.staples.runatic.model.SessionEntry;

import java.util.function.Function;

public class StaplesSessionData extends AbstractSessionDao {
    public StaplesSessionData() {
        this("staples_data.csv");
    }

    StaplesSessionData(String fileName) {
        super(fileName, RowHandler.COMMA);
    }

    @Override
    protected Function<String, SessionEntry> rowMapper(RowHandler rowHandler) {
        return line -> SessionEntry.fromRunaDataStore(new String[]{}, rowHandler.handle(line));
    }

    @Override
    protected String[] expectedHeaders() {
        return new String[] { "Order ID", "Unit Price Cents", "Merchant Discount Cents", "Runa Discount Cents", "Session Type"};
    }
}
