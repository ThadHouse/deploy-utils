package edu.wpi.first.deployutils.deploy.context;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import edu.wpi.first.deployutils.deploy.CommandDeployResult;
import edu.wpi.first.deployutils.deploy.cache.CacheMethod;
import edu.wpi.first.deployutils.deploy.sessions.SessionController;
import edu.wpi.first.deployutils.deploy.target.location.DeployLocation;
import edu.wpi.first.deployutils.log.ETLogger;

public interface DeployContext {
    SessionController getController();

    ETLogger getLogger();

    String getWorkingDir();

    DeployLocation getDeployLocation();

    CommandDeployResult execute(String command);

    // Send a batch of files
    void put(Map<String, Callable<InputStream>> files, CacheMethod cache);

    // Send a single file
    default void put(String dest, Callable<InputStream> source, CacheMethod cache) {
        put(Map.of(dest, source), cache);
    }

    default void putFiles(Set<File> files, CacheMethod cache) {
        put(files.stream().collect(Collectors.toMap(x -> x.getName(), x -> {
            Path path = x.toPath();
            return () -> Files.newInputStream(path);
        })), cache);
    }

    default void putFiles(Map<String, File> files, CacheMethod cache) {
        put(files.entrySet().stream().collect(
                Collectors.toMap(x -> x.getKey(), x -> {
                    Path path = x.getValue().toPath();
                    return () -> Files.newInputStream(path);
                })), cache);
    }

    default void putFile(String dest, File source, CacheMethod cache) {
        Callable<InputStream> sourceGetter = () -> Files.newInputStream(source.toPath());
        put(Map.of(dest, sourceGetter), cache);
    }

    String friendlyString();

    DeployContext subContext(String workingDir);
}
