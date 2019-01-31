package core.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import core.pojo.Step;
import core.pojo.Test;
import core.pojo.TestDetail;

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
        List<Test> ret=testService.getTestsByParentId(parentId);
        return this.gson.toJson(ret);
    }

    @RequestMapping(value="/test",method=RequestMethod.POST)
    public String addNewTest(HttpServletResponse res, @RequestBody Test newTest){
        return gson.toJson(testService.createTest(newTest));
    }

    @RequestMapping(value="/testdetail",method=RequestMethod.PUT)
    public String saveTestDetail(HttpServletResponse res, @RequestBody Test test){
        testService.saveTest(test);
        return "true";
    }
}