package core.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import core.pojo.Uiobject;

public interface UiobjectService{
    public List<Uiobject> getObjectsByPage(String page);
    public Uiobject createObject(Uiobject newObj) throws Exception;
    public List<Uiobject> searchObjByPath(String path);
    public void updateObject(Uiobject obj) throws Exception;
    public Map<String, Object> getStructedObjByPage(String page);
    public Set<String> getPages();
    public Uiobject getObjectById(String objId);
    public List<Uiobject> getPageObjById(String objId);
}