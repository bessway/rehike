package core.pojo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="parameters")
public class Para{
    private String testId = null;
    private Long paraId = null;
    private String paraName = null;
    private String paraValue = null;
    private String refTestId = null;
    private Integer isFormalPara = null;
    private Integer stepId = null;

    public void setTestId(String testId){
        this.testId=testId;
    }
    public String getTestId(){
        return this.testId;
    }

    public void setParaId(Long paraId){
        this.paraId=paraId;
    }
    public Long getParaId(){
        return this.paraId;
    }

    public void setParaName(String paraName){
        this.paraName=paraName;
    }
    public String getParaName(){
        return this.paraName;
    }

    public void setParaValue(String paraValue){
        this.paraValue=paraValue;
    }
    public String getParaValue(){
        return this.paraValue;
    }

    public void setRefTestId(String refTestId){
        this.refTestId=refTestId;
    }
    public String getRefTestId(){
        return this.refTestId;
    }

    public void setIsFormalPara(Integer isFormalPara){
        this.isFormalPara=isFormalPara;
    }
    public Integer getIsFormalPara(){
        return this.isFormalPara;
    }

    public void setStepId(Integer stepId){
        this.stepId=stepId;
    }
    public Integer getStepId(){
        return this.stepId;
    }
}