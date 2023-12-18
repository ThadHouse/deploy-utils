package edu.wpi.first.deployutils.deploy.cache;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.gradle.api.Named;

import edu.wpi.first.deployutils.deploy.context.DeployContext;

public interface CacheMethod extends Named {
    // Returns false if something can't be found (e.g. md5sum). In this case, cache
    // checking is skipped.
    boolean compatible(DeployContext context);

    // Map is remote file path for key, and function to get stream for local file
    // for value. return map true if update is needed, false if not
    Map<Boolean, List<Entry<String, Callable<InputStream>>>> needsUpdate(DeployContext context, Map<String, Callable<InputStream>> files);
}
