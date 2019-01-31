package core.pojo;

import java.util.List;

public class Action{
    private String actionId = null;
    private String actionName = null;
    private Integer hasUiObj = null;
    private Integer hasResponse = null;
    private List<ActionPara> actionParas = null;

    public void setActionId(String actionId){
        this.actionId=actionId;
    }
    public String getActionId(){
        return this.actionId;
    }

    public void setActionName(String actionName){
        this.actionName=actionName;
    }
    public String getActionName(){
        return this.actionName;
    }

    public void setHasUiObj(Integer hasUiObj){
        this.hasUiObj=hasUiObj;
    }
    public Integer getHasUiObj(){
        return this.hasUiObj;
    }

    public void setHasResponse(Integer hasResponse){
        this.hasResponse=hasResponse;
    }
    public Integer getHasResponse(){
        return this.hasResponse;
    }

    public void setActionParas(List<ActionPara> actionParas){
        this.actionParas=actionParas;
    }
    public List<ActionPara> getActionParas(){
        return this.actionParas;
    }
}