package core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.ObjectPojo;

@Repository("ObjectDao")
public class ObjectDaoImpl implements ObjectDao{
    @Autowired
    private MongoTemplate mongoTemplate = null;

    public List<ObjectPojo> getAllObjects() {
        return mongoTemplate.findAll(ObjectPojo.class);
    }

    public Boolean updateObject(ObjectPojo newObj) {
        Query query = Query.query(Criteria.where("page").is(newObj.getPage()).and("type").is(newObj.getType())
                .and("name").is(newObj.getName()));
        Update update=Update.update("xpath", newObj.getXpath());
        mongoTemplate.upsert(query, update, ObjectPojo.class);
        return true;
    }
}