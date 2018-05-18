package core.controller;


import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.service.DataService;

@RestController
@RequestMapping("/1/data")
public class DataManager{
    @Autowired
    private DataService dataService=null;
    private Gson gson=new Gson();

    @RequestMapping("/global")
    public String getGlobalParas(HttpServletResponse res){
        return gson.toJson(this.dataService.getGlobalParas());
    }
}