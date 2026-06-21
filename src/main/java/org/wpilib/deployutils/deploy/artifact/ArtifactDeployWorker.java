package org.wpilib.deployutils.deploy.artifact;

import org.gradle.workers.WorkAction;
import org.wpilib.deployutils.deploy.StorageService.DeployStorage;
import org.wpilib.deployutils.deploy.context.DeployContext;

public abstract class ArtifactDeployWorker implements WorkAction<ArtifactDeployParameters> {

    @Override
    public void execute() {
        Integer index = getParameters().getIndex().get();
        DeployStorage storage = getParameters().getStorageService().get().getDeployStorage(index);

        DeployContext rootContext = storage.context;
        Artifact artifact = storage.artifact;
        run(rootContext, artifact);
    }

    public void run(DeployContext rootContext, Artifact artifact) {
        DeployContext context = rootContext.subContext(artifact.getDirectory().get());
        boolean enabled = artifact.isEnabled(context);
        if (enabled) {
            var startTime = System.nanoTime();
            ArtifactRunner.runDeploy(artifact, context);
            var endTime = System.nanoTime();
            var duration = (endTime - startTime) / 1_000_000;
            context.getLogger().log("Artifact deployed in " + duration + " ms");
        } else {
            context.getLogger().log("Artifact skipped");
        }
    }
}
