package pojo;

import java.util.ArrayList;
import java.util.Collections;


public class CasePojo{
    private String caseId=null;
    private String caseDesc=null;
    private ArrayList<StepPojo> steps=null;
    public String getCaseId(){
        return this.caseId;
    }
    public String getCaseDesc(){
        return this.caseDesc;
    }
    public ArrayList<StepPojo> getSteps(){
        return this.steps;
    }
    public ArrayList<StepPojo> getSortedSteps(){
        Collections.sort(this.steps,StepPojo.indexComp);
        return this.steps;
    }
    public String toString(){
        String ret="caseId:"+this.caseId
        +"\ncaseDesc:"+this.caseDesc;
        for(StepPojo item:steps){
            ret=ret+"\nstep----"+item.toString();
        }
        return ret;
    }
}