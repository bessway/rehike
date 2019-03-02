package core.service;

import java.util.List;

import core.pojo.Para;


public interface ParaService{
    public Para createTestPara(Para newPara) throws Exception;
    public List<Para> copyRefParasToStep(String srcTestId, String tarTestId, Integer stepId);
    public void setParasFormal(List<Para> paras);
    public void setParasValue(List<Para> paras);
    public List<Para> getTestParas(String testId,String dataVersion);
    public List<Para> getTestRefParas(String testId, Integer stepId,String dataVersion);
    public List<Para> getTestParasWithRef(String testId,String dataVersion);
    public void delStepFormalPara(String testId, List<Integer> stepIds);
    public void setParasName(Para newPara) throws Exception;
}