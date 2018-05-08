package core.controller;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.service.KeyServiceImpl;

@RestController
@RequestMapping("/1/action")
public class KeyManager{
    private Gson gson=new Gson();
    @Autowired
    private KeyServiceImpl keyService=null;
    
    @RequestMapping("/all")
    public String getAllKeys(HttpServletResponse res){
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Content-Type", "application/json;charset=UTF-8");
        
        return gson.toJson(keyService.getAllKeys());
    }
}