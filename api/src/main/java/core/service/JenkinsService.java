package core.service;

import java.util.List;

import core.pojo.Agent;
import core.pojo.Task;

public interface JenkinsService{
    public Task startJob(Task suite) throws Exception;
    public List<Task> getAllTasks();
    //public TestReport getTestResult(Task suite) throws Exception;
    //public String getTestReport(String jobName,Integer taskId) throws Exception;
    public List<Agent> getAllAgents();
    public Task getExecution(String jobName,Integer taskId);
    public void syncAgentStatus(String jobName,Integer isJobRunning);
    public void updateExecStatus(Task suite);
    public void updateTestStatus(String jobName,Integer taskId,String testId,String status);
    public void updateAgentStatus(String jobName,Integer isFree);
    public void deleteLogFile();
}