node {
    git url: 'git@github.com:renarj/robo-sdk.git'
    def mvnHome : tool 'M3'

    stage 'checkout'
    checkout scm

    stage 'build'
    sh "${mvnHome}/bin/mvn -B clean install"

    stage 'release'
    sh "${mvnHome}/bin/mvn -B clean deploy"

    step([$class: 'ArtifactsArchiver', artifacts: '**/target/*.jar', fingerprint: true])
}