package core.service;
import java.util.List;
import core.pojo.Action;

public interface ActionService{
    public List<Action> getActions();
    public Action createAction(Action action) throws Exception;
}