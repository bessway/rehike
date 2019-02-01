package core.controller;


import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import core.pojo.Uiobject;
import core.service.UiobjectService;

@RestController
@RequestMapping("/api/v2/objects")
public class UiobjectManager{
    @Autowired
    private UiobjectService uiObjService = null;
    private Gson gson = new Gson();
    
    @RequestMapping("/page/{page}")
    public String getObjectsByPage(HttpServletResponse res,@PathVariable String page){
        String ret = gson.toJson(uiObjService.getObjectsByPage(page));
        return ret;
    }

    @RequestMapping(value="/object",method=RequestMethod.POST)
    public String createObj(HttpServletResponse res, @RequestBody Uiobject newObj){
        String ret = gson.toJson(uiObjService.createObject(newObj));
        return ret;
    }

    @RequestMapping(value="/path",method=RequestMethod.POST)
    public List<Uiobject> searchObjByPath(HttpServletResponse res, @RequestBody String path){
        //String ret = gson.toJson(uiObjService.searchObjByPath(path));
        return uiObjService.searchObjByPath(path);
    }
    @RequestMapping(value="/object",method=RequestMethod.PUT)
    public String updateObj(HttpServletResponse res, @RequestBody Uiobject obj){
        uiObjService.updateObject(obj);
        String ret= "true";
        return ret;
    }
    @RequestMapping("/structedpage/{page}")
    public String getStructedObjByPage(HttpServletResponse res,@PathVariable String page){
        String ret = gson.toJson(uiObjService.getStructedObjByPage(page));
        return ret;
    }
}