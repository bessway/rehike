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
}