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
import com.offbytwo.jenkins.model.MavenJobWithDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import core.dao.JenkinsDao;
import core.pojo.Agent;
import core.pojo.Task;
import utils.Utils;

@Service("JenkinsService")
public class JenkinsServiceImpl implements JenkinsService {
    private Logger logger = LoggerFactory.getLogger(JenkinsServiceImpl.class);
    private static String jenkinsConfigFile = "jenkins.properties";
    private static Properties jProperty = null;
    private static JenkinsServer jServer = null;
    public static Vector<String> runningJobNames=null;
    @Autowired
    private TestService testService=null;
    
    private JenkinsDao jenkinsDao=null;
    
    @Autowired
    public JenkinsServiceImpl(JenkinsDao jenkinsDao){
        this.jenkinsDao=jenkinsDao;
        //如果重启则重新从数据库获取状态
        //缓存runningJobNames防止把一个jenkins任务启动多次
        if(runningJobNames==null){
            runningJobNames=new Vector<String>();
            List<Task> runningJobs=jenkinsDao.getRunningJobs();
            for(Task item:runningJobs){
                runningJobNames.add(item.getJenkinsJobName());
            }
        }
    }
    private JenkinsServer getJenkinsServer() throws Exception{  
        if (jServer == null) {
            try {
                jProperty = Utils.readPropery(jenkinsConfigFile);
            } catch (Exception e) {
                logger.error("cannot find jenkins config file", e);
                throw e;
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
    private void findAllExecutableTests(Task suite) throws Exception{
        //找到所有的case，因为可能会包含非case的node
        List<String> allTests=new ArrayList<String>();
        allTests = testService.findAllExecutableTests(Lists.newArrayList(suite.getTests().keySet()));
        //如果没有找到可执行的用例，释放agent
        if(allTests.size()==0){
            runningJobNames.remove(suite.getJenkinsJobName());
            throw new Exception("job "+suite.getJenkinsJobName()+" has no case");
        }
        suite.setTests(null);
        for(String casz:allTests){
            suite.addTest(casz, Utils.ExecStatus.READYTOSTART.name());
        }
    }
    public Task startJob(Task suite) throws Exception {
        this.isJenkinsJobInuse(suite.getJenkinsJobName());
        //防止启动另外一个jenkins build
        runningJobNames.add(suite.getJenkinsJobName());
        //设置agent状态
        this.updateAgentStatus(suite.getJenkinsJobName(), 0);

        //启动执行
        try{
            this.findAllExecutableTests(suite);

            MavenJobWithDetails job=getJenkinsServer().getMavenJob(suite.getJenkinsJobName());
            Integer id=job.getNextBuildNumber();
            suite.setJenkinsBuildId(id);
            suite.setTaskStatus(Utils.ExecStatus.RUNNING.name());
            suite.setCreateTime(new Date());
            suite.setPassedCnt(0);
            suite.setFailedCnt(0);
            String reportUrl = "";
            reportUrl = jProperty.getProperty("jenkins.host")+"/userContent/";
            reportUrl = reportUrl+suite.getJenkinsJobName()+String.valueOf(suite.getJenkinsBuildId())+".html";
            suite.setReportUrl(reportUrl);
            
            //存储case供agent调用
            jenkinsDao.saveExecution(suite);
            //添加maven参数
            Map<String,String> mavenPara = new HashMap<String,String>();
            mavenPara.put("jobName", suite.getJenkinsJobName());
            mavenPara.put("buildId", String.valueOf(suite.getJenkinsBuildId()));
            mavenPara.put("dataVersion", suite.getDataVersion());
            mavenPara.put("env", suite.getEnv());
            mavenPara.put("browserType", suite.getBrowserType());
            mavenPara.put("logLevel", suite.getLogLevel());
            job.build(mavenPara,true);
        }catch(Exception e){
            runningJobNames.remove(suite.getJenkinsJobName());
            this.updateAgentStatus(suite.getJenkinsJobName(), 1);
            logger.error("error when calling jenkins", e);
            throw new Exception("error when calling jenkins: "+e.getMessage());
        }
        //for debug
        //runningJobNames.remove(suite.getJenkinsJobName());
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
            for(String status:item.getTests().values()){
                if(Utils.ExecStatus.SUCCESS.name().equals(status)){
                    passed=passed+1;
                }else if(Utils.ExecStatus.FAILED.name().equals(status)){
                    failed=failed+1;
                }
            }
            item.setPassedCnt(passed);
            item.setFailedCnt(failed);
            item.setTests(null);
        }
        return result;
    }
    // public TestReport getTestResult(Task suite) throws Exception{
    //     MavenJobWithDetails job=getJenkinsServer().getMavenJob(suite.getJenkinsJobName());
    //     MavenBuild build=job.getBuildByNumber(suite.getJenkinsBuildId());
    //     TestReport result= build.getTestReport();
        
    //     return result;
    // }
    public List<Agent> getAllAgents(){
        List<Agent> agents=jenkinsDao.getAllAgents(1);
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
    public void updateTestStatus(String jobName,Integer taskId,String testId,String status){
        jenkinsDao.updateTestStatus(jobName, taskId, testId, status);
    }
    public void updateAgentStatus(String jobName,Integer isAvailable){
        jenkinsDao.updateAgentStatus(jobName, isAvailable);
    }
    public void deleteLogFile(){
        Properties pLog=null;
        try{
            pLog=Utils.readPropery("application.properties");
        }catch(Exception e){
            logger.error("cannot find application configuration", e);
        }
        String path=pLog.getProperty("logging.path");
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