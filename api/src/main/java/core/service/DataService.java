package core.service;

import java.util.List;

import core.pojo.CaseDataPojo;

public interface DataService{
    public List<String> getGlobalParas();
    public CaseDataPojo getCaseData(String caseId,String version);
    public Boolean updateData(String caseId,String version,CaseDataPojo data);
    public Boolean updateGlobalParas(List<String> para);
    public Boolean deleteMultipleCasesData(List<String> caseIds);
    public List<CaseDataPojo> getCasesData(List<String> casesId,String version);
}