package com.georgen.hawthorne.io;

import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.SystemHelper;
import com.georgen.hawthorne.tools.id.counters.IntegerCounter;
import com.georgen.hawthorne.tools.id.counters.LongCounter;
import com.georgen.hawthorne.tools.id.counters.UuidCounter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileFactory {
    private static final List<Class> PERMITTED_CALLERS = Arrays.asList(
            StorageSettings.class, FileOperation.class,
            IntegerCounter.class, LongCounter.class, UuidCounter.class
    );

    // This map ensures that all requested files with the same path will reference to the same File object,
    // thus, properly synchronizing all input/output operations
    private ConcurrentMap<String, File> fileCache;

    private FileFactory(){ this.fileCache = new ConcurrentHashMap<>(); }

    public File getFile(String path, boolean isCreated) throws IOException {
        File operatedFile = fileCache.get(path);
        if (operatedFile != null) return operatedFile;

        File file = new File(path);
        if (isCreated && !file.exists()) file = createFile(file);
        if (file.exists()) fileCache.put(path, file);

        return file;
    }

    public boolean delete(File file){
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

    public void registerInCache(File file){
        this.fileCache.put(file.getPath(), file);
    }

    public File releaseFromCache(File file){
        File releasedFile = fileCache.remove(file.getPath());
        return releasedFile;
    }

    private File createFile(File file) throws IOException {
        synchronized (file){
            file.getParentFile().mkdirs();
            file.createNewFile();
            setFilePermissions(file);
            return file;
        }
    }

    private void setFilePermissions(File file) throws IOException {
        if (!SystemHelper.isUnixSystem()) return;
        SystemHelper.setFilePermissions(file);
    }

    public static FileFactory getInstance() throws HawthorneException {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        boolean isPermittedCaller = PERMITTED_CALLERS.stream().anyMatch(caller -> caller.getName().equals(stackTrace[2].getClassName()));
        if (!isPermittedCaller) throw new HawthorneException(Message.PERMISSION_DENIED);
        return FileFactoryHolder.INSTANCE;
    }

    private static class FileFactoryHolder{
        private static final FileFactory INSTANCE = new FileFactory();
    }
}
