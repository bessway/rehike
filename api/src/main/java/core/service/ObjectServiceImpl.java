package core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.ObjectDaoImpl;
import core.pojo.ObjectPojo;
import utils.Utils;

@Service("ObjectService")
public class ObjectServiceImpl{
    @Autowired
    private ObjectDaoImpl project=null;

    public List<ObjectPojo> getAllObjects(){
        if(Utils.objects!=null){
            return Utils.objects;
        }
        List<ObjectPojo> ret=project.getAllObjects();
        Utils.cacheObject(ret);
        return ret;
    }
}