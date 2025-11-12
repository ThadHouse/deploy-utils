package org.wpilib.deployutils.deploy.artifact;

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;
import org.gradle.nativeplatform.SharedLibraryBinarySpec;
import org.wpilib.deployutils.deploy.cache.CacheMethod;
import org.wpilib.deployutils.deploy.context.DeployContext;
import org.wpilib.deployutils.deploy.target.RemoteTarget;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

public class NativeLibraryArtifact extends AbstractArtifact implements CacheableArtifact {

    private final Property<CacheMethod> cacheMethod;

    @Inject
    public NativeLibraryArtifact(String name, RemoteTarget target) {
        super(name, target);
        filename = target.getProject().getObjects().property(String.class);
        binarySpec = target.getProject().getObjects().property(SharedLibraryBinarySpec.class);
        cacheMethod = target.getProject().getObjects().property(CacheMethod.class);

        linkTaskProvider = target.getProject().getProviders().provider(() -> {
            return binarySpec.get().getTasks().getLink();
        });

        dependsOn(linkTaskProvider);
    }

    private final Provider<Task> linkTaskProvider;

    @Override
    public Property<CacheMethod> getCacheMethod() {
        return cacheMethod;
    }

    private final Property<String> filename;

    public Property<String> getFilename() {
        return filename;
    }

    private Property<SharedLibraryBinarySpec> binarySpec;

    public Property<SharedLibraryBinarySpec> getBinary() {
        return binarySpec;
    }

    private final PatternFilterable libraryFilter = new PatternSet();

    public PatternFilterable getLibraryFilter() {
        return libraryFilter;
    }

    protected File getDeployedFile() {
        return binarySpec.get().getSharedLibraryFile();
    }

    @Override
    public void deploy(DeployContext context) {
        CacheMethod cm = cacheMethod.getOrElse(null);

        File libFile = getDeployedFile();
        context.put(libFile, getFilename().getOrElse(libFile.getName()), cm);
    }
}
