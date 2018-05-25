@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )  
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.* 
import static groovyx.net.http.ContentType.*

//def JOB_NAME='auto_zll/test'
//def BUILD_ID='22'
//def JENKINS_HOME='D:\\Program Files (x86)\\Jenkins'
if("${JOB_NAME}".contains('auto_')){
	def shortName="${JOB_NAME}".split("/")[0]
	println "${JENKINS_HOME}\\jobs\\${shortName}\\configurations\\axis-label\\${shortName}\\htmlreports\\Test_Report\\${shortName}${BUILD_ID}.html"
	def srcFolder=new File("${JENKINS_HOME}\\jobs\\${shortName}\\configurations\\axis-label\\${shortName}\\htmlreports\\Test_Report")
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
	
	//def http = new HTTPBuilder("http://192.168.2.103:8081/1/jenkins/job/${shortName}")
	def http = new HTTPBuilder("http://118.178.133.96:8080/hike/1/jenkins/job/${shortName}")
	http.request( PUT, JSON ) { req ->
		body = [isComplete:true] 
		response.success = { resp, json ->
			println "agent status synced! ${resp.status}"
		}

		response.failure = { resp ->
			println "Sync agent status failed with status ${resp.status}"
		}
	}
	
	def delete=new File("${JENKINS_HOME}\\userContent")
    delete.eachFile{file->
		if(file.lastModified()<new Date().getTime()-7*24*3600){
			file.delete()
		}
	}
}
