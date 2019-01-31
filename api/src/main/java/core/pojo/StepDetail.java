package core.pojo;

import java.util.List;

public class StepDetail{
    private Integer id = null;
    private Action action = null;
    private String stepDesc = null;
    private UiObject uiObject = null;
    private String refTestId = null;
    private List<Para> stepParas = null;
    private Para resPara = null;
    /*0:ui, 1:api, 2:ref*/
    private Integer stepType = null;

    public void setId(Integer id){
        this.id=id;
    }
    public Integer getId(){
        return this.id;
    }

    public void setAction(Action action){
        this.action=action;
    }
    public Action getAction(){
        return this.action;
    }

    public void setStepDesc(String stepDesc){
        this.stepDesc=stepDesc;
    }
    public String getStepDesc(){
        return this.stepDesc;
    }

    public void setUiObject(UiObject uiObject){
        this.uiObject=uiObject;
    }
    public UiObject getUiObject(){
        return this.uiObject;
    }

    public void setRefTestId(String refTestId){
        this.refTestId=refTestId;
    }
    public String getRefTestId(){
        return this.refTestId;
    }

    public void setResPara(Para resPara){
        this.resPara=resPara;
    }
    public Para getResPara(){
        return this.resPara;
    }

    public void setStepParas(List<Para> stepParas){
        this.stepParas=stepParas;
    }
    public List<Para> getStepParas(){
        return this.stepParas;
    }

    public void setStepType(Integer stepType){
        this.stepType=stepType;
    }
    public Integer getStepType(){
        return this.stepType;
    }
}