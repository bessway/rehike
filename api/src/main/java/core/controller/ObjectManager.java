package core.controller;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.service.ObjectService;

@RestController
@RequestMapping("/1/object")
public class ObjectManager{
    @Autowired
    private ObjectService objService=null;
    Gson gson=new Gson();

    @RequestMapping("/all")
    public String getAllObjects(HttpServletResponse res){
        return gson.toJson(objService.getAllObjects());
    }
}