def app_name = "Product"
def img = "Product-Service"
def gitrepo = 'https://github.com/julioquintana/product.git'
def build_id = "${env.BUILD_ID}"
def deploy_list = ["master"]


switch(env.BRANCH_NAME) {
  case 'master':
    nspace = 'txd'
    k_cloud = 'k8scl01'
    k_folder = 'prod'
    image_id= "${build_id}"
    secret_name = 'sm-prod-database-secrets'
  break
  default:
    nspace = 'txd'
    k_cloud = 'k8sqacl01'
    k_folder = 'dev'
    image_id = "dev-${build_id}"
    secret_name = 'sm-dev-database-secrets'
  break
}

def image = "${img}:${image_id}"


    node('shippingcalculator') {
    gitlabCommitStatus(name: 'jenkins') {
      stage('Get project dependencies') {
        git branch: "${env.BRANCH_NAME}", credentialsId: 'cc_oms', url: "${gitrepo}"
        checkout scm
        container('maven') {
          sh "mvn install -DskipTests -Dcobertura.skip " +
            "-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true " +
            "-Dmaven.wagon.http.ssl.ignore.validity.dates=true " + "-e "
        }
      }

      stage('Unit tests') {
        git branch: "${env.BRANCH_NAME}", credentialsId: 'cc_oms', url: "${gitrepo}"
        checkout scm
        container('maven') {
          sh "mvn test -Djacoco.skip " +
            "-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true " +
            "-Dmaven.wagon.http.ssl.ignore.validity.dates=true " + "-e "
        }
      }

      stage('Coverage Analysis') {
        git branch: "${env.BRANCH_NAME}", credentialsId: 'cc_oms', url: "${gitrepo}"
        checkout scm
        container('maven') {
          sh "mvn test " +
            "-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true " +
            "-Dmaven.wagon.http.ssl.ignore.validity.dates=true " + "-e "
        }
      }
      stage('Integration tests') {
        git branch: "${env.BRANCH_NAME}", credentialsId: 'cc_oms', url: "${gitrepo}"
        checkout scm
        container('maven') {
          withCredentials([file(credentialsId: "${secret_name}", variable: 'DATABASE')]) {
            sh """
                set +x
                export `cat $DATABASE | grep DATABASE_URL`
                export `cat $DATABASE | grep DATABASE_USER`
                export `cat $DATABASE | grep DATABASE_PASSWD`
             """ +
             "mvn failsafe:integration-test " +
              "-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true " +
              "-Dmaven.wagon.http.ssl.ignore.validity.dates=true " + "-e "
          }
        }
      }
      stage('Build project artifact') {
        git branch: "${env.BRANCH_NAME}", credentialsId: 'cc_oms', url: "${gitrepo}"
        checkout scm
        container('maven') {
          sh "mvn package -DskipTests -Dcobertura.skip" +
            "-Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true " +
            "-Dmaven.wagon.http.ssl.ignore.validity.dates=true " + "-e "
        }
      }

      stage('Build and push Docker image') {
        if(deploy_list.contains(env.BRANCH_NAME)){
          withCredentials([usernamePassword(credentialsId: 'nexus-registry', passwordVariable: 'DOCKER_PASSWD', usernameVariable: 'DOCKER_USER')]) {
            container('docker') {
              sh "docker build --no-cache -t ${image} -f docker/Dockerfile ."
              sh "echo ${DOCKER_PASSWD} | docker login -u ${DOCKER_USER} --password-stdin cencoreg:5000"
              sh "docker push ${image} && docker rmi ${image}"
            }
          }
        }
      }

      stage('Deploy to Kubernetes') {
        if(deploy_list.contains(env.BRANCH_NAME)){
          container('kubectl') {
            sh "sed -i -r 's/(${img_sed}:)[0-9a-z]*/\\1${image_id}/' k8s/${k_folder}/deployment.yaml"
            // sh "sed -ie 's/AWS_ACCESS_KEY_ID_VALUE/${AWS_ACCESS_KEY_ID}/g' k8s/${k_folder}/deployment.yaml"
            // sh "sed -ie 's/AWS_SECRET_ACCESS_KEY_VALUE/${AWS_SECRET_ACCESS_KEY}/g' k8s/${k_folder}/deployment.yaml"
            sh "kubectl -n ${nspace} apply -f k8s/${k_folder}"
          }
        }
      }
    }
  }
