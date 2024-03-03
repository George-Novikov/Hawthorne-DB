package com.georgen.hawthorne.io;

import java.io.File;
import java.io.IOException;

public class FileOperation implements AutoCloseable {
    private File file;

    public FileOperation(String path, boolean isFileCreated) throws IOException {
        this.file = FileFactory.getInstance().getFile(path, isFileCreated);
    }

    public File getFile(){
        return this.file;
    }

    public boolean delete(){
        return FileFactory.getInstance().delete(this.file);
    }

    public boolean isExistingFile() throws IOException {
        return this.file.exists();
    }

    @Override
    public void close() throws Exception {
        if (this.file != null) FileFactory.getInstance().releaseFromMemory(this.file);
    }
}
