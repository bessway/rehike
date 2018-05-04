package core.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.DBUtils;
import core.pojo.CasePojo;
import core.service.TestServiceImpl;

@RestController
@RequestMapping("/1/test")
public class TestManager {
    @Autowired
    TestServiceImpl testService=null;
    ArrayList<CasePojo> cases = null;
    Gson gson=new Gson();

    public void buildCase() throws Exception {
        this.cases = new Gson().fromJson(DBUtils.getData("case.json"), new TypeToken<ArrayList<CasePojo>>() {
        }.getType());
        for (CasePojo item : this.cases) {
            System.out.println(item.toString());
        }
    }
    /*
     * public static void main(String[] args) throws Exception { new
     * TestManager().buildCase(); }
     */

    @RequestMapping("/health")
    public String home() {
        return "Hello!";
    }
    @RequestMapping("/projects")
    public String getProjects(HttpServletResponse res){
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Content-Type", "application/json;charset=UTF-8");
        
        return gson.toJson(testService.getProjects());
    }
}