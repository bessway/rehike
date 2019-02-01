package core.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
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
    //private Gson gson = new Gson();

    @RequestMapping(value="/{parentId}",method=RequestMethod.GET)
    public List<Test> getTestsByParentId( @PathVariable String parentId){
        //String ret = this.gson.toJson(testService.getTestsByParentId(parentId));
        //return ret;
        return testService.getTestsByParentId(parentId);
    }

    @RequestMapping(value="/test",method=RequestMethod.POST)
    public Test addNewTest( @RequestBody Test newTest){
        //String ret = gson.toJson(testService.createTest(newTest));
        //return ret;
        return testService.createTest(newTest);
    }

    @RequestMapping(value="/testdetail",method=RequestMethod.PUT)
    public String saveTestDetail( @RequestBody Test test){
        testService.saveTest(test);
        return "success";
        //String ret = "true";
        //return ret;
    }

    @RequestMapping(value="/public",method=RequestMethod.POST)
    public List<Test> searchPublicTest( @RequestBody String key){
        //String ret = gson.toJson(testService.searchPublicTest(key));
        //return ret;
        return testService.searchPublicTest(key);
    }
    /*暂不实现*/
    @RequestMapping(value="/testdetail/{testId}")
    public String getTestDetail( @PathVariable String testId){

        return null;
    }
}