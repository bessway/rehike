package core.controller;

import java.util.List;

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

    @RequestMapping(value="/{parentId}",method=RequestMethod.GET)
    public List<Test> getTestsByParentId( @PathVariable String parentId){
        return testService.getTestsByParentId(parentId);
    }

    @RequestMapping(value="/test",method=RequestMethod.POST)
    public Test addNewTest( @RequestBody Test newTest){
        return testService.createTest(newTest);
    }

    @RequestMapping(value="/testdetail",method=RequestMethod.PUT)
    public String saveTestDetail( @RequestBody Test test){
        testService.saveTest(test);
        return "success";
    }

    @RequestMapping(value="/public",method=RequestMethod.POST)
    public List<Test> searchPublicTest( @RequestBody String key){
        return testService.searchPublicTest(key);
    }

    @RequestMapping(value="/testdetail/{testId}")
    public Test getTestDetail( @PathVariable String testId){
        return testService.getTestDetail(testId) ;
    }
}