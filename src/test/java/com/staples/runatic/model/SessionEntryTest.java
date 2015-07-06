package com.staples.runatic.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SessionEntryTest {

    @Test
    public void shouldCreateSessionEntryForStaplesRowData() {
        String header = "Order ID,Unit Price Cents,Merchant Discount Cents,Runa Discount Cents,Session Type";
        String row = "72144305,11050,1000,2020,control";
        SessionEntry sessionEntry = SessionEntry.fromRunaDataStore(header.split(","), row.split(","));
        assertThat(sessionEntry, is(new SessionEntry(72144305, 11050, 1000, 2020, "control")));
    }

    @Test
    public void shouldCreateSessionEntryForExternalDataSourceRowData() {
        String header = "Order ID|Unit Price Dollars|Runa Discount Dollars|Merchant Discount Dollars|Session Type";
        String row = "72144305|110.0|20.5|10.2|CONTROL";
        SessionEntry sessionEntry = SessionEntry.fromExternalDataStore(header.split("\\|"), row.split("\\|"));
        assertThat(sessionEntry, is(new SessionEntry(72144305, 11000, 1020, 2050, "control")));
    }
}