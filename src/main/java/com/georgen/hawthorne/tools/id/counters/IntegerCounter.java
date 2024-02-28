package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.File;
import java.io.IOException;

public class IntegerCounter extends IdCounter {
    private File counterFile;
    public IntegerCounter(StorageArchetype archetype) throws IOException {
        this.counterFile = FileFactory.getFile(
                PathBuilder.concat(
                        archetype.getPath(),
                        SystemProperty.ID_COUNTER_NAME.getValue()
                )
        );
    }

    @Override
    public Object getNext() {
        return null;
    }
}
