package core.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.DataDaoImpl;
import core.pojo.CaseDataPojo;

@Service("DataService")
public class DataServiceImpl{
    @Autowired
    private DataDaoImpl project=null;
    public Map<String,String> getGlobalParas(){
        Map<String,String> ret=this.project.getGlobalParas();
        ret.remove("_id");
        ret.remove("caseId");
        return ret;
    }
    public CaseDataPojo getCaseData(String caseId,String version){
        return project.getCaseData(caseId, version);
    }
}