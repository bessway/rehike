package core.dao;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.Test;

@Repository("TestDao")
public class TestDaoImpl implements TestDao{
    @Autowired
    private MongoTemplate mongoTemplate = null;

    public List<Test> getTestsByParentId(String parentId){
        Query query = Query.query(Criteria.where("parentId").is(parentId));
        List<Test> ret = mongoTemplate.find(query, Test.class);

        return ret;
    }
    public List<Test> getTestsByDesc(String testDesc){
        Query query = Query.query(Criteria.where("testDesc").is(testDesc));
        List<Test> ret = mongoTemplate.find(query, Test.class);

        return ret;
    }
    public void createTest(Test newTest){
        mongoTemplate.insert(newTest);
    }
    public Test updateTestIndex(String testId, String key, Long value){
        Query query = Query.query(Criteria.where("_id").is(testId));
        Update update = Update.update(key, value);
        return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Test.class);
    }
    public void updateTest(Test t){
        Query query = Query.query(Criteria.where("_id").is(t.getTestId()));
        Update update = Update.update("testDesc", t.getTestDesc());
        update.set("steps", t.getSteps());
        mongoTemplate.findAndModify(query, update, Test.class);
    }
}