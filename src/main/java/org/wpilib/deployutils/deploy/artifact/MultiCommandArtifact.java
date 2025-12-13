package org.wpilib.deployutils.deploy.artifact;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.gradle.api.GradleException;
import org.wpilib.deployutils.deploy.CommandDeployResult;
import org.wpilib.deployutils.deploy.context.DeployContext;
import org.wpilib.deployutils.deploy.target.RemoteTarget;

public class MultiCommandArtifact extends AbstractArtifact {


    // Mapping name to command
    // Linked hash map is ordered by insertion order
    private Map<String, String> commandNameMap = new LinkedHashMap<>();
    private Map<String, CommandDeployResult> resultMap = new HashMap<>();

    @Inject
    public MultiCommandArtifact(String name, RemoteTarget target) {
        super(name, target);
    }

    @Override
    public void deploy(DeployContext context) {
        for (Map.Entry<String, String> command : commandNameMap.entrySet()) {
            CommandDeployResult result = context.execute(command.getValue());
            resultMap.put(command.getKey(), result);
        }
    }

    public Map<String, String> getCommands() {
        return commandNameMap;
    }

    public void addCommand(String name, String command) {
        String oldCommand = commandNameMap.putIfAbsent(name, command);
        if (oldCommand != null) {
            throw new GradleException("Key already exists");
        }
    }

    public Map<String, CommandDeployResult> getResults() {
        return resultMap;
    }
}
