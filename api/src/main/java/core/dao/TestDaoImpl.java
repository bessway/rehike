package core.dao;

import java.util.List;

import com.mongodb.client.result.DeleteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.CasePojo;
import core.pojo.HierachyPojo;
import core.pojo.StepPojo;

@Repository("testDao")
public class TestDaoImpl {
    @Autowired
    private MongoTemplate mongoTemplate = null;

    public List<HierachyPojo> getAllProjects() {
        Query query = Query.query(Criteria.where("parentId").is(null));
        List<HierachyPojo> ret = mongoTemplate.find(query, HierachyPojo.class);

        return ret;
    }

    public List<HierachyPojo> getSubNodes(String parentId) {
        Query query = Query.query(Criteria.where("parentId").is(parentId));
        List<HierachyPojo> ret = mongoTemplate.find(query, HierachyPojo.class);

        return ret;
    }

    public CasePojo getCaseDetail(String caseId) {
        Query query = Query.query(Criteria.where("caseId").is(caseId));
        return mongoTemplate.findOne(query, CasePojo.class);
    }

    public Boolean deleteSubNodes(String parentId) {
        Query query = Query.query(Criteria.where("parentId").is(parentId));
        mongoTemplate.remove(query, "hierachy");
        return true;
    }

    public Boolean deleteNode(String nodeId) {
        Query query = Query.query(Criteria.where("refId").is(nodeId));
        DeleteResult ret = mongoTemplate.remove(query, "hierachy");
        return true;
    }

    public void addNode(HierachyPojo node) {
        mongoTemplate.insert(node);
    }

    public HierachyPojo getNodeByLabel(String label) {
        Query query = Query.query(Criteria.where("label").is(label));
        return mongoTemplate.findOne(query, HierachyPojo.class);
    }

    public HierachyPojo updateParentNode(String nodeId, String targetNodeId) {
        Query query = Query.query(Criteria.where("_id").is(nodeId));
        Update update = Update.update("parentId", targetNodeId);
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(false),
                HierachyPojo.class);
    }
    public HierachyPojo updateCaseName(String nodeId, String newName) {
        Query query = Query.query(Criteria.where("_id").is(nodeId));
        Update update = Update.update("label", newName);
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(false),
                HierachyPojo.class);
    }
    public Boolean updateCaseSteps(String caseId,List<StepPojo> steps){
        return true;
    }
}