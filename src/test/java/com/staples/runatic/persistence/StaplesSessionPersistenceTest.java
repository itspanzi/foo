package com.staples.runatic.persistence;

import com.staples.runatic.model.SessionEntry;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class StaplesSessionPersistenceTest {

    @Test
    public void shouldReturnSessionDataForStaplesStore() {
        List<SessionEntry> entries = new StaplesSessionPersistence().entriesStream().collect(Collectors.toList());
        assertThat(entries.size(), is(3));
        assertThat(entries.get(0), is(new SessionEntry("72144305", 11050, 1000, 2020, "control")));
        assertThat(entries.get(1), is(new SessionEntry("72144777", 20000, 0, 3000, "test")));
        assertThat(entries.get(2), is(new SessionEntry("72145239", 5000, 500, 1500, "unmanaged")));
    }

    @Test
    public void shouldThrowAnExceptionIfTheRowHeadersAreNotWhatIsExpected() {
        try {
            new StaplesSessionPersistence("different_staples_data.csv").entriesStream().collect(Collectors.toList());
            fail("Should have gotten an exception since the data format has changed");
        } catch (RuntimeException expected) {
            assertThat(expected.getMessage(), is("The format of the external file has changed. Cannot parse this file."));
        }
    }

    @Test
    public void shouldThrowAnExceptionIfTheDataStoreFileIsNotFound() {
        try {
            new StaplesSessionPersistence("foo.csv").entriesStream().collect(Collectors.toList());
            fail("Should have gotten an exception since the data store is not found");
        } catch (RuntimeException expected) {
            assertThat(expected.getMessage(), is("Oh oh! The data store 'foo.csv' is not found. Something is not right here."));
        }
    }
}
