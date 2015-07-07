package com.staples.runatic.data;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RowHandlerTest {

    @Test
    public void shouldThrowAnExceptionWhenTheDelimiterIsNotFound() throws Exception {
        try {
            RowHandler.handlerFor("*");
            fail("Should have thrown an exception because of an invalid delimiter");
        } catch(RuntimeException expected) {
            assertThat(expected.getMessage(), is("No row handlers found for the delimiter '*'"));
        }
    }

    @Test
    public void shouldParseCommaSeparatedRow() throws Exception {
        String[] rows = RowHandler.handlerFor(RowHandler.COMMA).handle("first, second, third");
        assertThat(rows, is(new String[] {"first", "second", "third"}));
    }

    @Test
    public void shouldParsePipeliSeparatedRow() throws Exception {
        String[] rows = RowHandler.handlerFor(RowHandler.PIPE).handle("first | second| third | ");
        assertThat(rows, is(new String[] {"first", "second", "third", ""}));
    }
}
