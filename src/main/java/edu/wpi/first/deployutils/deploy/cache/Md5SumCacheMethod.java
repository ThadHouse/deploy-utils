package edu.wpi.first.deployutils.deploy.cache;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import edu.wpi.first.deployutils.deploy.context.DeployContext;
import edu.wpi.first.deployutils.log.ETLogger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class Md5SumCacheMethod extends Md5BackedCacheMethod {
    private Logger log = Logging.getLogger(Md5SumCacheMethod.class);
    private int csI = 0;

    @Inject
    public Md5SumCacheMethod(String name) {
        super(name);
    }

    @Override
    public boolean compatible(DeployContext context) {
        ETLogger logger = context.getLogger();
        if (logger != null) {
            logger.silent(true);
        }
        String sum = context.execute("echo test | md5sum 2> /dev/null").getResult();
        if (logger != null) {
            logger.silent(false);
        }

        return !sum.isEmpty() && sum.split(" ")[0].equalsIgnoreCase("d8e8fca2dc0f896fd7cb4cb0031ba249");
    }

    private String localChecksumsText(Map<String, Callable<InputStream>> files) {
        Map<String, String> checksums = localChecksumsMap(files);
        Optional<String> sums = checksums.entrySet().stream().map(entry -> {
            return entry.getValue().trim() + " *" + entry.getKey();
        }).reduce((a, b) -> a + "\n" + b);
        return sums.orElse(null);
    }

    @Override
    public Map<Boolean, List<Entry<String, Callable<InputStream>>>> needsUpdate(DeployContext context, Map<String, Callable<InputStream>> files) {
        ETLogger logger = context.getLogger();
        if (logger != null) {
            logger.silent(true);
        }

        int cs = csI++;

        log.debug("Comparing Checksums " + cs + "...");
        String localChecksums = localChecksumsText(files);

        if (log.isDebugEnabled()) {
            log.debug("Local Checksums " + cs + ":");
            log.debug(localChecksums);
        }

        String result;
        try (InputStream is = new ByteArrayInputStream(localChecksums.getBytes(StandardCharsets.UTF_8))) {
            result = context.execute("md5sum -c 2> /dev/null", Optional.of(is)).getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (log.isDebugEnabled()) {
            log.debug("Remote Checksums " + cs + ":");
            log.debug(result);
        }

        List<String> upToDate = Arrays.stream(result.split("\n"))
            .map(x -> x.split(":"))
            .filter(ls -> ls[ls.length - 1].trim().equalsIgnoreCase("ok"))
            .map(ls -> ls[0])
            .collect(Collectors.toList());

        if (logger != null) {
            logger.silent(false);
        }

        return files.entrySet().stream().collect(Collectors.partitioningBy(entry -> !upToDate.contains(entry.getKey())));
    }
}
