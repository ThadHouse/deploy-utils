package edu.wpi.first.deployutils.deploy.cache

import edu.wpi.first.deployutils.deploy.context.DeployContext

class DefaultCacheMethodTest extends AbstractCacheMethodTestSpec {

    DefaultCacheMethod cacheMethod

    def setup() {
        cacheMethod = new DefaultCacheMethod(name)
    }

    def "compatible by default"() {
        expect:
        cacheMethod.compatible(null)
    }

    def "cache miss by default"() {
        def map = [
                "a": Mock(File),
                "b": Mock(File)
        ]
        when:
        def r = cacheMethod.needsUpdate(null, map)
        then:
        r == ["a", "b"] as Set<String>
    }

    def "configure compatible"() {
        cacheMethod.compatible = { false }

        expect:
        !cacheMethod.compatible(null)
    }

    def "configure cache"() {
        cacheMethod.needsUpdate = { DeployContext ctx, String filename, File localfile ->
            filename == "a"
        }

        def map = [
                "a": Mock(File),
                "b": Mock(File)
        ]

        when:
        def r = cacheMethod.needsUpdate(null, map)
        then:
        r == ["a"] as Set<String>
    }
}
