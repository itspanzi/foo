package com.staples.runatic.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrderTest {

    @Test
    public void shouldSerialiseWithTheExpectedNames() throws Exception {
        Order order = new Order(new SessionEntry("4455", 12500, 1500, 3000, "unmanaged"),
                                    new SessionEntry("4455", 12500, 1000, 3000, "unmanaged"));

        Map map = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(order), Map.class);

        assertThat(map.containsKey("runa-data"), is(true));
        assertThat(map.containsKey("merchant-data"), is(true));
    }

}
