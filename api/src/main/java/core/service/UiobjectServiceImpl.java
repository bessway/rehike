package core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.UiobjectDao;
import core.pojo.Uiobject;
import utils.Utils;

@Service("UiObjectService")
public class UiobjectServiceImpl implements UiobjectService{
    @Autowired
    private UiobjectDao objDao = null;

    public List<Uiobject> getObjectsByPage(String page){
        return objDao.getObjectsByPage(page);
    }
    public List<Uiobject> searchObjByPath(String path){
        return objDao.searchObjectByPath(Utils.escapeExprSpecialWord(path));
    }
    public Uiobject createObject(Uiobject newObj){
        return objDao.createUiobject(newObj);
    }
    public void updateObject(Uiobject obj){
        objDao.updateObject(obj);
    }
    public Map<String, Object> getStructedObjByPage(String page){
        List<Uiobject> objs = this.getObjectsByPage(page);
        Map<String, Object> ret = null;
        if(objs!=null && objs.size()>0){
            ret = new HashMap<String, Object>();
            for(Uiobject item: objs){
                this.addToStructObjects(ret, item);
            }
        }
        return ret;
    }
    private void addToStructObjects(Map<String, Object>ret, Uiobject obj){
        Map<String,Object> temp = ret;
        if(!temp.containsKey(obj.getUiObjectPage())){
            temp.put(obj.getUiObjectPage(), new HashMap<String, Object>());
        }
        temp = (Map<String,Object>)temp.get(obj.getUiObjectPage());
        if(!temp.containsKey(obj.getUiObjectType())){
            temp.put(obj.getUiObjectType(), new HashMap<String, Object>());
        }
        temp = (Map<String,Object>)temp.get(obj.getUiObjectType());
        if(!temp.containsKey(obj.getUiObjectName())){
            temp.put(obj.getUiObjectName(), new HashMap<String, String>());
        }
        temp = (Map<String,Object>)temp.get(obj.getUiObjectName());
        temp.put("path",obj.getUiObjectPath());
        temp.put("objId",obj.getUiObjectId());
    }
}