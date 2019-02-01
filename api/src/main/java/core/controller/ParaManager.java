package core.controller;
import com.google.gson.Gson;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import core.pojo.Para;
import core.service.ParaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/paras")
public class ParaManager{
    @Autowired
    private ParaService paraService = null;
    //private Gson gson = new Gson();

    @RequestMapping(value="/para",method=RequestMethod.POST)
    public Para createTestPara(@RequestBody Para newPara){
        //String ret = gson.toJson(paraService.createTestPara(newPara));
        //return ret;
        return paraService.createTestPara(newPara);
    }

    @RequestMapping(value="/{srcTestId}/formalpara/{tarTestId}/step/{stepId}",method=RequestMethod.POST)
    public List<Para> addRefParasToStep( @PathVariable String srcTestId, @PathVariable String tarTestId, @PathVariable Integer stepId){
        //String ret = gson.toJson(paraService.copyRefParasToStep(srcTestId, tarTestId, stepId));
        //return ret;
        return paraService.copyRefParasToStep(srcTestId, tarTestId, stepId);
    }

    @RequestMapping(value="/formalparas",method=RequestMethod.PUT)
    public String setFormalParas( @RequestBody List<Para> paras) throws Exception{
        paraService.setParasFormal(paras);
        return "success";
    }

    @RequestMapping(value="/values",method=RequestMethod.PUT)
    public String setParasValue(HttpServletResponse res, @RequestBody List<Para> paras){
        paraService.setParasValue(paras);
        return "success";
    }
}