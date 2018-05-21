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

import core.pojo.StepDetailPojo;
import core.pojo.HierachyPojo;
import core.service.TestService;

@RestController
@RequestMapping("/1/test")
public class TestManager {
    @Autowired
    private TestService testService = null;
    private Gson gson = new Gson();

    @RequestMapping("/health")
    public String home() {
        return "Hello!";
    }

    @RequestMapping("/projects")
    public String getProjects(HttpServletResponse res) {
        return gson.toJson(testService.getProjects());
    }

    @RequestMapping("/nodes/{parentId}")
    public String getSubNodes(HttpServletResponse res, @PathVariable String parentId) {
        return gson.toJson(testService.getSubNodes(parentId));
    }

    @RequestMapping("/detail/{caseId}")
    public String getCaseDetail(HttpServletResponse res, @PathVariable String caseId) {
        return gson.toJson(testService.getCaseDetail(caseId,"default"));
    }

    @RequestMapping(value="/node",method=RequestMethod.POST)
    public String addCase(HttpServletResponse res, @RequestBody HierachyPojo node){
        return gson.toJson(testService.addNode(node));
    }
    @RequestMapping(value="/node/hierachy/{nodeId}",method=RequestMethod.PUT)
    public String updateParentNode(HttpServletResponse res,@PathVariable String nodeId, @RequestBody HierachyPojo parentNode){
        return gson.toJson(testService.updateNodeParentId(nodeId, parentNode));
    }

     //复制子节点
     @RequestMapping(value="/node/hierachy/{nodeId}",method=RequestMethod.POST)
     public String  CopyNode(HttpServletResponse res,@PathVariable String nodeId, @RequestBody HierachyPojo parentNode){
        return gson.toJson(testService.CopyNode(nodeId, parentNode));
     }


    @RequestMapping(value="/node/{nodeId}",method=RequestMethod.PUT)
    public String updateNodeName(HttpServletResponse res,@PathVariable String nodeId, @RequestBody HierachyPojo newName){
        return gson.toJson(testService.updateNodeName(nodeId, newName));
    }
    @RequestMapping(value="/node/step/{caseId}",method=RequestMethod.PUT)
    public String updateCaseStep(HttpServletResponse res,@PathVariable String caseId, @RequestBody List<StepDetailPojo> data){
        Boolean result=testService.updateCase(caseId,data);
        return "{result:"+result+"}";
    }
    
    @RequestMapping(value="/node/{nodeId}",method=RequestMethod.DELETE)
    public String deleteCase(HttpServletResponse res,@PathVariable String nodeId){
        Boolean result=testService.deleteNode(nodeId);

        return "{result:"+result+"}";
    }
    @RequestMapping(value="/cases",method=RequestMethod.POST)
    public String getCases(@RequestBody List<String> casesId){
        return gson.toJson(testService.getCases(casesId));
    }
}