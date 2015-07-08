package com.staples.runatic.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SummaryTest {

    @Test
    public void shouldSerialiseWithTheExpectedNames() throws Exception {
        Summary summary = new Summary(3400, 3499, 100);
        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(summary), Map.class);
        assertThat(map.containsKey("unit-price-dollars"), is(true));
        assertThat(map.containsKey("merchant-discount-dollars"), is(true));
        assertThat(map.containsKey("runa-discount-dollars"), is(true));
    }
}