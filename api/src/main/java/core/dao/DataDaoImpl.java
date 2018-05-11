package core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import core.pojo.CaseDataPojo;
import core.pojo.StepDataPojo;

@Repository("dataDao")
public class DataDaoImpl {
    @Autowired
    private MongoTemplate mongoTemplate = null;

    public Map<String, String> getGlobalParas() {
        Query query = Query.query(Criteria.where("caseId").is("global"));
        return mongoTemplate.findOne(query, Map.class, "data");
    }

    public CaseDataPojo getCaseData(String caseId, String version) {
        Query query = Query.query(Criteria.where("caseId").is(caseId).and("version").is(version));
        return mongoTemplate.findOne(query, CaseDataPojo.class);
    }
    public Boolean updateData(String caseId,String version,List<StepDataPojo> data){
        return true;
    }
    public Boolean updateGlobalParas(HashMap<String,String> para){
        return true;
    }
}