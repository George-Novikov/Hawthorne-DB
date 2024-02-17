package com.georgen.hawthorne.api.repositories;

import java.io.File;

public interface GenericRepository<T> {
    File save(T object);
}
