@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )  
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.* 
import static groovyx.net.http.ContentType.*
import groovy.json.JsonSlurper

//def JOB_NAME='auto_zll/test'
//def BUILD_ID='22'
//def JENKINS_HOME='D:\\Program Files (x86)\\Jenkins'
//def BUILD_RESULT='SUCCESS'
if("${JOB_NAME}".contains('auto_')){
	def shortName="${JOB_NAME}".split("/")[0]
	println "${JENKINS_HOME}\\jobs\\${shortName}\\configurations\\axis-label\\${shortName}\\htmlreports\\TestReport\\${shortName}${BUILD_ID}.html"
	def srcFolder=new File("${JENKINS_HOME}\\jobs\\${shortName}\\configurations\\axis-label\\${shortName}\\htmlreports\\TestReport")
	if(!srcFolder.exists()){
		println 'report is not published'
	}else{
		srcFolder.eachFile{file->
			def des=new File("${JENKINS_HOME}\\userContent\\"+file.getName())
			des.withOutputStream{
			os-> file.withInputStream {
				ins->
				   os << ins 
				}
			}
		}
	}
	
	def http = new HTTPBuilder("http://127.0.0.1:8080/api/v2/jenkins/job/${shortName}")
	http.request( PUT, JSON ) { req ->
		body = [isComplete:1] 
		response.success = { resp, json ->
			println "agent status synced! ${resp.status}"
		}

		response.failure = { resp ->
			println "Sync agent status failed with status ${resp.status}"
		}
	}
	println "${BUILD_RESULT}"
	if("${BUILD_RESULT}"=='FAILURE'){
		http = new HTTPBuilder("http://127.0.0.1:8080/api/v2/jenkins/taskstatus")
		http.request( PUT, JSON ) { req ->
			def end=new Date().format('yyyy-MM-dd HH:mm:ss')
			body = [forceStop:0,endTime:"${end}".toString(),taskStatus:'EXCEPTION',jenkinsJobName:"${shortName}",jenkinsBuildId:"${BUILD_ID}"] 
			response.success = { resp, json ->
				println "execution status synced! ${resp.status}"
			}

			response.failure = { resp ->
				println "Sync execution status failed with status ${resp.status}"
			}
		}
		
		def jsonSlurper = new JsonSlurper()
        def map = jsonSlurper.parseText('{"msgtype": "text","text": {"content": "Failed to execute '+"${shortName} "+"${BUILD_ID}"+'"}}')
        println(map)
        http = new HTTPBuilder("https://oapi.dingtalk.com/robot/send?access_token=")
        http.request(POST, JSON){req ->
            body = map
            response.success = { resp, json ->
                println "dingding synced! ${resp.status}"
            }
        }
	}else if("${BUILD_RESULT}"=='SUCCESS'){
		def jsonSlurper = new JsonSlurper()
        def map = jsonSlurper.parseText('{"msgtype": "text","text": {"content": "http://127.0.0.1:8010/jenkins/userContent/'+"${shortName}"+"${BUILD_ID}"+'.html"}}')
        println(map)
        http = new HTTPBuilder("https://oapi.dingtalk.com/robot/send?access_token")
        http.request(POST, JSON){req ->
            body = map
            response.success = { resp, json ->
                println "dingding synced! ${resp.status}"
            }
        }
	}
	
	
	def delete=new File("${JENKINS_HOME}\\userContent")
    delete.eachFile{file->
		if(file.lastModified()<new Date().getTime()-7*24*3600*1000){
			file.delete()
		}
	}
}
