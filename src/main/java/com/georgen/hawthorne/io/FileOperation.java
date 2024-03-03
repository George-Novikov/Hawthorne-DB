package com.georgen.hawthorne.io;

import com.georgen.hawthorne.io.comparators.FileIdNameComparator;
import com.georgen.hawthorne.io.comparators.PathComparator;
import com.georgen.hawthorne.io.filters.FileExtensionFilter;
import com.georgen.hawthorne.model.constants.FileExtension;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

public class FileOperation implements AutoCloseable {
    private static final FileIdNameComparator COMPARATOR = new FileIdNameComparator();
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

    public List<File> listFilesByExtension(FileExtension extension, int limit, int offset) throws HawthorneException, IOException {
        if (!this.file.isDirectory()) throw new HawthorneException(Message.NOT_A_DIRECTORY);

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(this.file.toPath())){
            for (Path entity : directoryStream){
                directoryStream
            }

            StreamSupport
                    .stream(directoryStream.spliterator(), false)
                    .sorted(PATH_COMPARATOR)
                    .filter(entity -> entity.get)
        }


        FilenameFilter entityFilter = new FileExtensionFilter(extension);
        File[] files = this.file.listFiles(entityFilter);
        Arrays.sort(files, COMPARATOR);

        int highestPossibleIndex = files.length-1;
        int startIndex = offset + 1 > highestPossibleIndex ? highestPossibleIndex : offset + 1;
        int endIndex = limit + startIndex > highestPossibleIndex ? highestPossibleIndex : limit + startIndex;

        List<File> entityFiles = Arrays.asList(files).subList(startIndex, endIndex);
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
