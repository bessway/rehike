package core.pojo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
public class StepDataPojo{
    private Integer sIndex=null;
    private String response=null;
    private String target=null;
    private ArrayList<ParaPojo> stepParas=null;
    public static Comparator<StepDataPojo> sindexComp=new Comparator<StepDataPojo>() {
        @Override
        public int compare(StepDataPojo first, StepDataPojo second){
            return first.getsIndex()-second.getsIndex();
        }
    };
    public Integer getsIndex(){
        return this.sIndex;
    }
    public String getResponse(){
        return this.response;
    }
    public String getTarget(){
        return this.target;
    }
    public ArrayList<ParaPojo> getStepParas(){
        return this.stepParas;
    }
    public ArrayList<ParaPojo> getSortedStepParas(){
        Collections.sort(this.stepParas,ParaPojo.pindexComp);
        return this.stepParas;
    }
    public String toString(){
        String ret="sIndex:"+String.valueOf(this.sIndex)
        +"\nresponse:"+this.response
        +"\ntarget:"+this.target;
        for(ParaPojo item:this.stepParas){
            ret=ret+"\nstepParas----"+item.toString();
        }
        return ret;
    }
}