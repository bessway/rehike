package core.pojo;

import java.util.List;

public class DaguanSearchPojo{
    private Integer count=null;
    private String inputLine=null;
    private String unit=null;
    private List<DaguanSkuPojo> hits=null;
    
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
    public List<DaguanSkuPojo> getHits(){
        return this.hits;
    }
    public void setHits(List<DaguanSkuPojo> hits){
        this.hits=hits;
    }
}