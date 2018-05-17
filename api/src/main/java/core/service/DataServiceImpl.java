package core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.DataDao;
import core.pojo.CaseDataPojo;

@Service("DataService")
public class DataServiceImpl implements DataService{
    @Autowired
    private DataDao dataDao=null;

    public List<String> getGlobalParas(){
        Map<String,Object> ret=this.dataDao.getGlobalParas();
        if(ret.get("paras")!=null){
            return (List<String>)ret.get("paras");
        }
        return new ArrayList<String>();
    }
    public CaseDataPojo getCaseData(String caseId,String version){
        return dataDao.getCaseData(caseId, version);
    }
    public Boolean updateData(String caseId,String version,CaseDataPojo data){
        return dataDao.updateData(caseId, version, data);
    }
    public Boolean updateGlobalParas(List<String> para){
        Map<String,Object> ret=this.dataDao.getGlobalParas();
        if(ret.get("paras")!=null){
            para.addAll((List<String>)ret.get("paras"));
        }
        ret=new HashMap<String,Object>();
        for(String item:para){
            ret.put(item,null);
        }
        return dataDao.updateGlobalParas(ret.keySet());
    }
    public Boolean deleteMultipleCasesData(List<String> caseIds){
        return dataDao.deleteMultipleCasesData(caseIds);
    }
    public List<CaseDataPojo> getCasesData(List<String> casesId,String version){
        return dataDao.getCasesData(casesId, version);
    }
}