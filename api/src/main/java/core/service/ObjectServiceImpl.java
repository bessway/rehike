package core.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.ObjectDaoImpl;
import core.pojo.ObjectPojo;
import utils.Utils;

@Service("ObjectService")
public class ObjectServiceImpl{
    @Autowired
    private ObjectDaoImpl objDao=null;

    public HashMap<String,String> getAllObjects(){
        List<ObjectPojo> objs=objDao.getAllObjects();
        HashMap<String,String> result=new HashMap<String,String>(); 
        for(ObjectPojo item:objs){
            String key=item.getPage()+"."+item.getType()+"."+item.getName();
            result.put(key,item.getXpath());
        }
        Utils.objectsMap=result;
        return result;
    }
    public Boolean updateObject(ObjectPojo obj){
        return objDao.updateObject(obj);
    }
}