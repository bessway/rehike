package core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import core.service.ActionService;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api/v2/actions")
public class ActionManager{
    @Autowired
    private ActionService actionService = null;
    private Gson gson=new Gson();

    @RequestMapping("/all")
    public String getAllActions(){
        String ret = gson.toJson(actionService.getActions());
        return ret;
    }
}