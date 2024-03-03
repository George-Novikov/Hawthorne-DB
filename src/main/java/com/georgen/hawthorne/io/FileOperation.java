package com.georgen.hawthorne.io;

import com.georgen.hawthorne.model.constants.FileExtension;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileOperation implements AutoCloseable {
    private FileFactory fileFactoryInstance;
    private File file;

    public FileOperation(String path, boolean isFileCreated) throws IOException, HawthorneException {
        this.fileFactoryInstance = FileFactory.getInstance();
        this.file = fileFactoryInstance.getFile(path, isFileCreated);
    }

    public File getFile(){
        return this.file;
    }

    public List<File> listEntityFiles(int limit, int offset) throws HawthorneException {
        if (!this.file.isDirectory()) throw new HawthorneException(Message.NOT_A_DIRECTORY);

        FileFilter entityFilter = new FileFilter(FileExtension.ENTITY_FILE_EXTENSION, limit, offset);
        List<File> entityFiles = Arrays.stream(this.file.listFiles(entityFilter)).collect(Collectors.toList());
        entityFiles.stream().forEach(file -> fileFactoryInstance.registerInCache(file));

        return entityFiles;
    }

    public List<File> listBinaryFiles(int limit, int offset) throws HawthorneException {
        if (!this.file.isDirectory()) throw new HawthorneException(Message.NOT_A_DIRECTORY);

        FileFilter binaryDataFilter = new FileFilter(FileExtension.BINARY_DATA_EXTENSION, limit, offset);
        List<File> entityFiles = Arrays.stream(this.file.listFiles(binaryDataFilter)).collect(Collectors.toList());
        entityFiles.stream().forEach(file -> fileFactoryInstance.registerInCache(file));

        return entityFiles;
    }

    public boolean delete() throws HawthorneException {
        return fileFactoryInstance.delete(this.file);
    }

    public boolean isExistingFile() throws IOException {
        return this.file.exists();
    }

    @Override
    public void close() throws Exception {
        if (this.file != null) fileFactoryInstance.releaseFromCache(this.file);
    }
}
