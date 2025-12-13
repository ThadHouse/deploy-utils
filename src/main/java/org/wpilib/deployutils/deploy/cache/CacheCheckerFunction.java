package org.wpilib.deployutils.deploy.cache;

import java.io.File;

import org.wpilib.deployutils.deploy.context.DeployContext;

@FunctionalInterface
public interface CacheCheckerFunction {
  boolean check(DeployContext ctx, String filename, File localFile);
}
