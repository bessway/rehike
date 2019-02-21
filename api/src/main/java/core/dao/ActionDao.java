package core.dao;
import java.util.List;
import core.pojo.Action;

public interface ActionDao{
    public List<Action> getActions();
    public void createAction(Action action);
    public Action getActionByFunc(String name);
}