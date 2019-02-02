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
                query = Query.query(Criteria.where("paraId").is(item.getParaId()).and("testId").is(item.getTestId()).and("stepId").is(item.getStepId()));
            }else{
                query = Query.query(Criteria.where("paraId").is(item.getParaId()).and("testId").is(item.getTestId()));
            }
            
            Update update = Update.update("paraValue", item.getParaValue());
            bulkops.updateOne(query, update);
        });
        bulkops.execute();
    }
    public List<Para> getParasByTest(String testId){
        Query query = Query.query(Criteria.where("testId").is(testId).and("refTestId").is(null));
        List<Para> ret = mongoTemplate.find(query, Para.class);
        return ret;
    }
}