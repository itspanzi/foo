package com.staples.runatic.data;

import com.staples.runatic.model.SessionEntry;

import java.util.function.Function;

public class ExternalSessionData extends AbstractSessionDao {

    public ExternalSessionData() {
        this("external_data.psv");
    }

    ExternalSessionData(String fileName) {
        super(fileName, RowHandler.PIPE);
    }

    @Override
    protected Function<String, SessionEntry> rowMapper(RowHandler rowHandler) {
        return line -> SessionEntry.fromExternalDataStore(new String[] {}, rowHandler.handle(line));
    }

    @Override
    protected String[] expectedHeaders() {
        return new String[] { "Order ID", "Unit Price Dollars", "Runa Discount Dollars",
                "Merchant Discount Dollars", "Session Type"};
    }
}
