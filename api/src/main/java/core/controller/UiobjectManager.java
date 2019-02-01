package core.controller;


import java.util.List;
import java.util.Map;

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
    
    @RequestMapping("/page/{page}")
    public List<Uiobject> getObjectsByPage(@PathVariable String page){
        return uiObjService.getObjectsByPage(page);
    }

    @RequestMapping(value="/object",method=RequestMethod.POST)
    public Uiobject createObj( @RequestBody Uiobject newObj){
        return uiObjService.createObject(newObj);
    }

    @RequestMapping(value="/path",method=RequestMethod.POST)
    public List<Uiobject> searchObjByPath(@RequestBody String path){
        return uiObjService.searchObjByPath(path);
    }
    @RequestMapping(value="/object",method=RequestMethod.PUT)
    public String updateObj( @RequestBody Uiobject obj){
        uiObjService.updateObject(obj);
        return "success";
    }
    @RequestMapping("/structedpage/{page}")
    public Map<String, Object> getStructedObjByPage(@PathVariable String page){
        return uiObjService.getStructedObjByPage(page);
    }
}