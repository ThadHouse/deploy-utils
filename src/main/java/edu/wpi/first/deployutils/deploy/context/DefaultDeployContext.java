package edu.wpi.first.deployutils.deploy.context;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;

import edu.wpi.first.deployutils.PathUtils;
import edu.wpi.first.deployutils.deploy.CommandDeployResult;
import edu.wpi.first.deployutils.deploy.cache.CacheMethod;
import edu.wpi.first.deployutils.deploy.sessions.SessionController;
import edu.wpi.first.deployutils.deploy.target.location.DeployLocation;
import edu.wpi.first.deployutils.log.ETLogger;

public class DefaultDeployContext implements DeployContext {

    private final SessionController session;
    private final ETLogger logger;
    private final DeployLocation deployLocation;
    private final String workingDir;

    @Inject
    public DefaultDeployContext(SessionController session, ETLogger logger, DeployLocation deployLocation,
            String workingDir) {
        this.session = session;
        this.logger = logger;
        this.deployLocation = deployLocation;
        this.workingDir = workingDir;
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public DeployLocation getDeployLocation() {
        return deployLocation;
    }

    @Override
    public ETLogger getLogger() {
        return logger;
    }

    @Override
    public SessionController getController() {
        return session;
    }

    @Override
    public CommandDeployResult execute(String command, Optional<InputStream> standardInput) {
        session.execute("mkdir -p " + workingDir, Optional.empty());

        logger.log("  -C-> " + command + " @ " + workingDir);
        CommandDeployResult result = session.execute(String.join("\n", "cd " + workingDir, command), standardInput);
        if (result != null) {
            if (result.getResult() != null && result.getResult().length() > 0) {
                logger.log("    -[" + result.getExitCode() + "]-> " + result.getResult());
            } else if (result.getExitCode() != 0) {
                logger.log("    -[" + result.getExitCode() + "]");
            }
        }
        return result;
    }

    @Override
    public void put(Map<String, Callable<InputStream>> files, CacheMethod cache) {
        session.execute("mkdir -p " + workingDir, Optional.empty());

        Collection<Entry<String, Callable<InputStream>>> cacheHits;
        Collection<Entry<String, Callable<InputStream>>> cacheMisses;

        if (cache != null && cache.compatible(this)) {
            var updateRequired = cache.needsUpdate(this, files);
            cacheHits = updateRequired.get(false);
            cacheMisses = updateRequired.get(true);
        } else {
            cacheHits = List.of();
            cacheMisses = files.entrySet();
        }

        if (!cacheMisses.isEmpty()) {
            Map<String, Callable<InputStream>> entries = cacheMisses.stream().map(x -> {
                logger.log("  -F-> " + x.getKey() + " @ " + workingDir);
                return x;
            }).collect(Collectors.toMap(x -> PathUtils.combine(workingDir, x.getKey()), x -> x.getValue()));
            session.put(entries);
        }

        if (!cacheHits.isEmpty()) {
            logger.log("  " + cacheHits.size() + " file(s) are up-to-date and were not deployed");
        }
    }

    @Override
    public String friendlyString() {
        return session.friendlyString();
    }

    @Override
    public DeployContext subContext(String workingDir) {
        return new DefaultDeployContext(session, logger.push(), deployLocation, PathUtils.combine(this.workingDir, workingDir));
    }
}
