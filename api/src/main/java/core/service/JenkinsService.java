package core.service;

import java.util.List;

import com.offbytwo.jenkins.model.TestReport;

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;

public interface JenkinsService{
    public BuildPojo startJob(BuildPojo suite) throws Exception;
    public List<BuildPojo> getAllBuilds();
    public TestReport getTestResult(BuildPojo suite) throws Exception;
    public List<AgentPojo> getAllAgents();
    public BuildPojo getExecution(String jobName,Integer buildId);
    public Boolean syncRunningJob(String jobName,Boolean isJobRunning);
}