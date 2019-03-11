package core.pojo;

import java.util.ArrayList;
import java.util.List;

public class TestDetail{
    private Long index = null;
    private String testId = null;
    private String testDesc = null;
    private String parentId = null;
    private List<StepDetail> steps = null;

    public void setIndex(Long index){
        this.index=index;
    }
    public Long getIndex(){
        return this.index;
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

    // public TestDetail(Test test){
    //     this.testId=test.getTestId();
    //     this.index=test.getIndex();
    //     this.parentId=test.getParentId();
    //     this.testDesc=test.getTestDesc();
    //     if(test.getSteps()==null || test.getSteps().size()==0){
    //         steps=new ArrayList<StepDetail>();
    //         for(Step item:test.getSteps()){
    //             steps.add(new StepDetail(item));
    //         }
    //     }
    // }
}