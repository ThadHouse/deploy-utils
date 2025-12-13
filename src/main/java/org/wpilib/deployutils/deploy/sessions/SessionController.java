package org.wpilib.deployutils.deploy.sessions;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.wpilib.deployutils.deploy.CommandDeployResult;

public interface SessionController extends AutoCloseable {
    CommandDeployResult execute(String command);

    void put(Map<String, File> files);

    void put(InputStream source, String dest);

    void delete(List<String> files);

    String friendlyString();
}
