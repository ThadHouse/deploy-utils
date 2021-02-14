package edu.wpi.first.embeddedtools.deploy.artifact;

import java.util.List;
import java.util.function.Predicate;

import org.gradle.api.Action;
import org.gradle.api.Named;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskProvider;

import edu.wpi.first.embeddedtools.deploy.context.DeployContext;
import edu.wpi.first.embeddedtools.deploy.target.RemoteTarget;

public interface Artifact extends Named {
    Project getProject();

    TaskProvider<ArtifactDeployTask> getDeployTask();

    void setTarget(Object target);

    RemoteTarget getTarget();

    void dependsOn(Object... paths);

    List<Action<Artifact>> getPreWorkerThread();

    Property<String> getDirectory();

    List<Action<DeployContext>> getPredeploy();

    List<Action<DeployContext>> getPostdeploy();

    void setOnlyIf(Predicate<DeployContext> action);

    boolean isEnabled(DeployContext context);

    boolean isDisabled();
    void setDisabled();

    void deploy(DeployContext context);

    boolean isExplicit();
    void setExplicit(boolean explicit);

    public default ExtensionContainer getExtensionContainer() {
        if (this instanceof ExtensionAware) {
            return ((ExtensionAware)this).getExtensions();
        }
        return null;
    }
}
