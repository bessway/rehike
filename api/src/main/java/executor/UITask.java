package executor;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import pojo.CaseDataPojo;
import pojo.CasePojo;
import pojo.KeyPojo;
import utils.DBUtils;
import utils.SeleniumUtils;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
public class UITask implements Executor<CasePojo,CaseDataPojo> {
    private Hashtable<String, String> gPara = null;
    private ArrayList<CasePojo> tests = null;
    private ArrayList<CaseDataPojo> data = null;
    private ArrayList<KeyPojo> keys = null;
    private Executor successor = null;
    private HashMap<String,CaseDataPojo> sortData=null;
    public static HashMap<String,KeyPojo> sortKey=null;

    @BeforeClass
    @Parameters({ "testId" })
    public void loadData(String testId) throws Exception{
        //从数据库获取所有的全局变量
        this.loadGlobalParas();
        //从数据库根据suiteid获取所有的case
        this.loadTest(testId);
        //从数据库根据suiteid获取所有的数据
        this.loadTestData(testId);
        this.loadKeys();
    }

    @Test(groups = { "ui" })
    public String start() throws Exception {
        String ret = this.execute(null, gPara);
        return ret;
    }


    public String execute(Hashtable<String, String> sPara, Hashtable<String, String> gPara) throws Exception {
        String taskResult = "true";
        for (CasePojo casz : this.tests) {
            String result = this.getSuccessor(casz,this.getTestData(casz)).execute(this.getSharedData(), gPara);
            //有一个失败则整体是失败状态
            if (result.equals("false")) {
                taskResult = result;
            }
            SeleniumUtils.closeDrivers(null);
        }
        return taskResult;
    }
    @Override
    public Executor getSuccessor(CasePojo test,CaseDataPojo data){
        this.successor=new CaseExecutor(test, data);
        return this.successor;
    }
    public CaseDataPojo getTestData(CasePojo test){
        return this.sortData.get(test.getCaseId());
    }
    public Hashtable<String,String> getSharedData(){
        return null;
    }
    
    private void loadTest(String testId) throws Exception {
        this.tests = new Gson().fromJson(DBUtils.getData("case.json"), new TypeToken<ArrayList<CasePojo>>() {
        }.getType());
    }

    private void loadTestData(String testId) throws Exception {
        this.data=new Gson().fromJson(DBUtils.getData("casedata.json"), new TypeToken<ArrayList<CaseDataPojo>>() {
        }.getType());
        this.sortData=new HashMap<String,CaseDataPojo>();
        for(CaseDataPojo item:this.data){
            this.sortData.put(item.getCaseId(), item);
        }
    }

    private void loadGlobalParas() throws Exception {
        this.gPara=new Gson().fromJson(DBUtils.getData("globals.json"), new TypeToken<Hashtable<String,String>>() {
        }.getType());
    }

    private void loadKeys() throws Exception{
        this.keys=new Gson().fromJson(DBUtils.getData("keys.json"), new TypeToken<ArrayList<KeyPojo>>() {
        }.getType());
        UITask.sortKey=new HashMap<String,KeyPojo>();
        for(KeyPojo item:this.keys){
            UITask.sortKey.put(item.getName(), item);
        }
    }

    public static void main(String[] args) throws Exception{
        UITask t=new UITask();
        t.loadData("");
        t.start();
    }
}