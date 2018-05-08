package core.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import utils.DBUtils;
import core.pojo.CaseDataPojo;
import core.pojo.CaseDetailPojo;
import core.pojo.CasePojo;
import core.pojo.StepPojo;
import core.service.DataServiceImpl;
import core.service.TestServiceImpl;

@RestController
@RequestMapping("/1/test")
public class TestManager {
    @Autowired
    private TestServiceImpl testService=null;
    @Autowired
    private DataServiceImpl dataService=null;
    private ArrayList<CasePojo> cases = null;
    private Gson gson=new Gson();

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
    @RequestMapping("/nodes/{parentId}")
    public String getSubNodes(HttpServletResponse res,@PathVariable String parentId){
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Content-Type", "application/json;charset=UTF-8");
        
        return gson.toJson(testService.getSubNodes(parentId));
    }
    @RequestMapping("/detail/{caseId}")
    public String getCaseDetail(HttpServletResponse res,@PathVariable String caseId){
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Content-Type", "application/json;charset=UTF-8");

        CasePojo casz=testService.getCaseDetail(caseId);
        CaseDataPojo data=dataService.getCaseData(caseId, "default");
        return gson.toJson(wrapCaseData(casz,data));
    }
    public HashMap<String,Object> wrapCaseData(CasePojo casz, CaseDataPojo data){
        HashMap<String,Object> result=new HashMap<String,Object>();
        ArrayList<CaseDetailPojo> steps=new ArrayList<CaseDetailPojo>();
        for(int i=0;i<casz.getSteps().size();i++){
            CaseDetailPojo tmp=new CaseDetailPojo();
            tmp.setAction(casz.getSteps().get(i).getKey());
            tmp.setid(casz.getSteps().get(i).getIndex());
            if(data.getStepsData().get(i).getTarget()!=null && !data.getStepsData().get(i).getTarget().equals("")){
                String[] target=data.getStepsData().get(i).getTarget().split("\\.");
                tmp.setPage(target[0]);
                tmp.setType(target[1]);
                tmp.setName(target[2]);
                tmp.setPath("//tmptest");
            }else{
                tmp.setPage("");
                tmp.setType("");
                tmp.setName("");
                tmp.setPath("");
            }
            if(data.getStepsData().get(i).getResponse()!=null && !data.getStepsData().get(i).getResponse().equals("")){
                tmp.setResponse(data.getStepsData().get(i).getResponse());
            }else{
                tmp.setResponse("");
            }
            for(int j=0;j<data.getStepsData().get(i).getStepParas().size();j++){
                tmp.addPara(data.getStepsData().get(i).getStepParas().get(j).getpValue());
            }
            steps.add(i, tmp);
        }
        result.put("steps", steps);
        result.put("caseId",casz.getCaseId());
        return result;
    }
}