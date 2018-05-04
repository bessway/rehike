package core.pojo;
import java.util.Comparator;
public class StepPojo{
    private Integer index=null;
    private String key=null;
    public static Comparator<StepPojo> indexComp=new Comparator<StepPojo>() {
        @Override
        public int compare(StepPojo first, StepPojo second){
            return first.getIndex()-second.getIndex();
        }
    };
    public Integer getIndex(){
        return this.index;
    }
    public String getKey(){
        return this.key;
    }
    public String toString(){
        return "index:"+String.valueOf(this.index)
        +"\nkey:"+this.key;
    }
}