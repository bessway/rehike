package executor;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.util.Date;
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
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import core.pojo.BuildPojo;
import core.pojo.CaseDataPojo;
import core.pojo.CasePojo;
import core.pojo.KeyPojo;
import utils.MongoUtils;
import utils.SeleniumUtils;
import utils.Utils;

public class UITask implements Executor<CasePojo, CaseDataPojo> {
    public static Logger logger = Logger.getLogger(UITask.class);
    private String jenkinsConfigFile = "agent.properties";
    private Map<String, String> gPara = null;
    private BuildPojo suite = null;
    private Executor successor = null;
    private Map<String, CaseDataPojo> data = null;
    private List<CasePojo> tests = null;
    public static Hashtable<String, KeyPojo> cachedKey = null;

    @BeforeClass
    @Parameters({ "jobName", "buildId", "dataVersion","logLevel" })
    public void loadData(String jobName, Integer buildId, String dataVersion,String logLevel) throws Exception {
        if (jobName == null || buildId == null || dataVersion == null) {
            throw new Exception(
                    "parameters is null: " + jobName + " | " + String.valueOf(buildId) + " | " + dataVersion);
        }
        logger.debug(jobName);
        logger.debug(buildId);
        logger.debug(dataVersion);
        // init DB
        // 从数据库获取所有的全局变量
        this.loadGlobalParas();
        // 从数据库根据suiteid获取所有的case
        this.loadSuite(jobName, buildId);
        List<String> casesId = Lists.newArrayList(this.suite.getCases().keySet());
        this.loadTests(casesId);
        // 从数据库根据suiteid获取所有的数据
        this.loadTestData(casesId, dataVersion);
        this.loadKeys();
        MongoUtils.getAllObjects();
        logger.debug(Utils.objectsMap.get("BaiduHome.input.keyword"));
    }

    @Test
    public void start() throws Exception {
        this.execute(null, this.gPara);
    }

    public String execute(Map<String, String> sPara, Map<String, String> gPara) throws Exception {
        String taskResult = "true";
        for (CasePojo casz : this.tests) {
            if (checkStopExec()) {
                this.suite.setFroceStop(true);
                taskResult = "stopped";
                break;
            }
            String result = "";
            try {
                if (casz.getSteps() == null || casz.getSteps().size() == 0) {
                    throw new Exception("case does not have steps: " + casz.getCaseId());
                }
                result = this.getSuccessor(casz, this.getTestData(casz)).execute(this.getSharedData(), gPara);
            } catch (Exception e) {
                result = "false";
            }
            // 有一个case失败则suite是失败状态
            if (result.equals("false")) {
                taskResult = result;
                this.suite.getCases().put(casz.getCaseId(), Utils.ExecStatus.FAILED);
            } else {
                this.suite.getCases().put(casz.getCaseId(), Utils.ExecStatus.SUCCESS);
            }
            this.updateCaseStatus(casz.getCaseId(), result);
            SeleniumUtils.closeDrivers(null);
        }
        if (taskResult.equals("stopped")) {
            this.suite.setBuildStatus(Utils.ExecStatus.FORCESTOP);
        } else if (taskResult.equals("false")) {
            this.suite.setBuildStatus(Utils.ExecStatus.FAILED);
        } else {
            this.suite.setBuildStatus(Utils.ExecStatus.SUCCESS);
        }
        // 同步服务端job的执行情况,同时重置agent状态,放在jenkins做了
        //this.syncRunningJob();
        // 保存suite执行结果
        this.suite.setEndTime(new Date());
        this.saveTestResult();
        MongoUtils.closeMongo();
        return taskResult;
    }

    @Override
    public Executor getSuccessor(CasePojo test, CaseDataPojo data) {
        this.successor = new CaseExecutor(test, data);
        return this.successor;
    }

    public CaseDataPojo getTestData(CasePojo test) throws Exception {
        CaseDataPojo result = this.data.get(test.getCaseId());
        if (result == null || result.getStepsData() == null || result.getStepsData().size() == 0) {
            throw new Exception("does not have data: " + test.getCaseId());
        }
        return result;
    }

    public Map<String, String> getSharedData() {
        return null;
    }

    // ================================================================================================================//
    // 检查是否停止执行
    private Boolean checkStopExec() {
        BuildPojo result = MongoUtils.getExecution(this.suite.getJobName(), this.suite.getBuildId());
        return result.isFroceStop() == null ? false : result.isFroceStop();
    }

    // suite执行完后保存结果
    private void saveTestResult() {
        MongoUtils.updateExecStatus(this.suite);
    }

    // 更新suite中每个case的执行结果
    private void updateCaseStatus(String caseId, String status) {
        if (status.equals("failed")) {
            MongoUtils.updateCaseStatus(this.suite.getJobName(), this.suite.getBuildId(), caseId,
                    Utils.ExecStatus.FAILED);
        } else {
            MongoUtils.updateCaseStatus(this.suite.getJobName(), this.suite.getBuildId(), caseId,
                    Utils.ExecStatus.SUCCESS);
        }
    }

    // 同步服务器的runningjob
    private void syncRunningJob() {
        try {
            Properties jProperty = Utils.readPropery(this.jenkinsConfigFile);
            String url = "http://" + jProperty.getProperty("server.host") + ":" + jProperty.getProperty("server.port")
                    + "/1/jenkins/job/" + this.suite.getJobName();
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPut put = new HttpPut(url);
            put.addHeader("Content-Type", "application/json;charset=UTF-8");
            put.setEntity(new StringEntity("{'isComplete':true}"));
            CloseableHttpResponse res = client.execute(put);
            Integer status = res.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new Exception("sync running job failed: " + this.suite.getJobName());
            }
        } catch (Exception e) {
            logger.debug("sync RunningJob" + e.getCause());
        }
    }

    private void loadGlobalParas() throws Exception {
        List<String> result = MongoUtils.getGlobalParas();
        this.gPara = new HashMap<String, String>();
        for (String item : result) {
            this.gPara.put(item, "");
        }
        logger.debug(this.gPara.keySet());
    }

    private void loadKeys() throws Exception {
        List<KeyPojo> result = MongoUtils.getAllKeys();

        UITask.cachedKey = new Hashtable<String, KeyPojo>();
        for (KeyPojo item : result) {
            UITask.cachedKey.put(item.getName(), item);
        }
        logger.debug(UITask.cachedKey.get("click").toString());
    }

    private void loadSuite(String jobName, Integer buildId) {
        this.suite = MongoUtils.getExecution(jobName, buildId);
        logger.debug(this.suite.getJobName());
    }

    private void loadTests(List<String> casesId) {
        this.tests = MongoUtils.getCases(casesId);
        logger.debug(this.tests.size());
    }

    private void loadTestData(List<String> casesId, String version) {
        List<CaseDataPojo> result = MongoUtils.getCasesData(casesId, version);

        this.data = new HashMap<String, CaseDataPojo>();
        for (CaseDataPojo item : result) {
            this.data.put(item.getCaseId(), item);
        }
        logger.debug(this.data.size());
    }
}