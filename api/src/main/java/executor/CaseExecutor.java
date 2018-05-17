package executor;

import core.pojo.CasePojo;
import core.pojo.CaseDataPojo;
import java.util.Map;

import core.pojo.StepPojo;
import core.pojo.StepDataPojo;
import java.util.List;;

public class CaseExecutor implements Executor<StepPojo,StepDataPojo>{
    private CasePojo test=null;
    private Executor successor=null;
    private CaseDataPojo data=null;
    private List<StepDataPojo> sortStep=null;
    private String caseId=null;

    public CaseExecutor(){
        
    }
    public CaseExecutor(CasePojo test,CaseDataPojo data){
        this.test=test;
        this.data=data;
        this.sortStep=data.getSortedStepsData();
    }
    public CaseExecutor(String caseId){
        this.caseId=caseId;
        this.sortStep=data.getSortedStepsData();
    }
    
    public String execute(Map<String,String> sPara,Map<String,String> gPara) throws Exception{
        String caseResult = "true";
        for (StepPojo step : this.test.getSortedSteps()) {
            String result = this.getSuccessor(step,this.getTestData(step)).execute(this.getSharedData(), gPara);
            //有一个失败则整体是失败状态
            if (result.equals("false")) {
                caseResult = result;
            }
        }
        return caseResult;
    }
    public Executor getSuccessor(StepPojo test,StepDataPojo data){
        this.successor=new StepExecutor(test, data);
        return this.successor;
    }
    public Map<String,String> getSharedData(){
        return this.data.getSharedParas();
    }
    public StepDataPojo getTestData(StepPojo test){
        return this.sortStep.get(test.getIndex());
    }
}