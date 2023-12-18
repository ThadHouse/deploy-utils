package edu.wpi.first.deployutils.deploy.cache;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import edu.wpi.first.deployutils.deploy.context.DeployContext;

public class DefaultCacheMethod extends AbstractCacheMethod {
    private CacheCheckerFunction needsUpdate = (ctx, fn, lf) -> true;
    private CompatibleFunction compatible = ctx -> true;

    public DefaultCacheMethod(String name) {
        super(name);

    }

    public CacheCheckerFunction getNeedsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(CacheCheckerFunction needsUpdate) {
        this.needsUpdate = needsUpdate;
    }

    public CompatibleFunction getCompatible() {
        return compatible;
    }

    public void setCompatible(CompatibleFunction compatible) {
        this.compatible = compatible;
    }

    @Override
    public boolean compatible(DeployContext context) {
        return compatible.check(context);
    }

    @Override
    public Map<Boolean, List<Entry<String, Callable<InputStream>>>> needsUpdate(DeployContext context,
            Map<String, Callable<InputStream>> files) {
        return files.entrySet().stream().collect(
                Collectors.partitioningBy(entry -> needsUpdate.check(context, entry.getKey(), entry.getValue())));
    }
}
