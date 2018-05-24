package executor;

import java.util.Map;

import core.pojo.KeyPojo;

import core.pojo.StepDataPojo;
import core.pojo.StepPojo;

public class StepExecutor implements Executor<KeyPojo,StepDataPojo>{
    private StepPojo test=null;
    private Executor successor=null;
    private StepDataPojo data=null;

    public StepExecutor(StepPojo test,StepDataPojo data){
        this.test=test;
        this.data=data;
    }
    
    public String execute(Map<String,String> sPara,Map<String,String> gPara) throws Exception{
        String keyName=this.test.getKey();
        String result=this.getSuccessor(this.getKeyPojo(keyName), this.data).execute(sPara, gPara);

        return result;
    }
    public Executor getSuccessor(KeyPojo test,StepDataPojo data){
        this.successor=new KeyExecutor(test, data);

        return this.successor;
    }
    public KeyPojo getKeyPojo(String keyName){
        return UITask.cachedKey.get(keyName);
    }
}