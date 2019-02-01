package core.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import core.pojo.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import core.service.TestService;

@RestController
@RequestMapping("/api/v2/tests")
public class TestManager {
    @Autowired
    private TestService testService = null;
    private Gson gson = new Gson();

    @RequestMapping(value="/{parentId}",method=RequestMethod.GET)
    public String getTestsByParentId(HttpServletResponse res, @PathVariable String parentId){
        String ret = this.gson.toJson(testService.getTestsByParentId(parentId));
        return ret;
    }

    @RequestMapping(value="/test",method=RequestMethod.POST)
    public String addNewTest(HttpServletResponse res, @RequestBody Test newTest){
        String ret = gson.toJson(testService.createTest(newTest));
        return ret;
    }

    @RequestMapping(value="/testdetail",method=RequestMethod.PUT)
    public String saveTestDetail(HttpServletResponse res, @RequestBody Test test){
        testService.saveTest(test);
        String ret = "true";
        return ret;
    }

    @RequestMapping(value="/public",method=RequestMethod.POST)
    public String searchPublicTest(HttpServletResponse res, @RequestBody String key){
        String ret = gson.toJson(testService.searchPublicTest(key));
        return ret;
    }
    /*暂不实现*/
    @RequestMapping(value="/testdetail/{testId}")
    public String getTestDetail(HttpServletResponse res, @PathVariable String testId){

        return null;
    }
}