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
    public Task startJob(@RequestBody Task task) throws Exception{
        return jenkinsService.startJob(task);
    }
    @RequestMapping("/jobs")
    public List<Task> getJobStatus(){
        return jenkinsService.getAllTasks();
    }
    @RequestMapping("/agents")
    public List<Agent> getAllAgents(){
        return jenkinsService.getAllAgents();
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
    @RequestMapping(value="/taskstatus",method=RequestMethod.PUT)
    public String updateExecStatus(@RequestBody Task suite){
        jenkinsService.updateExecStatus(suite);
        return "success"; 
    }
    @RequestMapping(value="/teststatus",method=RequestMethod.PUT)
    public String updateTestStatus(@RequestBody Task suite){
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