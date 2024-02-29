package com.georgen.hawthorne.io;

import java.io.File;
import java.io.IOException;

public class FileOperation implements AutoCloseable {
    private File file;

    public FileOperation(String path) throws IOException {
        this.file = FileFactory.getFile(path);
    }

    public File getFile(){
        return this.file;
    }

    @Override
    public void close() throws Exception {
        if (this.file != null) FileFactory.releaseFromMemory(this.file);
    }
}
