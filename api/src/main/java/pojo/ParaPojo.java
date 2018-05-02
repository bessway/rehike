package pojo;
import java.util.Comparator;
public class ParaPojo{
    private String pValue=null;
    private Integer pIndex=null;
    private String pDesc=null;
    public static Comparator<ParaPojo> pindexComp=new Comparator<ParaPojo>() {
        @Override
        public int compare(ParaPojo first, ParaPojo second){
            return first.getpIndex()-second.getpIndex();
        }
    };

    public String getpValue(){
        return this.pValue;
    }
    public String getpDesc(){
        return this.pDesc;
    }
    public Integer getpIndex(){
        return this.pIndex;
    }
    public String toString(){
        return "pValue:"+this.pValue
        +"\npIndex:"+String.valueOf(this.pIndex)
        +"\npDesc:"+this.pDesc;
    }
}