package edu.wpi.first.deployutils.deploy.cache;

import java.io.InputStream;
import java.util.concurrent.Callable;

import edu.wpi.first.deployutils.deploy.context.DeployContext;

@FunctionalInterface
public interface CacheCheckerFunction {
  boolean check(DeployContext ctx, String filename, Callable<InputStream> localFile);
}
