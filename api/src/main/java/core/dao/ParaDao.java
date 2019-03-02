package core.dao;

import java.util.List;

import core.pojo.Para;

public interface ParaDao{
    public void createPara(Para newPara);
    public Para findParaById(Para p);
    public List<Para> getFormalParas(String testId);
    public void bulkCreatePara(List<Para> paras);
    public List<Para> findStepParas(String testId, Integer stepId);
    public void bulkSetParasFormal(String testId, List<Long> paraId);
    public void bulkSetParasValue(List<Para> paras);
    public List<Para> getParasByTest(String testId, String dataVersion);
    public List<Para> getRefParasByTest(String testId, Integer stepId, String dataVersion);
    public List<Para> getParasByTestWithRef(String testId, String dataVersion);
    public Para findTestParaByName(String testId, String paraName, String valueVersion);
    public void delStepFormalPara(String testId, List<Integer> stepIds);
    public void setParasName(String testId,Long paraId, String paraName);
}