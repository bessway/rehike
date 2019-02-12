package core.dao;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.Uiobject;

@Repository("UiobjectDao")
public class UiobjectDaoImpl implements UiobjectDao{
    @Autowired
    MongoTemplate mongoTemplate = null;

    public void createUiobject(Uiobject newObj){
        mongoTemplate.insert(newObj);
        
    }
    public Uiobject findObjByName(String page,String type,String name){
        Query query = Query.query(Criteria.where("uiObjectPage").is(page).and("uiObjectType").is(type).and("uiObjectName").is(name));
        return mongoTemplate.findOne(query, Uiobject.class);
    }
    public List<Uiobject> getObjectsByPage(String page){
        Query query = Query.query(Criteria.where("uiObjectPage").is(page));
        return mongoTemplate.find(query, Uiobject.class);
    }
    public List<Uiobject> searchObjectByPath(String path){
        Pattern p = Pattern.compile("[\\s\\S]*"+path+"[\\s\\S]*", Pattern.CASE_INSENSITIVE);
        Query query = Query.query(Criteria.where("uiObjectPath").regex(p));
        return mongoTemplate.find(query, Uiobject.class);
    }
    public void updateObject(Uiobject obj){
        Query query = Query.query(Criteria.where("_id").is(obj.getUiObjectId()));
        Update update = Update.update("uiObjectPage", obj.getUiObjectPage());
        update.set("uiObjectType", obj.getUiObjectType());
        update.set("uiObjectName", obj.getUiObjectName());
        update.set("uiObjectPath", obj.getUiObjectPath());
        mongoTemplate.findAndModify(query, update, Uiobject.class);
    }
    public List<Uiobject> getPages(){
        Query query = new Query();
        query.fields().include("uiObjectPage");
        query.fields().exclude("_id");
        return mongoTemplate.find(query, Uiobject.class);
    }
    public Uiobject getObjectById(String objId){
        Query query = Query.query(Criteria.where("_id").is(objId));
        return mongoTemplate.findOne(query, Uiobject.class);
    }
}