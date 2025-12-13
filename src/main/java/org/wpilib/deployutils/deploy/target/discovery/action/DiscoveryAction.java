package org.wpilib.deployutils.deploy.target.discovery.action;

import java.util.concurrent.Callable;

import org.wpilib.deployutils.deploy.context.DeployContext;
import org.wpilib.deployutils.deploy.target.discovery.DiscoveryFailedException;
import org.wpilib.deployutils.deploy.target.discovery.DiscoveryState;
import org.wpilib.deployutils.deploy.target.location.DeployLocation;

public interface DiscoveryAction extends Callable<DeployContext> {
    DeployContext discover();

    DiscoveryFailedException getException();

    DiscoveryState getState();

    DeployLocation getDeployLocation();
}
