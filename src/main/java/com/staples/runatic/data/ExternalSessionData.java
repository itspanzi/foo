package com.staples.runatic.data;

import com.staples.runatic.model.SessionEntry;

import java.util.function.Function;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.round;

public class ExternalSessionData extends AbstractSessionDao {

    public ExternalSessionData() {
        this("external_data.psv");
    }

    ExternalSessionData(String fileName) {
        super(fileName, RowHandler.PIPE);
    }

    @Override
    protected Function<String, SessionEntry> rowMapper(RowHandler rowHandler) {
        return line -> {
            String[] cells = rowHandler.handle(line);
            return new SessionEntry(Long.parseLong(cells[0]), round(parseFloat(cells[1]) * 100),
                    round(parseFloat(cells[3]) * 100), round(parseFloat(cells[2]) * 100), cells[4].toLowerCase());
        };
    }

    @Override
    protected String[] expectedHeaders() {
        return new String[] { "Order ID", "Unit Price Dollars", "Runa Discount Dollars",
                "Merchant Discount Dollars", "Session Type"};
    }

}
