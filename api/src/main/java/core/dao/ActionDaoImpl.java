package core.dao;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.List;
import core.pojo.Action;

@Repository("ActionDao")
public class ActionDaoImpl implements ActionDao{
    @Autowired
    MongoTemplate mongoTemplate = null;

    public List<Action> getActions(){
        return mongoTemplate.findAll(Action.class);
    }
}