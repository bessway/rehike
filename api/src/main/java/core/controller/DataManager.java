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

import core.pojo.CaseDataPojo;
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
    @RequestMapping(value="/casesdata/version/{version}",method=RequestMethod.POST)
    public String getCasesData(@PathVariable String version,@RequestBody List<String> cases){
        List<CaseDataPojo> result=dataService.getCasesData(cases, version);
        return gson.toJson(result);
    }
}