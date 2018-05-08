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
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Content-Type", "application/json;charset=UTF-8");
        return gson.toJson(this.dataService.getGlobalParas());
    }



    
    private ArrayList<CaseDataPojo> casedatas=null;
    public ArrayList<CaseDataPojo> getCaseDatas() throws Exception{
        this.casedatas=new Gson().fromJson(DBUtils.getData("casedata.json"), new TypeToken<ArrayList<CaseDataPojo>>() {
        }.getType());
        for(CaseDataPojo item:this.casedatas){
            System.out.println(item.toString());
        }
        return this.casedatas;
    }
}