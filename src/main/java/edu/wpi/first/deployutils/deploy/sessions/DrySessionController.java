package edu.wpi.first.deployutils.deploy.sessions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import edu.wpi.first.deployutils.deploy.CommandDeployResult;

public class DrySessionController extends AbstractSessionController implements IPSessionController {

    @Inject
    public DrySessionController() {
        super(1, null);

        getLogger().info("DrySessionController opening");
    }

    @Override
    public CommandDeployResult execute(String command, Optional<InputStream> standardInput) {
        return new CommandDeployResult(command, "", 0);
    }

    @Override
    public void put(Map<String, Callable<InputStream>> files) { }

    @Override
    public String friendlyString() {
        return "DrySessionController";
    }

    @Override
    public void close() throws IOException {
        getLogger().info("DrySessionController closing");
    }

    @Override
    public String getHost() {
        return "dryhost";
    }

    @Override
    public int getPort() {
        return 22;
    }
}
