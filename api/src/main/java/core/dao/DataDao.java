package core.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import core.pojo.CaseDataPojo;

public interface DataDao{
    public Map<String,Object> getGlobalParas();
    public CaseDataPojo getCaseData(String caseId, String version);
    public Boolean updateData(String caseId,String version,CaseDataPojo data);
    public Boolean updateGlobalParas(Set<String> para);
    public Boolean deleteMultipleCasesData(List<String> caseIds);
    public List<CaseDataPojo> getCasesData(List<String> casesId,String version);
}