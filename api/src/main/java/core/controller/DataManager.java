package core.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.DBUtils;
import core.pojo.CaseDataPojo;
import core.service.DataServiceImpl;

@RestController
@RequestMapping("/1/data")
public class DataManager{
    @Autowired
    private DataServiceImpl dataService=null;
    private Gson gson=new Gson();

    @RequestMapping("/global")
    public String getGlobalParas(HttpServletResponse res){
        return gson.toJson(this.dataService.getGlobalParas());
    }
}