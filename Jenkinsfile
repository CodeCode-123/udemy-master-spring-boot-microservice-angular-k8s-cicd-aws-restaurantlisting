pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIALS')
    VERSION = "${env.BUILD_ID}"
  }

  tools {
    maven "Maven"
  }

  stages {
    stage('Maven Build') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Run Tests') {
      steps {
        sh 'mvn test'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970:sonar -Dsonar.host.url=http://localhost:9000/ -Dsonar.token=squ_903800015bff43aa5d9b2f8e64d18b21057bfa8f'
      }
    }

    stage('Check code coverage') {
      steps {
        script {
            def token = "squ_903800015bff43aa5d9b2f8e64d18b21057bfa8f"
            def sonarQubeUrl = "http://localhost:9000/api"
            def componentKey = "com.codecode:order"
            def coverageThreshold = 80.0

            def response = sh (
                 script: "curl -H 'Authorization: Bearer ${token}' '${sonarQubeUrl}/measures/component?component=${componentKey}&metricKeys=coverage'",
                 returnStdout: true
            ).trim()

            def coverage = sh (
                script: "echo '${response}' | jq -r '.component.measures[0].value'",
                returnStdout: true
            ).trim().toDouble()

            echo "Coverage: ${coverage}"

            if (coverage < coverageThreshold) {
                error "Coverage is below the threshold of ${coverageThreshold}%. Aborting the pipeline."
            }
        }
      }
    }

    stage('Docker Build and Push') {
      steps {
          sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
          sh 'docker build -t codecode123/order-service-arm:${VERSION} .'
          sh 'docker push codecode123/order-service-arm:${VERSION}'
      }
    }

    stage('Clean Workspace') {
      steps {
        deleteDir()
      }
    }

    stage('Update Image Tag in GitOps') {
      steps {
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[ credentialsId: 'git-ssh', url: 'git@github.com:CodeCode-123/udemy-master-spring-boot-microservice-angular-k8s-cicd-aws.git'])
        script {
          sh '''
            sed -i "s/image:.*/image: codecode123\\/order-service-arm:${VERSION}/" aws/order-manifest.yaml
          '''
          sh 'git checkout main'
          sh 'git add .'
          sh 'git commit -m "Update image tag"'
          sshagent(['git-ssh']) {
            sh('git push')
          }
        }
      }
    }
  }
}