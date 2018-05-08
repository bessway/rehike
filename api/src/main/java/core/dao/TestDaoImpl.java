package core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import core.pojo.CasePojo;
import core.pojo.HierachyPojo;

@Repository("testDao")
public class TestDaoImpl{
    @Autowired
    private MongoTemplate mongoTemplate=null;
    public List<HierachyPojo> getAllProjects(){
        Query query=Query.query(Criteria.where("parentId").is(null));
        List<HierachyPojo> ret= mongoTemplate.find(query,HierachyPojo.class);

        return ret;
    }
    public List<HierachyPojo> getSubNodes(String parentId){
        Query query=Query.query(Criteria.where("parentId").is(parentId));
        List<HierachyPojo> ret= mongoTemplate.find(query,HierachyPojo.class);

        return ret;
    }
    public CasePojo getCaseDetail(String caseId){
        Query query=Query.query(Criteria.where("caseId").is(caseId));
        return mongoTemplate.findOne(query, CasePojo.class);
    }
}