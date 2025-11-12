package org.wpilib.deployutils.deploy.artifact;

import org.gradle.api.provider.Property;
import org.wpilib.deployutils.deploy.cache.CacheMethod;

public interface CacheableArtifact extends Artifact {
    Property<CacheMethod> getCacheMethod();
}
