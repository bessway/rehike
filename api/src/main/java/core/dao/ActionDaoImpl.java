package core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.List;
import core.pojo.Action;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Repository("ActionDao")
public class ActionDaoImpl implements ActionDao{
    @Autowired
    MongoTemplate mongoTemplate = null;

    public List<Action> getActions(){
        return mongoTemplate.findAll(Action.class);
    }
    public Action getActionByName(String name){
        Query query=Query.query(Criteria.where("regFunc").gt(name));
        return mongoTemplate.findOne(query, Action.class);
    }
    public void createAction(Action action){
        mongoTemplate.insert(action);
    }
}