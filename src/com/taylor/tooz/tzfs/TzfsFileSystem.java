package com.taylor.tooz.tzfs;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TzfsFileSystem extends FileSystem {

    @Override
    public FileSystemProvider provider() {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public String getSeparator() {
        return null;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        return null;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return null;
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return null;
    }

    @Override
    public Path getPath(String s, String... strings) {
        return null;
    }

    @Override
    public PathMatcher getPathMatcher(String s) {
        return null;
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return null;
    }

    @Override
    public WatchService newWatchService() throws IOException {
        return null;
    }

    public static void main(String[] args) {
        FileSystem defaultFileSystem = FileSystems.getDefault();
        System.out.println(defaultFileSystem.getSeparator());
        defaultFileSystem.getFileStores().forEach(System.out::println);

        try {
            // URI uri = URI.create("jar:file:/zipfstest.zip");
            URI uri = URI.create("memory:///?name=logfs");

            Map<String, String> providerProperties = new HashMap<>();
            providerProperties.put("create", "true");
            providerProperties.put("encoding", "UTF-8");
            FileSystem tzfsFileSystem = FileSystems.newFileSystem(uri, providerProperties);

            System.out.println(tzfsFileSystem.provider().getScheme());
            Path path = tzfsFileSystem.getPath("");
            System.out.println(path.getFileName());

            while(path.iterator().hasNext()) {
                System.out.println(path.iterator().next().getFileName());
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
}
