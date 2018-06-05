package core.service;

import java.util.List;
import java.util.Map;

import core.pojo.CasePojo;
import core.pojo.HierachyPojo;
import core.pojo.StepDetailPojo;

public interface TestService{
    public List<HierachyPojo> getProjects();
    public List<HierachyPojo> getSubNodes(String parentId);
    public Map<String, Object> getCaseDetail(String caseId, String version);
    public HierachyPojo addNode(HierachyPojo node);
    public HierachyPojo updateNodeParentId(String nodeId, HierachyPojo parentNode);
    public HierachyPojo updateNodeName(String nodeId, HierachyPojo newName);
    public Boolean deleteNode(String nodeId);
    public List<String> getValidCases(List<String> toValidate);
    public Boolean findAllSubNodes(List<String> nodeIds,List<String> allCases);
    public Boolean updateCase(String nodeId, List<StepDetailPojo> steps);
    public List<CasePojo> getCases(List<String> casesId);
    public HierachyPojo copyNode(HierachyPojo nodeId, String targetNodeId);
    public List<HierachyPojo> copyNodes(String targetId,List<HierachyPojo> copiedNodes);
}