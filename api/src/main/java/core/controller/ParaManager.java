package core.controller;
import java.util.List;

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

    @RequestMapping(value="/para",method=RequestMethod.POST)
    public Para createTestPara(@RequestBody Para newPara){
        return paraService.createTestPara(newPara);
    }

    @RequestMapping(value="/{srcTestId}/formalpara/{tarTestId}/step/{stepId}",method=RequestMethod.POST)
    public List<Para> addRefParasToStep( @PathVariable String srcTestId, @PathVariable String tarTestId, @PathVariable Integer stepId){
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

    @RequestMapping(value="/test/{testId}", method=RequestMethod.GET)
    public List<Para> getTestParas(@PathVariable String testId) {
        return paraService.getTestParas(testId);
    }
    
}