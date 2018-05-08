package core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import core.pojo.KeyPojo;

@Repository("keyDao")
public class KeyDaoImpl{
    @Autowired
    private MongoTemplate mongoTemplate=null;
    public List<KeyPojo> getAllKeys(){
        List<KeyPojo> ret=mongoTemplate.findAll(KeyPojo.class);
        return ret;
    }
}