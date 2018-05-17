package core.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.MavenBuild;
import com.offbytwo.jenkins.model.MavenJobWithDetails;
import com.offbytwo.jenkins.model.TestReport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.collections.Lists;

import core.dao.JenkinsDao;
import core.pojo.AgentPojo;
import core.pojo.BuildPojo;
import utils.Utils;

@Service("JenkinsService")
public class JenkinsServiceImpl implements JenkinsService {
    private String jenkinsConfigFile = "jenkins.properties";
    private static Properties jProperty = null;
    private static JenkinsServer jServer = null;
    public static Vector<String> runningJob=null;
    @Autowired
    private TestService testService=null;
    @Autowired
    private JenkinsDao jenkinsDao=null;
    
    public JenkinsServer getJenkinsServer() throws Exception{
        if (jProperty == null) {
            try {
                jProperty = Utils.readPropery(this.jenkinsConfigFile);
            } catch (Exception e) {
                System.out.println("cannot find jenkins config file");
            }
            
        }
        if (jServer == null) {
            jServer = new JenkinsServer(new URI(jProperty.getProperty("jenkins.host")),
                    jProperty.getProperty("jenkins.user"), jProperty.getProperty("jenkins.password"));
                    
        }
        return jServer;
    }
    private void isAlreadyStarted(String jobName) throws Exception{
        //如果重启则重新从数据库获取状态
        if(runningJob==null){
            runningJob=new Vector<String>();
            List<BuildPojo> runningJobs=jenkinsDao.getRunningJobs();
            for(BuildPojo item:runningJobs){
                runningJob.add(item.getJobName());
            }
        }
        //这里默认一个job固定在一个agent上
        if(runningJob.contains(jobName)){
            throw new Exception("job "+jobName+" is already started");
        }
    }
    private void findAllCases(BuildPojo suite) throws Exception{
        //找到所有的case，可能会包含非case的node
        List<String> allCases=new ArrayList<String>();
        testService.findAllNodes(Lists.newArrayList(suite.getCases().keySet()), allCases);
        //删除非case的node
        allCases=testService.getValidCases(allCases);
        if(allCases.size()==0){
            runningJob.remove(suite.getJobName());
            throw new Exception("job "+suite.getJobName()+" has no case");
        }
        suite.setCases(null);
        for(String casz:allCases){
            suite.addCase(casz, Utils.ExecStatus.READYTOSTART);
        }
    }
    public BuildPojo startJob(BuildPojo suite) throws Exception {
        this.isAlreadyStarted(suite.getJobName());
        //防止启动另外一个build
        runningJob.add(suite.getJobName());
        this.findAllCases(suite);

        MavenJobWithDetails job=getJenkinsServer().getMavenJob(suite.getJobName());
        Integer id=job.getNextBuildNumber();
        suite.setBuildId(id);
        suite.setBuildStatus(Utils.ExecStatus.READYTOSTART);
        suite.setCreateTime(new Date());
        suite.setPassed(0);
        suite.setFailed(0);
        
        //存储case供agent调用
        jenkinsDao.saveExecution(suite);
        //添加maven参数
        suite.addPara("jobName", suite.getJobName());
        suite.addPara("buildId", String.valueOf(suite.getBuildId()));
/*
        //启动执行
        try{
            job.build(suite.getParas(),true);
        }catch(Exception e){
            runningJob.remove(suite.getJobName());
            throw new Exception("error when calling jenkins:"+e.getMessage());
        }
*/
        BuildPojo result=new BuildPojo();
        result.setBuildId(suite.getBuildId());
        result.setBuildStatus(suite.getBuildStatus());
        result.setJobName(suite.getJobName());
        result.setPassed(suite.getPassed());
        result.setFailed(suite.getFailed());
        result.setCreateTime(suite.getCreateTime());
        //for debug
        runningJob.remove(suite.getJobName());
        return result;
    }
    public List<BuildPojo> getAllBuilds(){
        Calendar now=Calendar.getInstance();
        now.add(Calendar.DATE,-7);
        List<BuildPojo> result= jenkinsDao.getAllBuilds(now.getTime());
        //统计case的状态
        for(BuildPojo item:result){
            Integer passed=0;
            Integer failed=0;
            for(Utils.ExecStatus status:item.getCases().values()){
                if(status.equals(Utils.ExecStatus.SUCCESS)){
                    passed=passed+1;
                }else if(status.equals(Utils.ExecStatus.FAILED)){
                    failed=failed+1;
                }
            }
            item.setPassed(passed);
            item.setFailed(failed);
            item.setCases(null);
        }
        return result;
    }
    public TestReport getTestResult(BuildPojo suite) throws Exception{
        MavenJobWithDetails job=getJenkinsServer().getMavenJob(suite.getJobName());
        MavenBuild build=job.getBuildByNumber(suite.getBuildId());
        TestReport result= build.getTestReport();
        return result;
    }
    public List<AgentPojo> getAllAgents(){
        List<AgentPojo> agents=jenkinsDao.getAllAgents(true);
        //检查是否正在使用中：看是否有正在执行的job
        //这里agent和job是一对一的
        List<BuildPojo> runningJobs=jenkinsDao.getRunningJobs();
        for(AgentPojo item:agents){
            for(BuildPojo build:runningJobs){
                if(build.getJobName().equals(item.getJobName())){
                    item.setStatus(false);
                    break;
                }
            }
            if(item.getStatus()==null){
                item.setStatus(true);
            }
        }
        return agents;
    }
    public BuildPojo getExecution(String jobName,Integer buildId){
        return jenkinsDao.getExecution(jobName, buildId);
    }
    public Boolean syncRunningJob(String jobName,Boolean isJobRunning){
        if(runningJob==null){
            return true;
        }
        if(isJobRunning){
            runningJob.add(jobName);
        }else{
            runningJob.remove(jobName);
        }
        return true;
    }
    public Boolean updateExecStatus(BuildPojo suite){
        return jenkinsDao.updateExecutionStatus(suite);
    }
    public Boolean updateCaseStatus(String jobName,Integer buildId,String caseId,Utils.ExecStatus status){
        return jenkinsDao.updateCaseStatus(jobName, buildId, caseId, status);
    }
}