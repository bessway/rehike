package core.controller;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.service.KeyService;

@RestController
@RequestMapping("/1/action")
public class KeyManager{
    private Gson gson=new Gson();
    @Autowired
    private KeyService keyService=null;
    
    @RequestMapping("/all")
    public String getAllKeys(HttpServletResponse res){   
        return gson.toJson(keyService.getAllKeys());
    }
}