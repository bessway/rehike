package core.service;

import java.util.Map;

import core.pojo.ObjectPojo;

public interface ObjectService{
    public Map<String,String> getAllObjects();
    public Boolean updateObject(ObjectPojo obj);
}