package edu.wpi.first.embeddedtools.deploy.target;

import java.util.function.Predicate;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.gradle.api.Action;
import org.gradle.api.DomainObjectCollection;
import org.gradle.api.Named;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskProvider;

import edu.wpi.first.embeddedtools.EmbeddedTools;
import edu.wpi.first.embeddedtools.deploy.artifact.Artifact;
import edu.wpi.first.embeddedtools.deploy.artifact.ArtifactDeployTask;
import edu.wpi.first.embeddedtools.deploy.context.DeployContext;
import edu.wpi.first.embeddedtools.deploy.target.discovery.TargetDiscoveryTask;
import edu.wpi.first.embeddedtools.deploy.target.location.DeployLocation;
import edu.wpi.first.embeddedtools.deploy.target.location.DeployLocationSet;

public class RemoteTarget implements Named {
    private final Logger log;
    private final String name;
    private final Project project;
    private final TaskProvider<Task> deployTask;
    private final TaskProvider<TargetDiscoveryTask> targetDiscoveryTask;
    private String targetPlatform;

    @Inject
    public RemoteTarget(String name, Project project) {
        this.name = name;
        this.project = project;
        this.dry = EmbeddedTools.isDryRun(project);
        locations = project.getObjects().newInstance(DeployLocationSet.class, project, this);
        log = Logger.getLogger(toString());
        deployTask = project.getTasks().register("deploy" + name, task -> {
            task.setGroup("EmbeddedTools");
            task.setDescription("Deploy task for " + name);
        });
        targetDiscoveryTask = project.getTasks().register("discover" + name, TargetDiscoveryTask.class, task -> {
            task.setGroup("EmbeddedTools");
            task.setDescription("Determine the address(es) of target " + name);
            task.setTarget(this);
        });
    }

    public String getTargetPlatform() {
        return targetPlatform;
    }

    public void setTargetPlatform(String targetPlatform) {
        this.targetPlatform = targetPlatform;
    }

    public TaskProvider<Task> getDeployTask() {
        return deployTask;
    }

    public TaskProvider<TargetDiscoveryTask> getTargetDiscoveryTask() {
        return targetDiscoveryTask;
    }

    private String directory = null;

    public String getDirectory() {
        return directory;
    }

    public void artifactAdded(Artifact artifact, TaskProvider<ArtifactDeployTask> task) {
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    private int timeout = 3;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private boolean failOnMissing = true;

    public boolean isFailOnMissing() {
        return failOnMissing;
    }

    public void setFailOnMissing(boolean failOnMissing) {
        this.failOnMissing = failOnMissing;
    }

    private int maxChannels = 1;

    public int getMaxChannels() {
        return maxChannels;
    }

    public void setMaxChannels(int maxChannels) {
        this.maxChannels = maxChannels;
    }

    private boolean dry = false;

    public boolean isDry() {
        return dry;
    }

    public void setDry(boolean dry) {
        this.dry = dry;
    }

    private final DeployLocationSet locations;

    public DeployLocationSet getLocations() {
        return locations;
    }

    private Predicate<DeployContext> onlyIf = null;;

    public Predicate<DeployContext> getOnlyIf() {
        return onlyIf;
    }

    public void setOnlyIf(Predicate<DeployContext> onlyIf) {
        this.onlyIf = onlyIf;
    }

    @Override
    public String getName() {
        return name;
    }

    public Project getProject() {
        return project;
    }

    public void locations(final Action<DomainObjectCollection<? extends DeployLocation>> action) {
        action.execute(locations);
    }

    @Override
    public String toString() {
        return "RemoteTarget[" + name + "]";
    }

    public boolean verify(DeployContext ctx) {
        if (onlyIf == null) {
            return true;
        }

        log.debug("OnlyIf...");
        boolean toConnect = onlyIf.test(ctx);
        if (!toConnect) {
            log.debug("OnlyIf check failed! Not connecting...");
            return false;
        }
        return true;
    }

}
