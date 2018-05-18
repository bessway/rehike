package core.service;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.ObjectDao;
import core.pojo.ObjectPojo;
import utils.Utils;

@Service("ObjectService")
public class ObjectServiceImpl implements ObjectService{
    @Autowired
    private ObjectDao objDao=null;

    public Map<String,String> getAllObjects(){
        List<ObjectPojo> objs=objDao.getAllObjects();
        Map<String,String> result=new Hashtable<String,String>(); 
        for(ObjectPojo item:objs){
            String key=item.getPage()+"."+item.getType()+"."+item.getName();
            result.put(key,item.getXpath());
        }
        Utils.objectsMap=(Hashtable<String,String>)result;
        return result;
    }
    public Boolean updateObject(ObjectPojo obj){
        return objDao.updateObject(obj);
    }
}