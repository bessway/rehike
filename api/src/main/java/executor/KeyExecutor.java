package executor;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import core.pojo.KeyPojo;
import core.pojo.ParaPojo;
import utils.SeleniumUtils;
import java.lang.reflect.Method;
import core.pojo.StepDataPojo;

public class KeyExecutor implements Executor<KeyPojo,StepDataPojo>{
    private KeyPojo test=null;
    private ArrayList<ParaPojo> data=null;
    private String target=null;
    private String response=null;
    public KeyExecutor(KeyPojo test,StepDataPojo data){
        this.test=test;
        this.data=data.getSortedStepParas();
        this.target=data.getTarget();
        this.response=data.getResponse();
    }
    public String execute(Hashtable<String,String> sPara,Hashtable<String,String> gPara) throws Exception{
        String funcName=this.test.getRegFunc();
        int paraCount=this.data.size();
        if(this.test.getTarget()){
            paraCount=paraCount+1;
        }
        Class<String>[] mPara=new Class[paraCount];
        for(int i=0;i<mPara.length;i++){
            mPara[i]=String.class;
        }
        Method toExe=SeleniumUtils.class.getMethod(funcName, mPara);
        //Method toExe=SeleniumUtils.class.getMethod(funcName, String.class, String.class, String.class);
        Object result=null;
        ArrayList<String> mParaValue=new ArrayList<String>();
        if(this.target!=null){
            mParaValue.add(0,this.unpackPara(this.target,sPara,gPara));
        }
        mParaValue.addAll(this.wrapPara(sPara,gPara));
        
        result=toExe.invoke(null, mParaValue.toArray());
        if(this.test.getResponse()){
            if(this.response.contains("{{")){
                gPara.put(this.response,String.valueOf(result));
            }else if(this.response.contains("{")){
                sPara.put(this.response,String.valueOf(result));
            }
        }

        return String.valueOf(result);
    }
    public Executor getSuccessor(KeyPojo test,StepDataPojo data){
        return null;
    }
    public ArrayList<String> wrapPara(Hashtable<String,String> sPara,Hashtable<String,String> gPara){
        Integer pCount=this.data.size();
        ArrayList<String> result=new ArrayList<String>();
        for(int i=0;i<this.data.size();i++){
            result.add(i,this.unpackPara(this.data.get(i).getpValue(), sPara, gPara));
        }
        return result;
    }
    public String unpackPara(String para,Hashtable<String,String> sPara,Hashtable<String,String> gPara){
        if(para.contains("{{")){
            Pattern p=Pattern.compile(".*(\\{\\{.*\\}\\}).*");
            Matcher m=p.matcher(para);
            m.find();
            if(m.groupCount()>0){
                for(int i=1;i<=m.groupCount();i++){
                    para=para.replace(m.group(i), gPara.get(m.group(i)));
                }
            }
        }
        if(para.contains("{")){
            Pattern p=Pattern.compile(".*(\\{.*\\}).*");
            Matcher m=p.matcher(para);
            m.find();
            if(m.groupCount()>0){
                for(int i=1;i<=m.groupCount();i++){
                    para=para.replace(m.group(i), sPara.get(m.group(i)));
                }
            }
        }
        return para;
    }
}