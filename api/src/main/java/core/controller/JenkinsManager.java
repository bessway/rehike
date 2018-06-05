package core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;
import core.service.JenkinsService;

@RestController
@RequestMapping("/1/jenkins")
public class JenkinsManager{
    private Logger logger=Logger.getLogger(JenkinsManager.class);
    @Autowired
    private JenkinsService jenkinsService=null;
    private Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    
    @RequestMapping(value="/job", method=RequestMethod.POST)
    public String startJob(HttpServletResponse res,@RequestBody BuildPojo build) throws Exception{
        BuildPojo result=null;
        try{
            result=jenkinsService.startJob(build);
            String strResult=gson.toJson(result);
            logger.debug(strResult);
            return strResult;
        }catch(Exception e){
            logger.debug(e.getMessage());
            res.sendError(500, e.getMessage());
        }
        return "{}";
    }
    @RequestMapping("/jobs")
    public String getJobStatus(){
        return gson.toJson(jenkinsService.getAllBuilds());
    }
    @RequestMapping("/agents")
    public String getAllAgents(){
        return gson.toJson(jenkinsService.getAllAgents());
    }
    @RequestMapping("/jobdetail/{jobName}/build/{buildId}")
    public String getJobDetail(HttpServletResponse res, @PathVariable String jobName,@PathVariable Integer buildId) throws Exception{
/*        BuildPojo suite=new BuildPojo();
        suite.setBuildId(buildId);
        suite.setJobName(jobName);
        try{
            TestReport result= jenkinsService.getTestResult(suite);
            return result.toString();
        }catch(Exception e){
            res.sendError(500, e.getMessage());
        }*/
        return "{\"url\":\""+jenkinsService.getTestReport(jobName,buildId)+"\"}";
    }
    @RequestMapping("/job/{jobName}")
    public String syncRunningJob(@PathVariable String jobName,@RequestBody Map<String,Boolean> build){
        jenkinsService.syncAgentStatus(jobName, build.get("isComplete"));
        return "{\"status\":true}";
    }
    @RequestMapping("/job/{jobName}/build/{buildId}")
    public String getExecutioin(@PathVariable String jobName,@PathVariable Integer buildId){
        return gson.toJson(jenkinsService.getExecution(jobName, buildId));
    }
    @RequestMapping(value="/jobstatus",method=RequestMethod.PUT)
    public String updateExecStatus(@RequestBody BuildPojo suite){
        jenkinsService.updateExecStatus(suite);
        return "{\"status\":true}"; 
    }
    @RequestMapping(value="/casestatus",method=RequestMethod.PUT)
    public String updateCaseStatus(@RequestBody BuildPojo suite){
        String key=(String)suite.getCases().keySet().toArray()[0];
        jenkinsService.updateCaseStatus(suite.getJobName(), suite.getBuildId(), key, suite.getCases().get(key));
        return "{\"status\":true}"; 
    }
    @RequestMapping(value="/agentstatus",method=RequestMethod.PUT)
    public String updateAgentStatus(@RequestBody AgentPojo agent){
        jenkinsService.updateAgentStatus(agent.getJobName(), agent.getStatus());
        return "{\"status\":true}"; 
    }
    @RequestMapping(value="/log",method=RequestMethod.DELETE)
    public String deleteLogFile(){
        jenkinsService.deleteLogFile();
        return "{\"status\":true}"; 
    }
}