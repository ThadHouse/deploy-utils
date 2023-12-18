package edu.wpi.first.deployutils.deploy.cache;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import edu.wpi.first.deployutils.deploy.context.DeployContext;
import edu.wpi.first.deployutils.log.ETLogger;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;

public class Md5FileCacheMethod extends Md5BackedCacheMethod {
    private Logger log = Logging.getLogger(Md5SumCacheMethod.class);
    private int csI = 0;
    private Gson gson = new Gson();
    private Type mapType = new TypeToken<Map<String, String>>(){}.getType();

    @Inject
    public Md5FileCacheMethod(String name) {
        super(name);
    }

    @Override
    public boolean compatible(DeployContext context) {
        return true;
    }

    private Map<String, String> getRemoteCache(DeployContext ctx) {
        String remote_cache = ctx.execute("cat cache.md5 2> /dev/null || echo '{}'").getResult();
        return gson.fromJson(remote_cache, mapType);
    }

    @Override
    public Map<Boolean, List<Entry<String, Callable<InputStream>>>> needsUpdate(DeployContext context,
            Map<String, Callable<InputStream>> files) {
        ETLogger logger = context.getLogger();
        if (logger != null) {
            logger.silent(true);
        }
        int cs = csI++;
        log.debug("Comparing File Checksum " + cs + "...");

        Map<String, String> remote_md5 = getRemoteCache(context);

        if (log.isDebugEnabled()) {
            log.debug("Remote Cache " + cs + ":");
            log.debug(gson.toJson(remote_md5, mapType));
        }

        Map<String, String> local_md5 = localChecksumsMap(files);

        if (log.isDebugEnabled()) {
            log.debug("Local JSON Cache " + cs + ":");
            log.debug(gson.toJson(local_md5, mapType));
        }

        Map<Boolean, List<Entry<String, Callable<InputStream>>>> checkResults = files.entrySet().stream().collect(Collectors.partitioningBy(entry -> {
            String md5 = remote_md5.get(entry.getKey());
            return md5 == null || !md5.equals(local_md5.get(entry.getKey()));
        }));

        List<Entry<String, Callable<InputStream>>> needsUpdate = checkResults.get(true);

        // Set<String> needs_update = files.entrySet().stream().filter(name -> {
        //     String md5 = remote_md5.get(name);
        //     return md5 == null || !md5.equals(local_md5.get(name));
        // }).collect(Collectors.toSet());

        if (!needsUpdate.isEmpty()) {
            context.execute("echo '" + gson.toJson(local_md5, mapType) + "' > cache.md5");
        }

        if (logger != null) {
            logger.silent(false);
        }

        return checkResults;
    }
}
