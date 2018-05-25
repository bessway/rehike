package core.service;

import java.util.List;

import com.offbytwo.jenkins.model.TestReport;

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;
import utils.Utils;

public interface JenkinsService{
    public BuildPojo startJob(BuildPojo suite) throws Exception;
    public List<BuildPojo> getAllBuilds();
    public TestReport getTestResult(BuildPojo suite) throws Exception;
    public String getTestReport(String jobName,Integer buildId) throws Exception;
    public List<AgentPojo> getAllAgents();
    public BuildPojo getExecution(String jobName,Integer buildId);
    public Boolean syncAgentStatus(String jobName,Boolean isJobRunning);
    public Boolean updateExecStatus(BuildPojo suite);
    public Boolean updateCaseStatus(String jobName,Integer buildId,String caseId,Utils.ExecStatus status);
    public Boolean updateAgentStatus(String jobName,Boolean isFree);
    public Boolean deleteLogFile();
}