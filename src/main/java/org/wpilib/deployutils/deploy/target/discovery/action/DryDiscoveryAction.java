package org.wpilib.deployutils.deploy.target.discovery.action;

import org.wpilib.deployutils.deploy.context.DefaultDeployContext;
import org.wpilib.deployutils.deploy.context.DeployContext;
import org.wpilib.deployutils.deploy.sessions.DrySessionController;
import org.wpilib.deployutils.deploy.target.discovery.DiscoveryState;
import org.wpilib.deployutils.deploy.target.location.DeployLocation;
import org.wpilib.deployutils.log.ETLogger;
import org.wpilib.deployutils.log.ETLoggerFactory;

public class DryDiscoveryAction extends AbstractDiscoveryAction {

    private ETLogger log;

    public DryDiscoveryAction(DeployLocation loc) {
        super(loc);
        this.log = ETLoggerFactory.INSTANCE.create(toString());
    }

    @Override
    public DeployContext discover() {
        DrySessionController controller = new DrySessionController();
        return new DefaultDeployContext(controller, log, getDeployLocation(), getDeployLocation().getTarget().getDirectory());
    }

    @Override
    public DiscoveryState getState() {
        return DiscoveryState.CONNECTED;
    }
}
