package core.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="parameters")
public class Para{
    private String testId = null;
    private Long paraId = null;
    private String paraName = null;
    private String paraValue = null;
    private String refTestId = null;
    private Integer isFormalPara = null;
    private List<Long> stepId = null;
    private String dataVersion = null;

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

    public void setStepId(List<Long> stepId){
        this.stepId=stepId;
    }
    public List<Long> getStepId(){
        return this.stepId;
    }
    public Para(Long paraId, List<Long> stepId, String refTestId){
        this.paraId=paraId;
        /*只有引用的步骤需要记录stepId*/
        if(refTestId!=null && !"".equals(refTestId)){
            this.stepId=stepId;
            this.refTestId=refTestId;
        }
    }
    public void setDataVersion(String dataVersion){
        this.dataVersion=dataVersion;
    }
    public String getDataVersion(){
        return this.dataVersion;
    }
}