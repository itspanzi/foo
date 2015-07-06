package com.staples.runatic.model;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SessionEntryTest {

    @Test
    public void shouldCreateSessionEntryForStaplesRowData() {
        String row = "72144305,11050,1000,2020,control";
        SessionEntry sessionEntry = SessionEntry.fromStaplesStore(row.split(","));
        assertThat(sessionEntry, is(new SessionEntry(72144305, 11050, 1000, 2020, "control")));
    }
}