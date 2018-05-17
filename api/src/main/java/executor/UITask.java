package executor;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import core.pojo.BuildPojo;
import core.pojo.CaseDataPojo;
import core.pojo.CasePojo;
import core.pojo.KeyPojo;
import core.service.DataService;
import core.service.JenkinsService;
import core.service.KeyService;
import core.service.TestService;
import utils.SeleniumUtils;
import utils.Utils;

public class UITask implements Executor<CasePojo,CaseDataPojo> {
    private String jenkinsConfigFile = "jenkins.properties";
    private Map<String, String> gPara = null;
    private BuildPojo suite = null;
    private Executor successor = null;
    private Map<String,CaseDataPojo> data=null;
    private List<CasePojo> tests=null;
    public static Hashtable<String,KeyPojo> cachedKey=null;
    @Autowired
    DataService dataService=null;
    @Autowired
    JenkinsService jenkinsService=null;
    @Autowired
    KeyService keyService=null;
    @Autowired
    TestService testService=null;
    

    @BeforeClass
    @Parameters({ "jobName","buildId","dataVersion" })
    public void loadData(String jobName,Integer buildId,String dataVersion) throws Exception{
        //init DB
        //从数据库获取所有的全局变量
        this.loadGlobalParas();
        //从数据库根据suiteid获取所有的case
        this.loadSuite(jobName,buildId);
        List<String> casesId=Lists.newArrayList(this.suite.getCases().keySet());
        this.loadTests(casesId);
        //从数据库根据suiteid获取所有的数据
        this.loadTestData(casesId,dataVersion);
        this.loadKeys();
    }

    @Test
    public String start() throws Exception {
        String ret = this.execute(null, this.gPara);
        return ret;
    }

    public String execute(Map<String, String> sPara, Map<String, String> gPara) throws Exception {
        String taskResult = "true";
        for (CasePojo casz : this.tests) {
            if(checkStopExec()){
                this.suite.setFroceStop(true);
                taskResult="stopped";
                break;
            }
            String result="";
            try{
                result = this.getSuccessor(casz,this.getTestData(casz)).execute(this.getSharedData(), gPara);
            }catch(Exception e){
                result="false";
            }
            //有一个case失败则suite是失败状态
            if (result.equals("false")) {
                taskResult = result;
                this.suite.getCases().put(casz.getCaseId(),Utils.ExecStatus.FAILED);
            }else{
                this.suite.getCases().put(casz.getCaseId(),Utils.ExecStatus.SUCCESS);
            }
            this.updateCaseStatus();
            SeleniumUtils.closeDrivers(null);
        }
        if(taskResult.equals("stopped")){
            this.suite.setBuildStatus(Utils.ExecStatus.FORCESTOP);
        }else if(taskResult.equals("false")){
            this.suite.setBuildStatus(Utils.ExecStatus.FAILED);
        }else{
            this.suite.setBuildStatus(Utils.ExecStatus.SUCCESS);
        }
        //同步服务端job的执行情况
        this.syncRunningJob();
        //保存suite执行结果
        this.saveTestResult();
        return taskResult;
    }
    @Override
    public Executor getSuccessor(CasePojo test,CaseDataPojo data){
        this.successor=new CaseExecutor(test, data);
        return this.successor;
    }
    
    public CaseDataPojo getTestData(CasePojo test){
        return this.data.get(test.getCaseId());
    }
    public Map<String,String> getSharedData(){
        return null;
    }
    //================================================================================================================//
    private void loadSuite(String jobName,Integer buildId) {
        this.suite=jenkinsService.getExecution(jobName, buildId);
    }
    private void loadTests(List<String> casesId){
        this.tests=testService.getCases(casesId);
    }
    private void loadTestData(List<String> casesId,String version) {
        List<CaseDataPojo> result=dataService.getCasesData(casesId, version);

        this.data=new HashMap<String,CaseDataPojo>();
        for(CaseDataPojo item:result){
            this.data.put(item.getCaseId(), item);
        }
    }
    //检查是否停止执行
    private Boolean checkStopExec(){
        return false;
    }
    //suite执行完后保存结果
    private void saveTestResult(){

    }
    //更新suite中每个case的执行结果
    private void updateCaseStatus(){

    }
    //同步服务器的runningjob
    private void syncRunningJob(){
        try{
            Properties jProperty=Utils.readPropery(this.jenkinsConfigFile);
            String url=jProperty.getProperty("jenkins.host")+"/1/jenkins/job/"+this.suite.getJobName();
            CloseableHttpClient client=HttpClients.createDefault();
            HttpPut put=new HttpPut(url);
            put.addHeader("Content-Type","application/json;charset=UTF-8");
            put.setEntity(new StringEntity("{'isComplete':true}"));
            CloseableHttpResponse res= client.execute(put);
            Integer status=res.getStatusLine().getStatusCode();
            if(status!=200){
                throw new Exception("sync running job failed: "+this.suite.getJobName());
            }
        }catch(Exception e){

        }
    }
    private void loadGlobalParas() throws Exception {
        List<String> result=dataService.getGlobalParas();
        this.gPara=new HashMap<String,String>();
        for(String item:result){
            this.gPara.put(item,"");
        }
    }

    private void loadKeys() throws Exception{
        List<KeyPojo> result=keyService.getAllKeys();

        UITask.cachedKey=new Hashtable<String,KeyPojo>();
        for(KeyPojo item:result){
            UITask.cachedKey.put(item.getName(), item);
        }
    }
}