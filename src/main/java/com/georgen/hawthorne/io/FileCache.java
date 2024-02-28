package com.georgen.hawthorne.io;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileCache {
    private ConcurrentMap<String, File> inMemoryFiles;
    private ConcurrentMap<String, LocalDateTime> timeStamps;

    private FileCache(){
        this.inMemoryFiles = new ConcurrentHashMap();
        this.timeStamps = new ConcurrentHashMap();
    }

    public static FileCache getInstance(){
        return FileCacheHolder.INSTANCE;
    }

    private static class FileCacheHolder {
        private static final FileCache INSTANCE = new FileCache();
    }

    public File register(String path, File file){
        File registeredFile = this.inMemoryFiles.put(path, file);
        this.timeStamps.put(path, LocalDateTime.now());
        return registeredFile;
    }

    public File get(String path){
        return (File) this.inMemoryFiles.get(path);
    }

    public File release(){

    }

    public void clearOld(){

    }

    public void purge(){

    }
}
