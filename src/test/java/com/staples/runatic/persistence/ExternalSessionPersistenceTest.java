package com.staples.runatic.persistence;

import com.staples.runatic.model.SessionEntry;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;

public class ExternalSessionPersistenceTest {

    @Test
    public void shouldReturnSessionDataForExternalStore() {
        List<SessionEntry> entries = new ExternalSessionPersistence().entriesStream().collect(Collectors.toList());
        assertThat(entries.size(), is(3));
        assertThat(entries.get(0), is(new SessionEntry("72144305", 11000, 1020, 2050, "control")));
        assertThat(entries.get(1), is(new SessionEntry("72144777", 20000, 0, 3000, "test")));
        assertThat(entries.get(2), is(new SessionEntry("72145239", 5000, 200, 1480, "unmanaged")));
    }

    @Test
    public void shouldThrowAnExceptionIfTheRowHeadersAreNotWhatIsExpected() {
        try {
            new ExternalSessionPersistence("different_external_data.psv").entriesStream().collect(Collectors.toList());
            fail("Should have gotten an exception since the data format has changed");
        } catch (RuntimeException expected) {
            assertThat(expected.getMessage(), is("The format of the external file has changed. Cannot parse this file."));
        }
    }
}