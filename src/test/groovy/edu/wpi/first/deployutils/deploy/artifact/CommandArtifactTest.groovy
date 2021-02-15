package edu.wpi.first.deployutils.deploy.artifact

import edu.wpi.first.deployutils.deploy.CommandDeployResult
import edu.wpi.first.deployutils.deploy.context.DeployContext

class CommandArtifactTest extends AbstractArtifactTestSpec {

    CommandArtifact artifact

    def setup() {
        artifact = new CommandArtifact(name, project)
    }

    def "deploy"() {
        def result = Mock(CommandDeployResult)
        def ctx = Mock(DeployContext) {
            execute(_) >> result
        }
        def cmd = "Hello World"
        artifact.command = cmd

        when:
        artifact.deploy(ctx)
        then:
        artifact.result == result
    }
}
