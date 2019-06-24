package org.rhine.framework.core;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

public class Scanner {

    public static final Set<File> FILES = new HashSet<>();

    public void doScan(String packageNames) {
        loadClass(packageNames.replace(".", "/"), false);
    }

    private void loadClass(String path, boolean absolute) {
        File[] files;
        try {
            if (absolute) {
                files = new File(path).listFiles();
            } else {
                URI rootPath = Thread.currentThread().getContextClassLoader().getResource("").toURI();
                files = new File(rootPath.getPath() + path).listFiles();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getCause());
        }
        synchronized (Scanner.class) {
            for (File file : files) {
                if (file.isDirectory()) {
                    loadClass(file.getPath(), true);
                } else {
                    FILES.add(file);
                }
            }
        }
    }
}
