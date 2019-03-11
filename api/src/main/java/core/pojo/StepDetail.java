package core.pojo;

import java.util.ArrayList;
import java.util.List;

public class StepDetail{
    private Integer index = null;
    private Action action = null;
    private String stepDesc = null;
    private Uiobject uiObject = null;
    private String refTestId = null;
    private List<Para> stepParas = null;
    private Para resPara = null;
    /*0:ui, 1:api, 2:ref*/
    private Integer stepType = null;

    public void setIndex(Integer index){
        this.index=index;
    }
    public Integer getIndex(){
        return this.index;
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

    public void setUiObject(Uiobject uiObject){
        this.uiObject=uiObject;
    }
    public Uiobject getUiobject(){
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
    // public StepDetail(Step step){
    //     this.index=step.getIndex();
    //     this.action=new Action(step.getActionId());
    //     this.stepDesc=step.getStepDesc();
    //     this.uiObject=new Uiobject(step.getUiObjectId());
    //     this.refTestId=step.getRefTestId();
    //     this.resPara=new Para(step.getResParaId(),step.getIndex(), step.getRefTestId());
    //     if(step.getParas()!=null & step.getParas().size()!=0){
    //         stepParas = new ArrayList<Para>();
    //         for(Long item:step.getParas()){
    //             Para p=new Para(item, step.getIndex(), step.getRefTestId());
    //             stepParas.add(p);
    //         }
    //     }
    // }
}