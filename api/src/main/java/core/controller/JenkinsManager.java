package core.controller;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import core.pojo.Agent;
import core.pojo.Task;
import core.service.JenkinsService;

@RestController
@RequestMapping("/api/v2/jenkins")
public class JenkinsManager{
    @Autowired
    private JenkinsService jenkinsService=null;
    
    @RequestMapping(value="/job", method=RequestMethod.POST)
    public Task startJob(@RequestBody Task task){
        try{
            return jenkinsService.startJob(task);
        }catch(Exception e){
            return null;
        }
    }
    @RequestMapping("/jobs")
    public List<Task> getJobStatus(){
        return jenkinsService.getAllTasks();
    }
    @RequestMapping("/agents")
    public List<Agent> getAllAgents(){
        return jenkinsService.getAllAgents();
    }
    @RequestMapping("/jobdetail/{jobName}/build/{buildId}")
    public String getJobDetail(@PathVariable String jobName,@PathVariable Integer buildId){
        // BuildPojo suite=new BuildPojo();
        // suite.setBuildId(buildId);
        // suite.setJobName(jobName);
        // try{
        //     TestReport result= jenkinsService.getTestResult(suite);
        //     return result.toString();
        // }catch(Exception e){
        //     res.sendError(500, e.getMessage());
        // }
        try{
            return "{\"url\":\""+jenkinsService.getTestReport(jobName,buildId)+"\"}";
        }catch(Exception e){
            return "failed";
        }
    }
    @RequestMapping("/job/{jobName}")
    public String syncRunningJob(@PathVariable String jobName,@RequestBody Map<String,Integer> task){
        jenkinsService.syncAgentStatus(jobName, task.get("isComplete"));
        return "success";
    }
    @RequestMapping("/job/{jobName}/build/{buildId}")
    public Task getExecutioin(@PathVariable String jobName,@PathVariable Integer buildId){
        return jenkinsService.getExecution(jobName, buildId);
    }
    @RequestMapping(value="/jobstatus",method=RequestMethod.PUT)
    public String updateExecStatus(@RequestBody Task suite){
        jenkinsService.updateExecStatus(suite);
        return "success"; 
    }
    @RequestMapping(value="/casestatus",method=RequestMethod.PUT)
    public String updateCaseStatus(@RequestBody Task suite){
        String key=(String)suite.getTests().keySet().toArray()[0];
        jenkinsService.updateTestStatus(suite.getJenkinsJobName(), suite.getJenkinsBuildId(), key, suite.getTests().get(key));
        return "success"; 
    }
    @RequestMapping(value="/agentstatus",method=RequestMethod.PUT)
    public String updateAgentStatus(@RequestBody Agent agent){
        jenkinsService.updateAgentStatus(agent.getJobName(), agent.getStatus());
        return "success"; 
    }
    @RequestMapping(value="/log",method=RequestMethod.DELETE)
    public String deleteLogFile(){
        jenkinsService.deleteLogFile();
        return "success"; 
    }
}