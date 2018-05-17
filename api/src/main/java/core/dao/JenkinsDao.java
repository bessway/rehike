package core.dao;

import java.util.Date;
import java.util.List;

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;
import utils.Utils;

public interface JenkinsDao{
    public Boolean saveExecution(BuildPojo suite);
    public List<BuildPojo> getRunningJobs();
    public List<BuildPojo> getAllBuilds(Date startDate);
    public List<AgentPojo> getAllAgents(Boolean isPublic);
    public BuildPojo getExecution(String jobName,Integer buildId);
    public Boolean updateExecutionStatus(BuildPojo suite);
    public Boolean updateCaseStatus(String jobName,Integer buildId,String caseId,Utils.ExecStatus status);
    public Boolean updateAgentStatus(String jobName,Boolean isFree);
}