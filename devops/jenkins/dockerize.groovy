def label = "ArsenalDev-${UUID.randomUUID().toString()}"
def isIpcRunningEnv = true
def isEpcRunningEnv = false
def mattermostDevIncomingUrl='http://10.220.185.200/hooks/qtqhqy74rpfatehfnccgiet9go'

String getBranchName(branch) {
    branchTemp=sh returnStdout:true ,script:"""echo "$branch" |sed -E "s#origin/##g" """
    if(branchTemp){
        branchTemp=branchTemp.trim()
    }
    return branchTemp
}


podTemplate(label: label, serviceAccount: 'tiller', namespace: 'oss',
    containers: [
        containerTemplate(name: 'build-tools', image: 'ktis-bastion01.container.ipc.kt.com:5000/alpine/build-tools:latest', ttyEnabled: true, command: 'cat', privileged: true, alwaysPullImage: true),
        containerTemplate(name: 'node', image: 'ktis-bastion01.container.ipc.kt.com:5000/admin/node:latest', ttyEnabled: true, command: 'cat', privileged: true, alwaysPullImage: true),
        containerTemplate(name: 'jnlp', image: 'ktis-bastion01.container.ipc.kt.com:5000/jenkins/jnlp-slave:alpine', args: '${computer.jnlpmac} ${computer.name}')
    ],
    volumes: [
        hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock'),
        nfsVolume(mountPath: '/home/jenkins', serverAddress: '10.217.67.145', serverPath: '/data/nfs/devops/jenkins-slave-pv', readOnly: false)
        ]
    ) {
    node(label) {
        if ( isIpcRunningEnv ) {
            library 'pipeline-lib'
        }
        try {
            // freshStart 
            def freshStart = params.freshStart

            if ( freshStart ) {
                container('build-tools'){
                    // remove previous working dir
                    print "freshStart... clean working directory ${env.JOB_NAME}"
                    sh 'ls -A1|xargs rm -rf' /* clean up our workspace */
                }
            }

            
            def commitId

            def branchTemp
            //branch Name Parsing
            branchTemp = params.branchName
            branch=getBranchName(branchTemp)


            stage('Get Source') {
                git url: "http://git.cz-dev.container.kt.co.kr/oss/poss-web.git",
                    credentialsId: 'gitlab-kt-credential',
                    branch: "${branch}"
                    commitId = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            }
            def props = readProperties  file:'devops/jenkins/dockerize.properties'
            def tag = commitId
            def dockerRegistry = props['dockerRegistry']
            def image = props['image']
            def selector = props['selector']
            def namespace = props['namespace']
            def appname = props['appname']
            
            def yarnEnable = false  
            yarnEnable = params.yarnEnable
            
            
            stage('npm build') {
                container('node') {
                    if( yarnEnable ){
                        sh "yarn config set registry http://10.217.59.89/nexus3/repository/npm-group/"
                        sh "yarn install --sass-binary-site=http://10.217.59.89/nexus3/repository/arsenal-raw-hosted/npm-libraries/node-sass"
                        env.NODE_ENV = "production" 
                        sh "yarn build"
                    } else {
                        sh "npm config set registry http://10.217.59.89/nexus3/repository/npm-group/"
                        sh "npm install --sass-binary-site=http://10.217.59.89/nexus3/repository/arsenal-raw-hosted/npm-libraries/node-sass"
                        env.NODE_ENV = "production" 
                        sh "npm run build"
                    }
                }
            }
            
            def unitTestEnable = true
            unitTestEnable = params.unitTestEnable
            
            if( unitTestEnable ){
                stage('unit test'){
                    container('node') {
                        sh "npm test"
                    }
                }
            }

            stage('Build Docker image') {
                container('build-tools') {
                    docker.withRegistry("${dockerRegistry}", 'cluster-registry-credentials') {
                        sh "docker build -t ${image}:${tag} -f devops/jenkins/Dockerfile ."
                        sh "docker push ${image}:${tag}"
                        sh "docker tag ${image}:${tag} ${image}:latest"
                        sh "docker push ${image}:latest"
                    }
                }
            }

            stage( 'Helm lint' ) {
                container('build-tools') {
                    dir('devops/helm/poss-web'){
                        if ( isIpcRunningEnv ) {
                            sh """
                            # initial helm
                            # central helm repo can't connect
                            # setting stable repo by local repo
                            helm init --client-only --stable-repo-url "http://127.0.0.1:8879/charts" --skip-refresh
                            helm lint --namespace oss --tiller-namespace oss .
                            """
                        } else {
                            sh """
                            helm lint --namespace oss .
                            """
                        }
                  }
                }
            }


            stage("Summary") {
                if ( mattermostDevIncomingUrl && isIpcRunningEnv ) {
                   gl_SummaryMessageMD(mattermostDevIncomingUrl)
                }
            }

        } catch(e) {
            container('build-tools'){
                print "Clean up ${env.JOB_NAME} workspace..."
                sh 'ls -A1|xargs rm -rf' /* clean up our workspace */
            }


            currentBuild.result = "FAILED"
            if ( mattermostDevIncomingUrl && isIpcRunningEnv ) {
               def buildMessage="**Error "+ e.toString()+"**"
               gl_SummaryMessageMD(mattermostDevIncomingUrl, 'F', buildMessage)
            } else {
                print " **Error :: " + e.toString()+"**"
            }
        } finally {
            EXIST=sh returnStatus: true, script: 'ls test-results.xml'
            if ( EXIST==0)
            {
                junit allowEmptyResults: true, testResults: 'test-results.xml'
            }
        }
    }
}
