package org.wpilib.deployutils.deploy.target.location;

import org.gradle.api.Named;
import org.wpilib.deployutils.deploy.target.RemoteTarget;
import org.wpilib.deployutils.deploy.target.discovery.action.DiscoveryAction;

public interface DeployLocation extends Named {
    DiscoveryAction createAction();

    RemoteTarget getTarget();

    String friendlyString();
}
