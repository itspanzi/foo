package com.staples.runatic.dao;

import com.staples.runatic.model.SessionEntry;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SessionDaoTest {

    @Test
    public void shouldReturnSessionDataForRunaStore() {
        List<SessionEntry> entries = new StaplesSessionDao().sessionEntries();
        assertThat(entries.size(), is(3));
        assertThat(entries.get(0), is(new SessionEntry(72144305, 11050, 1000, 2020, "control")));
        assertThat(entries.get(1), is(new SessionEntry(72144777, 20000, 0, 3000, "test")));
            assertThat(entries.get(2), is(new SessionEntry(72145239, 5000, 500, 1500, "unmanaged")));
    }

    @Test
    public void shouldReturnSessionDataForExternalStore() {
        List<SessionEntry> entries = new ExternalSessionDao().sessionEntries();
        assertThat(entries.size(), is(3));
        assertThat(entries.get(0), is(new SessionEntry(72144305, 11000, 1020, 2050, "control")));
        assertThat(entries.get(1), is(new SessionEntry(72144777, 20000, 0, 3000, "test")));
            assertThat(entries.get(2), is(new SessionEntry(72145239, 5000, 200, 1480, "unmanaged")));
    }
}
