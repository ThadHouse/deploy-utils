package org.wpilib.deployutils.deploy.artifact;

import javax.inject.Inject;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.jvm.tasks.Jar;
import org.wpilib.deployutils.deploy.target.RemoteTarget;

public class JavaClasspathArtifact extends FileTreeArtifact {

    @Inject
    public JavaClasspathArtifact(String name, RemoteTarget target) {
        super(name, target);

        configurationProperty = target.getProject().getObjects().property(Configuration.class);
        jarProperty = target.getProject().getObjects().property(Jar.class);

        dependsOn(configurationProperty);
        dependsOn(jarProperty);

        ProjectLayout projectLayout = target.getProject().getLayout();

        getPreWorkerThread().add(cfg -> {
            if (!configurationProperty.isPresent() || !jarProperty.isPresent()) {
                return;
            }
            var fileTree = configurationProperty.get().resolve();
            var files = projectLayout.files(fileTree).plus(jarProperty.get().getOutputs().getFiles());
            getFiles().set(files.getAsFileTree());
        });
    }

    private final Property<Configuration> configurationProperty;

    public Provider<Configuration> getConfigurationProvider() {
        return configurationProperty;
    }

    private final Property<Jar> jarProperty;

    public Provider<Jar> getJarProvider() {
        return jarProperty;
    }

    public void setJar(Jar jar) {
        jarProperty.set(jar);
    }

    public void setConfiguration(Configuration configuration) {
        configurationProperty.set(configuration);
    }
}
