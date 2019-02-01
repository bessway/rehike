package core.service;

import java.util.List;

import core.pojo.Para;


public interface ParaService{
    public Para createTestPara(Para newPara);
    public List<Para> copyRefParasToStep(String srcTestId, String tarTestId, Integer stepId);
    public void setParasFormal(List<Para> paras);
    public void setParasValue(List<Para> paras);
}