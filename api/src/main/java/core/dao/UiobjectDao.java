package core.dao;
import java.util.List;
import core.pojo.Uiobject;

public interface UiobjectDao{
    public Uiobject createUiobject(Uiobject newObj);
    public List<Uiobject> getObjectsByPage(String page);
    public List<Uiobject> searchObjectByPath(String path);
    public void updateObject(Uiobject obj);
}