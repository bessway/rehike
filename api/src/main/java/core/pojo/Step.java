package core.pojo;

import java.util.List;

public class Step{
    private Integer index = null;
    private String actionId = null;
    private String stepDesc = null;
    private String uiObjectId = null;
    private String refTestId = null;
    private List<String> paras = null;
    private String resParaId = null;
    /*0:ui, 1:api, 2:ref*/
    private Integer stepType = null;

    public void setIndex(Integer index){
        this.index=index;
    }
    public Integer getIndex(){
        return this.index;
    }

    public void setActionId(String actionId){
        this.actionId=actionId;
    }
    public String getActionId(){
        return this.actionId;
    }

    public void setStepDesc(String stepDesc){
        this.stepDesc=stepDesc;
    }
    public String getStepDesc(){
        return this.stepDesc;
    }

    public void setUiObjectId(String uiObjectId){
        this.uiObjectId=uiObjectId;
    }
    public String getUiObjectId(){
        return this.uiObjectId;
    }

    public void setRefTestId(String refTestId){
        this.refTestId=refTestId;
    }
    public String getRefTestId(){
        return this.refTestId;
    }

    public void setParas(List<String> paras){
        this.paras=paras;
    }
    public List<String> getParas(){
        return this.paras;
    }

    public void setResParaId(String resParaId){
        this.resParaId=resParaId;
    }
    public String getResParaId(){
        return this.resParaId;
    }

    public void setStepType(Integer stepType){
        this.stepType=stepType;
    }
    public Integer getStepType(){
        return this.stepType;
    }
}