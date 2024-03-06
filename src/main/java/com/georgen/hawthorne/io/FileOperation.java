package com.georgen.hawthorne.io;

import com.georgen.hawthorne.io.comparators.PathComparator;
import com.georgen.hawthorne.model.constants.FileExtension;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileOperation implements AutoCloseable {
    private static final PathComparator PATH_COMPARATOR = new PathComparator();
    private FileFactory fileFactoryInstance;
    private File file;

    public FileOperation(String path, boolean isFileCreated) throws IOException, HawthorneException {
        this.fileFactoryInstance = FileFactory.getInstance();
        this.file = fileFactoryInstance.getFile(path, isFileCreated);
    }

    public File getFile(){
        return this.file;
    }

    public long countByExtension(FileExtension extension) throws HawthorneException, IOException {
        if (!this.file.isDirectory()) throw new HawthorneException(Message.NOT_A_DIRECTORY);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file.toPath())){
            return StreamSupport
                    .stream(directoryStream.spliterator(), false)
                    .filter(path -> path.toString().endsWith(extension.getValue()))
                    .count();
        }
    }

    public List<File> listFilesByExtension(FileExtension extension, int limit, int offset) throws HawthorneException, IOException {
        if (!this.file.isDirectory()) throw new HawthorneException(Message.NOT_A_DIRECTORY);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file.toPath())){

            AtomicInteger offsetCounter = new AtomicInteger();
            AtomicInteger limitCounter = new AtomicInteger();

            Stream<Path> paths = StreamSupport
                    .stream(directoryStream.spliterator(), false)
                    .filter(path -> path.toString().endsWith(extension.getValue()))
                    .sorted(PATH_COMPARATOR)
                    .filter(path -> offsetCounter.incrementAndGet() > offset);

            List<File> files = paths
                    .filter(path -> limitCounter.incrementAndGet() <= limit)
                    .map(path -> path.toFile())
                    .collect(Collectors.toList());

            return files;
        }
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
