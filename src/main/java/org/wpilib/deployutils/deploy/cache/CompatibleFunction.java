package org.wpilib.deployutils.deploy.cache;

import org.wpilib.deployutils.deploy.context.DeployContext;

@FunctionalInterface
public interface CompatibleFunction {
  boolean check(DeployContext ctx);
}
