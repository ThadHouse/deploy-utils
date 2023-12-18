package edu.wpi.first.deployutils.deploy.sessions;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import edu.wpi.first.deployutils.deploy.CommandDeployResult;

public interface SessionController extends AutoCloseable {
    CommandDeployResult execute(String command, Optional<InputStream> standardInput);

    void put(Map<String, Callable<InputStream>> files);

    String friendlyString();
}
