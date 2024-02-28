package com.georgen.hawthorne.tools.id.counters;

import java.util.UUID;

public class UuidCounter extends IdCounter<String> {
    @Override
    public String getNext() {
        return UUID.randomUUID().toString();
    }
}
