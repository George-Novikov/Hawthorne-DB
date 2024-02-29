package com.georgen.hawthorne.io;

import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.tools.SystemHelper;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileFactory {
    /**
     * This map ensures that all requested files with the same path will reference to the same File object,
     * thus, properly synchronizing all input/output operations
     */
    private static ConcurrentMap<String, File> fileCache = new ConcurrentHashMap();

    public static File getFile(String path) throws IOException {
        File operatedFile = fileCache.get(path);
        if (operatedFile != null) return operatedFile;

        File file = new File(path);
        if (!file.exists()) file = createFile(file);
        fileCache.put(path, file);

        return file;
    }

    public static boolean isExistingFile(String path) throws IOException {
        File file = getFile(path);
        return file.exists();
    }

    public static boolean delete(File file){
        String path = file.getPath();
        if (path.endsWith(SystemProperty.ID_COUNTER_NAME.getValue())) return false;

        AtomicBoolean isDeletedAtomically = new AtomicBoolean();

        synchronized (file){
            isDeletedAtomically.set(file.delete());
        }

        boolean isDeleted = isDeletedAtomically.get();
        if (isDeleted) fileCache.remove(path);

        return isDeleted;
    }

    public static File releaseFromMemory(File file){
        return fileCache.remove(file);
    }

    private static File createFile(File file) throws IOException {
        synchronized (file){
            file.getParentFile().mkdirs();
            file.createNewFile();
            setFilePermissions(file);
            return file;
        }
    }

    private static void setFilePermissions(File file) throws IOException {
        if (!SystemHelper.isUnixSystem()) return;
        SystemHelper.setFilePermissions(file);
    }
}
