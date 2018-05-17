package core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import core.pojo.KeyPojo;

@Repository("KeyDao")
public class KeyDaoImpl implements KeyDao{
    @Autowired
    private MongoTemplate mongoTemplate=null;
    public List<KeyPojo> getAllKeys(){
        List<KeyPojo> ret=mongoTemplate.findAll(KeyPojo.class);
        return ret;
    }
}