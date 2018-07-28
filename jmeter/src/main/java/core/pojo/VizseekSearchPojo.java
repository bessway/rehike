package core.pojo;

import java.util.List;

public class VizseekSearchPojo{
    private Integer count=null;
    private String inputLine=null;
    private String unit=null;
    private List<VizseekSkuPojo> hits=null;
    
    public Integer getCount(){
        return this.count;
    }
    public void setCount(Integer count){
        this.count=count;
    }
    public String getInputLine(){
        return this.inputLine;
    }
    public void setInputLine(String inputLine){
        this.inputLine=inputLine;
    }
    public String getUnit(){
        return this.unit;
    }
    public void setUnit(String unit){
        this.unit=unit;
    }
    public List<VizseekSkuPojo> getHits(){
        return this.hits;
    }
    public void setHits(List<VizseekSkuPojo> hits){
        this.hits=hits;
    }
}