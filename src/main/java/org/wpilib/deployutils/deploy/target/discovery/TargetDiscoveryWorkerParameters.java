package org.wpilib.deployutils.deploy.target.discovery;

import org.gradle.api.provider.Property;
import org.gradle.workers.WorkParameters;
import org.wpilib.deployutils.deploy.StorageService;

public interface TargetDiscoveryWorkerParameters extends WorkParameters {
    Property<StorageService> getStorageService();
    Property<Integer> getIndex();
}
