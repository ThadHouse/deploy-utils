package org.wpilib.deployutils.deploy.artifact;

import org.gradle.api.provider.Property;
import org.gradle.workers.WorkParameters;
import org.wpilib.deployutils.deploy.StorageService;

public interface ArtifactDeployParameters extends WorkParameters {
    Property<StorageService> getStorageService();
    Property<Integer> getIndex();
}
