package com.staples.runatic.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SessionEntryTest {

    @Test
    public void shouldSerialiseWithTheExpectedNames() throws Exception {
        SessionEntry entry = new SessionEntry(72144305, 11000, 1020, 2050, "control");

        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(entry), Map.class);
        assertThat(map.containsKey("unit-price-dollars"), is(true));
        assertThat(map.containsKey("merchant-discount-dollars"), is(true));
        assertThat(map.containsKey("runa-discount-dollars"), is(true));
        assertThat(map.containsKey("session-type"), is(true));
        assertThat(map.containsKey("order-id"), is(true));
    }

    @Test
    public void shouldReturnTheRatesInDollars() throws Exception {
        SessionEntry entry = new SessionEntry(72144305, 11001, 1020, 2000, "control");

        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(entry), Map.class);

        assertThat(map.get("unit-price-dollars"), is(110.01));
        assertThat(map.get("merchant-discount-dollars"), is(10.2));
        assertThat(map.get("runa-discount-dollars"), is(20.0));
    }

}