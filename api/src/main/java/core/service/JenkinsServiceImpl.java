package core.service;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import com.google.common.collect.Lists;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.MavenBuild;
import com.offbytwo.jenkins.model.MavenJobWithDetails;
import com.offbytwo.jenkins.model.TestReport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import core.dao.JenkinsDao;
import core.pojo.Agent;
import core.pojo.Task;
import utils.Utils;

@Service("JenkinsService")
public class JenkinsServiceImpl implements JenkinsService {
    private Logger logger=Logger.getLogger(JenkinsServiceImpl.class);
    private static String jenkinsConfigFile = "jenkins.properties";
    private static Properties jProperty = null;
    private static JenkinsServer jServer = null;
    public static Vector<String> runningJobNames=null;
    @Autowired
    private TestService testService=null;
    @Autowired
    private JenkinsDao jenkinsDao=null;
    
    public JenkinsServiceImpl(){
        //如果重启则重新从数据库获取状态
        //缓存runningJobNames防止把一个jenkins任务启动多次
        // if(runningJobNames==null){
        //     runningJobNames=new Vector<String>();
        //     List<Task> runningJobs=jenkinsDao.getRunningJobs();
        //     for(Task item:runningJobs){
        //         runningJobNames.add(item.getJenkinsJobName());
        //     }
        // }
    }
    private JenkinsServer getJenkinsServer() throws Exception{  
        if (jServer == null) {
            try {
                jProperty = Utils.readPropery(jenkinsConfigFile);
            } catch (Exception e) {
                logger.debug("cannot find jenkins config file");
            }
            jServer = new JenkinsServer(new URI(jProperty.getProperty("jenkins.host")),
                    jProperty.getProperty("jenkins.user"), jProperty.getProperty("jenkins.password"));
        }
        return jServer;
    }
    private void isJenkinsJobInuse(String jobName) throws Exception{
        //这里默认一个job固定在一个agent上
        if(runningJobNames.contains(jobName)){
            throw new Exception("job "+jobName+" is already started");
        }
    }
    private void findAllExectableTests(Task suite) throws Exception{
        //找到所有的case，可能会包含非case的node
        List<String> allTests=new ArrayList<String>();
        allTests = testService.findAllExecutableTests(Lists.newArrayList(suite.getTests().keySet()));
        if(allTests.size()==0){
            runningJobNames.remove(suite.getJenkinsJobName());
            throw new Exception("job "+suite.getJenkinsJobName()+" has no case");
        }
        suite.setTests(null);
        for(String casz:allTests){
            suite.addTest(casz, Utils.ExecStatus.READYTOSTART);
        }
    }
    public Task startJob(Task suite) throws Exception {
        this.isJenkinsJobInuse(suite.getJenkinsJobName());
        //防止启动另外一个jenkins build
        runningJobNames.add(suite.getJenkinsJobName());
        //设置agent状态
        this.updateAgentStatus(suite.getJenkinsJobName(), 0);
        this.findAllExectableTests(suite);

        MavenJobWithDetails job=getJenkinsServer().getMavenJob(suite.getJenkinsJobName());
        Integer id=job.getNextBuildNumber();
        suite.setJenkinsBuildId(id);
        suite.setTaskStatus(Utils.ExecStatus.RUNNING);
        suite.setCreateTime(new Date());
        suite.setPassedCnt(0);
        suite.setFailedCnt(0);
        
        //存储case供agent调用
        jenkinsDao.saveExecution(suite);
        //添加maven参数
        Map<String,String> mavenPara = new HashMap<String,String>();
        mavenPara.put("jobName", suite.getJenkinsJobName());
        mavenPara.put("buildId", String.valueOf(suite.getJenkinsBuildId()));
        mavenPara.put("logLevel", suite.getLogLevel());
        mavenPara.put("env", suite.getEnv());
        mavenPara.put("dataVersion", suite.getDataVersion());

        //启动执行
        try{
            job.build(mavenPara,true);
        }catch(Exception e){
            runningJobNames.remove(suite.getJenkinsJobName());
            throw new Exception("error when calling jenkins:"+e.getMessage());
        }
        //for debug
        //runningJob.remove(suite.getJobName());
        return suite;
    }
    public List<Task> getAllTasks(){
        Calendar now=Calendar.getInstance();
        now.add(Calendar.DATE,-7);
        List<Task> result= jenkinsDao.getAllTasks(now.getTime());
        //统计case的状态
        for(Task item:result){
            Integer passed=0;
            Integer failed=0;
            for(Utils.ExecStatus status:item.getTests().values()){
                if(Utils.ExecStatus.SUCCESS.equals(status)){
                    passed=passed+1;
                }else if(Utils.ExecStatus.FAILED.equals(status)){
                    failed=failed+1;
                }
            }
            item.setPassedCnt(passed);
            item.setFailedCnt(failed);
            item.setTests(null);
        }
        return result;
    }
    public TestReport getTestResult(Task suite) throws Exception{
        MavenJobWithDetails job=getJenkinsServer().getMavenJob(suite.getJenkinsJobName());
        MavenBuild build=job.getBuildByNumber(suite.getJenkinsBuildId());
        TestReport result= build.getTestReport();
        
        return result;
    }
    public String getTestReport(String jobName,Integer buildId) throws Exception{
        return jProperty.getProperty("jenkins.host")+"/userContent/"+jobName+String.valueOf(buildId)+".html";
    }
    public List<Agent> getAllAgents(){
        List<Agent> agents=jenkinsDao.getAllAgents(1);
/*        //检查是否正在使用中：看是否有正在执行的job
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
        }*/
        return agents;
    }
    public Task getExecution(String jobName,Integer buildId){
        return jenkinsDao.getExecution(jobName, buildId);
    }
    public void syncAgentStatus(String jobName,Integer isComplete){
        if(runningJobNames!=null){
            if(isComplete.equals(1)){
                runningJobNames.remove(jobName);
            }else{
                runningJobNames.add(jobName);
            }
        }
        this.updateAgentStatus(jobName, isComplete);
    }
    public void updateExecStatus(Task suite){
        jenkinsDao.updateExecutionStatus(suite);
    }
    public void updateTestStatus(String jobName,Integer taskId,String testId,Utils.ExecStatus status){
        jenkinsDao.updateTestStatus(jobName, taskId, testId, status);
    }
    public void updateAgentStatus(String jobName,Integer isFree){
        jenkinsDao.updateAgentStatus(jobName, isFree);
    }
    public void deleteLogFile(){
        Properties pLog=null;
        try{
            pLog=Utils.readPropery("log4j.properties");
        }catch(Exception e){
            logger.debug("cannot find log4j configuration");
        }
        String path=pLog.getProperty("log4j.appender.logfile.File");
        String dir=System.getenv("user.dir")+"/"+path.split("/")[1];
        File logFolder=new File(dir);
        if(logFolder.exists()){
            for(File f:logFolder.listFiles()){
                if(f.lastModified()<new Date().getTime()-7*24*3600){
                    f.delete();
                }
            }
        }
    }
}