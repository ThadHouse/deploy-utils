package org.wpilib.deployutils.deploy.artifact;

import org.gradle.api.file.FileCollection;
import org.gradle.api.provider.Property;
import org.wpilib.deployutils.deploy.cache.CacheMethod;
import org.wpilib.deployutils.deploy.context.DeployContext;
import org.wpilib.deployutils.deploy.target.RemoteTarget;
import org.wpilib.deployutils.log.ETLogger;

import javax.inject.Inject;

public class FileCollectionArtifact extends AbstractArtifact implements CacheableArtifact {

    private final Property<CacheMethod> cacheMethod;

    @Inject
    public FileCollectionArtifact(String name, RemoteTarget target) {
        super(name, target);
        files = target.getProject().getObjects().property(FileCollection.class);
        cacheMethod = target.getProject().getObjects().property(CacheMethod.class);
    }

    private final Property<FileCollection> files;

    public Property<FileCollection> getFiles() {
        return files;
    }

    @Override
    public Property<CacheMethod> getCacheMethod() {
        return cacheMethod;
    }

    @Override
    public void deploy(DeployContext context) {
        if (files.isPresent())
            context.put(files.get().getFiles(), cacheMethod.getOrElse(null));
        else {
            ETLogger logger = context.getLogger();
            if (logger != null) {
                logger.log("No file(s) provided for " + toString());
            }
        }
    }
}
