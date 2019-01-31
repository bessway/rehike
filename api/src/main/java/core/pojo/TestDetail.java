package core.pojo;

import java.util.List;

public class TestDetail{
    private Long id = null;
    private String testId = null;
    private String testDesc = null;
    private String parentId = null;
    private List<StepDetail> steps = null;

    public void setId(Long id){
        this.id=id;
    }
    public Long getId(){
        return this.id;
    }

    public void setTestId(String testId){
        this.testId=testId;
    }
    public String getTestId(){
        return this.testId;
    }

    public void setTestDesc(String testDesc){
        this.testDesc=testDesc;
    }
    public String getTestDesc(){
        return this.testDesc;
    }

    public void setParentId(String parentId){
        this.parentId=parentId;
    }
    public String getParentId(){
        return this.parentId;
    }

    public void setSteps(List<StepDetail> steps){
        this.steps=steps;
    }
    public List<StepDetail> getSteps(){
        return this.steps;
    }
}