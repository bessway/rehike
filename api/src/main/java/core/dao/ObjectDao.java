package core.dao;

import java.util.List;

import core.pojo.ObjectPojo;

public interface ObjectDao{
    public List<ObjectPojo> getAllObjects();
    public Boolean updateObject(ObjectPojo newObj);
}