pipeline {
  agent any

  environment {
    VERSION = "${env.BUILD_ID}"
    PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:${env.PATH}"
  }

  tools {
    maven "Maven"
  }

  stages {
    stage('Build') {
      steps {
        sh 'docker --version'
      }
    }

    stage('Maven Build') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }

    stage('Run Tests') {
      steps {
        sh 'mvn test -DDB_USERNAME=root -DDB_PASSWORD=root'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970:sonar -Dsonar.host.url=http://host.docker.internal:9000/ -Dsonar.token=squ_903800015bff43aa5d9b2f8e64d18b21057bfa8f -DDB_USERNAME=root -DDB_PASSWORD=root'
      }
    }

    stage('Check code coverage') {
      steps {
        script {
            def token = "squ_903800015bff43aa5d9b2f8e64d18b21057bfa8f"
            def sonarQubeUrl = "http://host.docker.internal:9000/api"
            def componentKey = "com.codecode:restaurantlisting"
            def coverageThreshold = 80.0

            def response = sh (
                script: "curl -s -u ${token}: '${sonarQubeUrl}/measures/component?component=${componentKey}&metricKeys=coverage'",
                returnStdout: true
            ).trim()

            // Parse the JSON payload
            def json = readJSON text: response

            // Extract the coverage value safely from the response structure
            def coverageObj = json.component.measures.find { it.metric == 'coverage' }
            def coverageValue = coverageObj ? coverageObj.value : "0"

            echo "Coverage: ${coverageValue}"

            if (coverageValue.toDouble() < coverageThreshold) {
                error "Coverage is below the threshold of ${coverageThreshold}%. Aborting the pipeline."
            }
        }
      }
    }

    stage('Login to Docker Hub') {
      steps {
        // 'docker-hub-credentials' matches the ID you created in Jenkins
        withCredentials([usernamePassword(
            credentialsId: 'DOCKER_HUB_CREDENTIAL',
            usernameVariable: 'USER',
            passwordVariable: 'PASS'
        )]) {
            // Log in securely using stdin to prevent credentials leaking in process lists
            sh 'echo "$PASS" | docker login -u "$USER" --password-stdin'
            }
      }
    }

    stage('Docker Build and Push') {
      steps {
          sh 'docker build -t codecode123/restaurantlisting-arm-cicd:${VERSION} .'
          sh 'docker push codecode123/restaurantlisting-arm-cicd:${VERSION}'
      }
    }

    stage('Clean Workspace') {
      steps {
        deleteDir()
      }
    }

    stage('Update Image Tag in GitOps') {
      steps {
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[ credentialsId: 'git-ssh', url: 'git@github.com:CodeCode-123/udemy-master-spring-boot-microservice-angular-k8s-cicd-aws-deployment.git']])
        script {
          sh '''
            sed -i "s/image:.*/image: codecode123\\/restaurantlisting-arm-cicd:${VERSION}/" restaurantlisting-manifest.yaml
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