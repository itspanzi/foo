package com.staples.runatic.data;

import com.staples.runatic.model.SessionEntry;

import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class StaplesSessionData extends AbstractSessionDao {
    public StaplesSessionData() {
        this("staples_data.csv");
    }

    StaplesSessionData(String fileName) {
        super(fileName, RowHandler.COMMA);
    }

    @Override
    protected Function<String, SessionEntry> rowMapper(RowHandler rowHandler) {
        return line -> {
            String[] cells = rowHandler.handle(line);
            return new SessionEntry(cells[0], parseInt(cells[1]), parseInt(cells[2]), parseInt(cells[3]), cells[4]);
        };
    }

    @Override
    protected String[] expectedHeaders() {
        return new String[]{"Order ID", "Unit Price Cents", "Merchant Discount Cents", "Runa Discount Cents", "Session Type"};
    }
}
