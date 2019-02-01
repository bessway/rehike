package core.dao;
import java.util.List;
import core.pojo.Uiobject;

public interface UiobjectDao{
    public void createUiobject(Uiobject newObj);
    public Uiobject findObjByName(String page,String type,String name);
    public List<Uiobject> getObjectsByPage(String page);
    public List<Uiobject> searchObjectByPath(String path);
    public void updateObject(Uiobject obj);
}