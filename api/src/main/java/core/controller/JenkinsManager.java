package core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.offbytwo.jenkins.model.TestReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import core.pojo.BuildPojo;
import core.service.JenkinsService;

@RestController
@RequestMapping("/1/jenkins")
public class JenkinsManager{
    @Autowired
    private JenkinsService jenkinsService=null;
    private Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    
    @RequestMapping(value="/job", method=RequestMethod.POST)
    public String startJob(HttpServletResponse res,@RequestBody BuildPojo build) throws Exception{
        BuildPojo result=null;
        try{
            result=jenkinsService.startJob(build);
            return gson.toJson(result);
        }catch(Exception e){
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
    @RequestMapping("/job/{jobName}/build/{buildId}")
    public String getJobDetail(HttpServletResponse res, @PathVariable String jobName,@PathVariable Integer buildId) throws Exception{
        BuildPojo suite=new BuildPojo();
        //for debug to set 98
        suite.setBuildId(98);
        suite.setJobName(jobName);
        try{
            TestReport result= jenkinsService.getTestResult(suite);
            return result.toString();
        }catch(Exception e){
            res.sendError(500, e.getMessage());
        }
        return "{}";
    }
    @RequestMapping("/job/{jobName}")
    public String syncRunningJob(@PathVariable String jobName,@RequestBody Map<String,Boolean> build){
        jenkinsService.syncRunningJob(jobName, build.get("isComplete"));
        return "{'status':true}";
    }
}