package core.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.client.result.DeleteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.CaseDataPojo;
import core.pojo.StepDataPojo;

@Repository("dataDao")
public class DataDaoImpl {
    @Autowired
    private MongoTemplate mongoTemplate = null;

    public Map<String,Object> getGlobalParas() {
        Query query = Query.query(Criteria.where("caseId").is("global"));
        return mongoTemplate.findOne(query, Map.class, "data");
    }

    public CaseDataPojo getCaseData(String caseId, String version) {
        Query query = Query.query(Criteria.where("caseId").is(caseId).and("version").is(version));
        return mongoTemplate.findOne(query, CaseDataPojo.class);
    }
    public Boolean updateData(String caseId,String version,CaseDataPojo data){
        Query query = Query.query(Criteria.where("caseId").is(caseId).and("version").is(version));
        Update update = Update.update("stepsData", data.getStepsData()).set("sharedParas", data.getSharedParas());
        mongoTemplate.upsert(query, update, CaseDataPojo.class);
        return true;
    }
    public Boolean updateGlobalParas(Set<String> para){
        Query query = Query.query(Criteria.where("caseId").is("global"));
        Update update = Update.update("paras", para);
        mongoTemplate.updateFirst(query, update, "data");
        return true;
    }
    public Boolean deleteMultipleCasesData(List<String> caseIds){
        Query query = Query.query(Criteria.where("caseId").in(caseIds));
        DeleteResult ret=mongoTemplate.remove(query,"data");
        return true;
    }
}