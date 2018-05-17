package core.dao;

import java.util.List;

import core.pojo.CasePojo;
import core.pojo.HierachyPojo;
import core.pojo.StepPojo;

public interface TestDao{
    public List<HierachyPojo> getAllProjects();
    public List<HierachyPojo> getSubNodes(String parentId);
    public List<HierachyPojo> getSubNodes(List<String> parentIds);
    public CasePojo getCaseDetail(String caseId);
    public Boolean deleteMultipleNodes(List<String> nodeIds);
    public Boolean deleteMultipleCases(List<String> caseIds);
    public void addNode(HierachyPojo node);
    public HierachyPojo getNodeByLabel(String label);
    public HierachyPojo updateParentNode(String nodeId, String targetNodeId);
    public HierachyPojo updateCaseName(String nodeId, String newName);
    public Boolean updateCaseSteps(String caseId,List<StepPojo> steps);
    public List<String> getValidCases(List<String> toValidate);
    public List<CasePojo> getCases(List<String> casesId);
}