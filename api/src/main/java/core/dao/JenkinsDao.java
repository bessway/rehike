package core.dao;

import java.util.Date;
import java.util.List;

import core.pojo.Agent;
import core.pojo.Task;
import utils.Utils;

public interface JenkinsDao{
    public void saveExecution(Task suite);
    public List<Task> getRunningJobs();
    public List<Task> getAllTasks(Date startDate);
    public List<Agent> getAllAgents(Integer isPublic);
    public Task getExecution(String jobName,Integer taskId);
    public void updateExecutionStatus(Task suite);
    public void updateTestStatus(String jobName,Integer taskId,String caseId,Utils.ExecStatus status);
    public void updateAgentStatus(String jobName,Integer isFree);
}