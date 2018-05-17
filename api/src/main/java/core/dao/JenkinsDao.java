package core.dao;

import java.util.Date;
import java.util.List;

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;

public interface JenkinsDao{
    public Boolean saveExecution(BuildPojo suite);
    public List<BuildPojo> getRunningJobs();
    public List<BuildPojo> getAllBuilds(Date startDate);
    public List<AgentPojo> getAllAgents(Boolean isPublic);
    public BuildPojo getExecution(String jobName,Integer buildId);
}