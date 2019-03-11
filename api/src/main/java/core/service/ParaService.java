package core.service;

import java.util.List;
import java.util.Map;

import core.pojo.Para;


public interface ParaService{
    public Para createTestPara(Para newPara) throws Exception;
    public List<Para> copyRefParasToStep(String srcTestId, String tarTestId, List<Long> stepId);
    public void setParasFormal(List<Para> paras);
    public void setParasValue(List<Para> paras);
    public List<Para> getTestParas(String testId,String dataVersion);
    public List<Para> getTestRefParas(String testId, List<Long> stepId,String dataVersion);
    public List<Para> getTestParasWithRef(String testId,String dataVersion);
    public void delStepFormalPara(String testId, List<Long> stepIds);
    public void setParasName(Para newPara) throws Exception;
    public void delParas(List<Para> paras) throws Exception;
    public Map<Long,Long> copyAllParas(String oldTestId,String newTestId);
    public void delNouseParaInTest(String testId, List<Long> ids);
}