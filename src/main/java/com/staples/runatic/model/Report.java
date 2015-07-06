package com.staples.runatic.model;

import java.util.HashMap;
import java.util.Map;

public class Report {
    private final Map<String, Summary> summaries;

    public Report() {
        summaries = new HashMap<>();
    }
}
