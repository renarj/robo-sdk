node {
    stage ('checkout') {
        git credentialsId: 'github-ssh', url: 'git@github.com:renarj/robo-sdk.git'
    }

    stage ('build') {
        withMaven(maven: 'M3', jdk: 'JDK10') {
            sh "mvn clean install"
        }
    }

    stage ('release') {
        withMaven(maven: 'M3', jdk: 'JDK10') {
            sh "mvn clean deploy -Dmaven.test.skip=true"
        }
    }

    stage 'archive'
    step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
}
