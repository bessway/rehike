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
}