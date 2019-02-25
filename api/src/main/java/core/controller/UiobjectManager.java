package core.controller;


import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @RequestMapping(value="/uiobject",method=RequestMethod.POST)
    public Uiobject createObj( @RequestBody Uiobject newObj) throws Exception{
        return uiObjService.createObject(newObj);
    }

    @RequestMapping(value="/path",method=RequestMethod.POST)
    public List<Uiobject> searchObjByPath(@RequestBody String path){
        return uiObjService.searchObjByPath(path);
    }

    @RequestMapping(value="/uiobject",method=RequestMethod.PUT)
    public String updateObj( @RequestBody Uiobject obj) throws Exception{
        uiObjService.updateObject(obj);
        return "success";
    }

    @RequestMapping("/structedpage/{page}")
    public Map<String, Object> getStructedObjByPage(@PathVariable String page){
        return uiObjService.getStructedObjByPage(page);
    }

    @RequestMapping(value="/pages", method=RequestMethod.GET)
    public Set<String> getPages() {
        return uiObjService.getPages();
    }

    @RequestMapping("/uiobject/{objId}")
    public Uiobject getObjectById(@PathVariable String objId){
        return uiObjService.getObjectById(objId);
    }

    @RequestMapping("/uiobject/pageobjects/{objId}")
    public List<Uiobject> getPageObjById(@PathVariable String objId){
        return uiObjService.getPageObjById(objId);
    }
}