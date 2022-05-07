import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.dockerCommand
import jetbrains.buildServer.configs.kotlin.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.04"

project {

    buildType(Build)

    features {
        dockerRegistry {
            id = "PROJECT_EXT_5"
            name = "Docker Registry"
            url = "https://docker.io"
            userName = "divyasadhankar"
            password = "credentialsJSON:2eab850a-d625-4484-aa74-fb5c8409bfb6"
        }
    }
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            name = "build application"
            tasks = "clean build"
        }
        dockerCommand {
            name = "build image"
            commandType = build {
                source = file {
                    path = "dockerfile"
                }
                namesAndTags = """
                    divyasadhankar/mydemorepo:myapp-9.0-%build.number%
                    divyasadhankar/mydemorepo:latest
                """.trimIndent()
                commandArgs = "--pull"
            }
        }
        dockerCommand {
            commandType = push {
                namesAndTags = """
                    divyasadhankar/mydemorepo:myapp-9.0-%build.number%
                    divyasadhankar/mydemorepo:latest
                """.trimIndent()
            }
        }
    }

    triggers {
        vcs {
        }
    }
})
