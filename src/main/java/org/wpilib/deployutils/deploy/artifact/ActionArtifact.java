package org.wpilib.deployutils.deploy.artifact;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.wpilib.deployutils.deploy.context.DeployContext;
import org.wpilib.deployutils.deploy.target.RemoteTarget;

public class ActionArtifact extends AbstractArtifact {
    private Action<DeployContext> deployAction;

    @Inject
    public ActionArtifact(String name, RemoteTarget target) {
        super(name, target);
    }

    @Override
    public void deploy(DeployContext context) {
        deployAction.execute(context);
    }

    public Action<DeployContext> getDeployAction() {
        return deployAction;
    }

    public void setDeployAction(Action<DeployContext> deployAction) {
        this.deployAction = deployAction;
    }

}
