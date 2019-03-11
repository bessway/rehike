package core.pojo;

import java.util.List;

public class Step{
    private Integer index = null;
    private Long uniqueIdInTest = null;
    private String actionId = null;
    private String stepDesc = null;
    private String uiObjectId = null;
    private String refTestId = null;
    private List<Long> paras = null;
    private Long resParaId = null;
    /*0:ui, 1:api, 2:ref*/
    private Integer stepType = null;
    //private List<Long> refParas = null;

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

    public void setParas(List<Long> paras){
        this.paras=paras;
    }
    public List<Long> getParas(){
        return this.paras;
    }

    public void setResParaId(Long resParaId){
        this.resParaId=resParaId;
    }
    public Long getResParaId(){
        return this.resParaId;
    }

    public void setStepType(Integer stepType){
        this.stepType=stepType;
    }
    public Integer getStepType(){
        return this.stepType;
    }
    public Long getUniqueIdInTest(){
        return this.uniqueIdInTest;
    }
    public void setUniqueIdInTeset(Long uniqueIdInTest){
        this.uniqueIdInTest=uniqueIdInTest;
    }
    // public void setRefParas(List<Long> refParas){
    //     this.refParas=refParas;
    // }
    // public List<Long> getRefParas(){
    //     return this.refParas;
    // }
}