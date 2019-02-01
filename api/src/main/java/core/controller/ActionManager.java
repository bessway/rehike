package core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.pojo.Action;
import core.service.ActionService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/actions")
public class ActionManager{
    @Autowired
    private ActionService actionService = null;

    @RequestMapping("/all")
    public List<Action> getAllActions(){
        return actionService.getActions();
    }
}