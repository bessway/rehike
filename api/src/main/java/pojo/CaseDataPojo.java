package pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map.Entry;

public class CaseDataPojo{
    private String caseId=null;
    private String version=null;
    private Hashtable<String,String> sharedParas=null;
    private ArrayList<StepDataPojo> stepsData=null;
    public String getCaseId(){
        return this.caseId;
    }
    public String getVersion(){
        return this.version;
    }
    public Hashtable<String,String> getSharedParas(){
        return this.sharedParas;
    }
    public ArrayList<StepDataPojo> getStepsData(){
        return this.stepsData;
    }
    public ArrayList<StepDataPojo> getSortedStepsData(){
        Collections.sort(this.stepsData,StepDataPojo.sindexComp);
        return this.stepsData;
    }
    public void addSharedParas(String key,String value){
        if(this.sharedParas==null){
            this.sharedParas=new Hashtable<String,String>();
        }
        this.sharedParas.put(key,value);
    }
    public String toString(){
        String ret="caseId:"+this.caseId
        +"\nversion:"+this.version;
        for(Entry<String,String> item:this.sharedParas.entrySet()){
            ret=ret+"\nsharedparas----"+item.getKey()+"="+item.getValue();
        }
        for(StepDataPojo item:this.stepsData){
            ret=ret+"\nstepsdata----"+item.toString();
        }
        return ret;
    }
}