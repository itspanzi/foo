package com.staples.runatic.service.cache;

import com.staples.runatic.model.SessionEntry;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;

public class SessionEntryCacheTest {

    @Test
    public void shouldReturnADeepCloneOfCachedValue() throws Exception {
        SessionEntryCache cache = new SessionEntryCache();
        Map<Long, SessionEntry> toBeCached = sampleValues();
        Map<Long, SessionEntry> firstActual = cache.get("foo", () -> toBeCached);
        Map<Long, SessionEntry> secondActual = cache.get("foo", () -> null);

        assertThat(firstActual, is(toBeCached));
        assertThat(firstActual, not(sameInstance(toBeCached)));

        assertThat(secondActual, is(toBeCached));
        assertThat(secondActual, not(sameInstance(toBeCached)));
    }

    private Map<Long, SessionEntry> sampleValues() {
        Map<Long, SessionEntry> orderIdToEntries = new HashMap<>();
        orderIdToEntries.put(1234L, new SessionEntry("1234", 100, 18, 15, "control"));
        orderIdToEntries.put(5678L, new SessionEntry("5678", 3500, 0, 100, "test"));
        orderIdToEntries.put(4455L, new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"));
        return orderIdToEntries;
    }
}