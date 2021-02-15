package edu.wpi.first.deployutils.deploy.target.location

import edu.wpi.first.deployutils.deploy.target.RemoteTarget
import spock.lang.Specification
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Subject

class DeployLocationSetTest extends Specification {

    def project = ProjectBuilder.builder().build()
    def target = Mock(RemoteTarget)

    @Subject
    def locSet = project.objects.newInstance(DeployLocationSet, project, target)

    def "starts empty"() {
        expect:
        locSet.empty
    }

    def "add ssh location"() {
        when:
        locSet.ssh { it.user = "myuser" }
        then:
        locSet.size() == 1
        locSet.first() instanceof SshDeployLocation
        locSet.first().user == "myuser"
    }

    def "add dry"() {
        target.isDry() >> true

        when:
        locSet.ssh { it.user = "myuser" }
        then:
        locSet.size() == 1
        locSet.first() instanceof DryDeployLocation
    }

}
