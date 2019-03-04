package core.controller;

import java.util.List;
import java.util.ArrayList;

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

    @RequestMapping(method=RequestMethod.POST)
    public List<Test> getTestsDetail( @RequestBody List<String> testIds){
        return testService.getTests(testIds);
    }

    @RequestMapping(value="/allsub")
    public List<String> getSubTests(){
        List<String> testIds = new ArrayList<String>();
        testIds.add("5c637a135eeb2960ac1fcc6b");
        return testService.findAllExecutableTests(testIds) ;
    }
    
    @RequestMapping(value="/test/copy",method=RequestMethod.POST)
    public Test copyTest(@RequestBody Test test){
        return testService.copyOneTest(test);
    }
}