package com.tray.webpieces.server.dev;

import org.digitalforge.sneakythrow.SneakyThrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.webpieces.util.file.VirtualFile;
import org.webpieces.util.file.VirtualFileImpl;
import org.webpieces.webserver.api.IDESupport;
import org.webpieces.webserver.api.ServerConfig;
import org.webpieces.webserver.api.WebpiecesServer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public abstract class TrayAbstractDevServer {

    private static Logger log = LoggerFactory.getLogger(TrayAbstractDevServer.class);
    protected final ServerConfig serverConfig;
    protected VirtualFile directory;
    protected ArrayList<VirtualFile> srcPaths;
    protected String[] args;

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private ExecutorService fileWatchThread = Executors.newFixedThreadPool(1, new MyFileWatchThreadFactory());

    public TrayAbstractDevServer(
            String name,
            boolean usePortZero
    ) {
        //In DevServer or ProdServerForIDE, if platform is upgraded, bad things can happen so we shutdown on platform upgrade
        //if we are currently running.
        String file = "/"+ WebpiecesServer.class.getName().replaceAll("\\.", "/")+".class";
        URL res = getClass().getResource(file);
        if (res.getProtocol().equals("jar")) {
            watchForDangerousJarChanges(res);
        }

        directory = IDESupport.modifyForIDE(name);

        String path = directory.getCanonicalPath();
        File f = new File(path);
        File javaDirectory = f.getParentFile().getParentFile();
        File libraries = new File(javaDirectory, "libraries");
        List<Path> libs = new LinkedList<>();
        try {
            Files.walkFileTree(libraries.toPath(), new LibraryFileVistor(libs));
        }
        catch(IOException ex) {
            throw SneakyThrow.sneak(ex);
        }

        //list all source paths here(DYNAMIC html files and java) as you add them(or just create for loop)
        //These are the list of directories that we detect java file changes under.  static source files(html, css, etc) do
        //not need to be recompiled each change so don't need to be listed here.
        srcPaths = new ArrayList<>();

        //We are going to put ALL source in the path(it's not all loaded though or scanned!!!) rather than figure out what you depend on
        //In this way, when something is asked for, all files are checked in O(1) time.
        srcPaths.add(directory.child("production/src/main/java"));
        srcPaths.add(directory.child("development/src/main/java"));
        for(Path lib : libs) {
            VirtualFile virtLib = new VirtualFileImpl(lib.toFile());
            VirtualFile child = virtLib.child("src/main/java");
            if(child.exists())
                srcPaths.add(child);

            //special case for generated grpc code
            VirtualFile child2 = virtLib.child("build/generated/source/proto/main/grpc");
            if(child2.exists())
                srcPaths.add(child2);

            VirtualFile child3 = virtLib.child("build/generated/source/proto/main/java");
            if(child3.exists())
                srcPaths.add(child3);
        }

        DevConfig config = getConfig();

        List<String> tempArgs = new ArrayList<>();
        if(usePortZero) {
            tempArgs.add("-http.port=:0");
            tempArgs.add("-https.port=:0");
        } else {
            tempArgs.add("-http.port=:" + config.getHttpPort());
            tempArgs.add("-https.port=:" + config.getHttpsPort());
        }

        tempArgs.add("-https.over.http=false");
        if(config.getHibernateSettingsClazz() != null)
            tempArgs.add("-hibernate.persistenceunit=" + config.getHibernateSettingsClazz());

        String[] args = config.getExtraArguments();

        if(args != null) {
            for (String a : args) {
                tempArgs.add(a);
            }
        }

        this.args = tempArgs.toArray(new String[0]);

        VirtualFile metaFile = directory.child( "development/src/main/resources/appmetadev.txt");

        // temporary fallback
        if(!metaFile.exists()) {
            metaFile = directory.child("production/src/main/resources/appmetadev.txt");
        }

        serverConfig = new ServerConfig(false);
        serverConfig.setValidateFlash(true);

        //It is very important to turn off BROWSER caching or developers will get very confused when they
        //change stuff and they don't see changes in the website
        serverConfig.setStaticFileCacheTimeSeconds(null);
        serverConfig.setMetaFile(metaFile);
        log.info("LOADING from meta file="+serverConfig.getMetaFile().getCanonicalPath());
    }

    protected abstract DevConfig getConfig();

    public abstract void start();


    private void watchForDangerousJarChanges(URL res) {
        try {
            watchForDangerousJarChangesImpl(res);
        } catch (IOException e) {
            throw new RuntimeException("Weird but this code can be safely removed BUT just make sure you restart your servers when upgrading any 3rd party jars");
        }
    }

    private void watchForDangerousJarChangesImpl(URL res) throws IOException {
        //It's always a jar in your code but sometimes we run Dev Server in webpieces where the code is not a jar
        //ie(you can delete the if statement if you like)
        log.info("res="+res.getFile()+" res="+res+" res1"+res.getPath());
        //register jar file listener so on changes, we shutdown the server on the developer and make them reboot
        String filePath = res.getFile();
        String absPath = filePath.substring("file:".length());
        String fullJarPath = absPath.split("!")[0];
        File f = new File(fullJarPath);
        Path directoryPath = f.getParentFile().toPath();

        WatchService watcher = FileSystems.getDefault().newWatchService();
        directoryPath.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        fileWatchThread.execute(new MyFileWatchRunnable(watcher));
    }

    private class MyFileWatchRunnable implements Runnable {

        private WatchService watcher;

        public MyFileWatchRunnable(WatchService watcher) {
            this.watcher = watcher;
        }

        @Override
        public void run() {
            WatchKey key;
            try {
                log.info("Starting to watch files");
                // wait for a key to be available
                key = watcher.take();
            } catch (Throwable ex) {
                log.error("Exception", ex);
            }

            log.error("\n-------------------------------------------------------------------------------\n"
                    + "Webpiecees was upgraded so we need to shutdown the server to use the new jar files or bad things happen\n"
                    +"-------------------------------------------------------------------------------");

            System.exit(9492);
        }
    }

    private class MyFileWatchThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("fileWatchThread");
            return t;
        }
    }

    private static class LibraryFileVistor extends SimpleFileVisitor<Path> {

        private static final String[] SKIP_LIST = {".gradle", "gradle", "src", "build", "output", "out"};

        private final List<Path> results;

        LibraryFileVistor(final List<Path> results) {
            this.results = results;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if(Files.exists(dir.resolve("build.gradle"))) {
                results.add(dir);
                return FileVisitResult.SKIP_SUBTREE;
            }
            String name = dir.getFileName().toString();
            for(String skip : SKIP_LIST) {
                if(skip.equalsIgnoreCase(name)) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
            }
            return FileVisitResult.CONTINUE;
        }

    }
}
