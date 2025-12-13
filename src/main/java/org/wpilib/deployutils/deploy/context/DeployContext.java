package org.wpilib.deployutils.deploy.context;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.wpilib.deployutils.deploy.CommandDeployResult;
import org.wpilib.deployutils.deploy.cache.CacheMethod;
import org.wpilib.deployutils.deploy.sessions.SessionController;
import org.wpilib.deployutils.deploy.target.location.DeployLocation;
import org.wpilib.deployutils.log.ETLogger;

public interface DeployContext {
    SessionController getController();

    ETLogger getLogger();

    String getWorkingDir();

    DeployLocation getDeployLocation();

    CommandDeployResult execute(String command);

    // Send a batch of files
    void put(Map<String, File> files, CacheMethod cache);

    // Send a single file
    void put(File source, String dest, CacheMethod cache);

    // Send multiple files, and trigger cache checking only once
    void put(Set<File> files, CacheMethod cache);

    // Put an input stream, with no caching
    void put(InputStream source, String dest);

    void delete(Stream<String> files);

    String friendlyString();

    DeployContext subContext(String workingDir);
}
