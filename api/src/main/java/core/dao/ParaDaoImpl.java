package core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.Para;

@Repository("ParaDao")
public class ParaDaoImpl implements ParaDao{
    @Autowired
    private MongoTemplate mongoTemplate = null;

    public void createPara(Para newPara){
        mongoTemplate.insert(newPara);
    }
    public Para findParaById(Para p){
        Query query = Query.query(Criteria.where("paraId").is(p.getParaId()));
        Para ret = mongoTemplate.findOne(query, Para.class);
        return ret;
    }
    public List<Para> getFormalParas(String testId){
        Query query = Query.query(Criteria.where("testId").is(testId).and("isFormalPara").is(1));
        List<Para> ret = mongoTemplate.find(query, Para.class);
        return ret;
    }
    public void bulkCreatePara(List<Para> paras){
        mongoTemplate.insertAll(paras);
    }
    public List<Para> findStepParas(String testId, Integer stepId){
        Query query = Query.query(Criteria.where("testId").is(testId).and("stepId").is(stepId));
        List<Para> ret = mongoTemplate.find(query, Para.class);
        return ret;
    }
    public void bulkSetParasFormal(String testId, List<Long> paraId){
        Query query = Query.query(Criteria.where("testId").is(testId).and("refTestId").is(null).and("paraId").in(paraId));
        Update update = Update.update("isFormalPara", 1);
        mongoTemplate.updateMulti(query, update, Para.class);
    }
    public void bulkSetParasValue(List<Para> paras){
        BulkOperations bulkops = mongoTemplate.bulkOps(BulkMode.UNORDERED, Para.class);
        paras.forEach(item->{
            Query query = null;
            if(item.getRefTestId()!=null && !"".equals(item.getRefTestId())){
                query = Query.query(Criteria.where("paraId").is(item.getParaId())
                    .and("testId").is(item.getTestId()).and("stepId").is(item.getStepId())
                    .and("dataVersion").is(item.getDataVersion()));
            }else{
                query = Query.query(Criteria.where("paraId").is(item.getParaId())
                    .and("testId").is(item.getTestId()).and("dataVersion").is(item.getDataVersion()));
            }
            
            Update update = Update.update("paraValue", item.getParaValue());
            bulkops.updateOne(query, update);
        });
        bulkops.execute();
    }
    public List<Para> getParasByTest(String testId, String dataVersion){
        Query query = Query.query(Criteria.where("testId").is(testId).and("refTestId").is(null).and("dataVersion").is(dataVersion));
        List<Para> ret = mongoTemplate.find(query, Para.class);
        return ret;
    }
    public List<Para> getRefParasByTest(String testId, Integer stepId ,String dataVersion){
        Query query = Query.query(Criteria.where("testId").is(testId).and("stepId").is(stepId).and("dataVersion").is(dataVersion));
        List<Para> ret = mongoTemplate.find(query, Para.class);
        return ret;
    }
    public List<Para> getParasByTestWithRef(String testId, String dataVersion){
        Query query = Query.query(Criteria.where("testId").is(testId).and("dataVersion").is(dataVersion));
        List<Para> ret = mongoTemplate.find(query, Para.class);
        return ret;
    }
    public Para findTestParaByName(String testId, String paraName, String valueVersion){
        Query query = Query.query(Criteria.where("testId").is(testId).and("dataVersion").is(valueVersion).and("paraName").is(paraName).and("refTestId").is(null));
        Para ret = mongoTemplate.findOne(query, Para.class);
        return ret;
    }
    public void delStepFormalPara(String testId, List<Integer> stepIds){
        Query query = Query.query(Criteria.where("testId").is(testId).and("stepId").in(stepIds).and("refTestId").ne(null));
        mongoTemplate.remove(query, Para.class);
    }
    public void setParasName(String testId,Long paraId, String paraName){
        Query query = Query.query(Criteria.where("paraId").is(paraId).and("testId").is(testId));
        Update update = Update.update("paraName", paraName);
        mongoTemplate.updateMulti(query, update, Para.class);
    }
    public void delParas(String testId, List<Long> paraIds){
        Query query = Query.query(Criteria.where("paraId").in(paraIds).and("testId").is(testId));
        mongoTemplate.remove(query, Para.class);
    }
    public void delParasFromRefTest(String testId, List<Long> paraIds){
        Query query = Query.query(Criteria.where("paraId").in(paraIds).and("refTestId").is(testId));
        mongoTemplate.remove(query, Para.class);
    }
}