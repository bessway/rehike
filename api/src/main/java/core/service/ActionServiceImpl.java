package core.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.pojo.Action;
import core.dao.ActionDao;

@Service("ActionService")
public class ActionServiceImpl implements ActionService{
    @Autowired
    private ActionDao actionDao = null;

    public List<Action> getActions(){
        return actionDao.getActions();
    }
    public Action createAction(Action action) throws Exception{
        //regFunc不能重复
        Action result = actionDao.getActionByFunc(action.getRegFunc());
        if(result!=null && result.getRegFunc().equals(action.getRegFunc())){
            throw new Exception(action.getRegFunc()+"已经存在，不能重复创建");
        }
        actionDao.createAction(action);
        return actionDao.getActionByFunc(action.getRegFunc());
    }
}