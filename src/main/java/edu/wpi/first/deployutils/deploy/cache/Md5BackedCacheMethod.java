package edu.wpi.first.deployutils.deploy.cache;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.codehaus.groovy.runtime.EncodingGroovyMethods;

public abstract class Md5BackedCacheMethod extends AbstractCacheMethod {

    @Inject
    public Md5BackedCacheMethod(String name) {
        super(name);
    }

        private String digestInput(MessageDigest digest, byte[] cache, Callable<InputStream> streamSupplier) {
        try (InputStream stream = streamSupplier.call()) {
            digest.reset();
            int read;
            while ((read = stream.read(cache, 0, cache.length)) >= 0) {
                digest.update(cache, 0, read);
            }
            return EncodingGroovyMethods.encodeHex(digest.digest()).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> localChecksumsMap(Map<String, Callable<InputStream>> files) {
        MessageDigest md;
        byte[] cache = new byte[8192];
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            throw new RuntimeException(e1);
        }
        return files.entrySet().stream().collect(Collectors.toMap(entry -> {
            return entry.getKey();
        }, entry -> digestInput(md, cache, entry.getValue())));
    }
}
