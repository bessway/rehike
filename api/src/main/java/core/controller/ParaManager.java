package core.controller;
import java.util.List;

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
    public Para createTestPara(@RequestBody Para newPara) throws Exception{
        return paraService.createTestPara(newPara);
    }

    @RequestMapping(value="/{srcTestId}/formalpara/{tarTestId}/step/{stepId}",method=RequestMethod.POST)
    public List<Para> addRefParasToStep( @PathVariable String srcTestId, @PathVariable String tarTestId, @PathVariable List<Long> stepId){
        return paraService.copyRefParasToStep(srcTestId, tarTestId, stepId);
    }

    @RequestMapping(value="/formalparas",method=RequestMethod.PUT)
    public String setFormalParas( @RequestBody List<Para> paras) throws Exception{
        paraService.setParasFormal(paras);
        return "success";
    }

    @RequestMapping(value="/values",method=RequestMethod.PUT)
    public String setParasValue(@RequestBody List<Para> paras){
        paraService.setParasValue(paras);
        return "success";
    }

    @RequestMapping(value="/test/{testId}/version/{dataVersion}", method=RequestMethod.GET)
    public List<Para> getTestParas(@PathVariable String testId, @PathVariable String dataVersion) {
        return paraService.getTestParas(testId, dataVersion);
    }
    
    @RequestMapping(value="/test/{testId}/step/{stepId}/versioin/{dataVersion}", method=RequestMethod.GET)
    public List<Para> getTestRefParas(@PathVariable String testId, @PathVariable List<Long> stepId, @PathVariable String dataVersion) {
        return paraService.getTestRefParas(testId, stepId, dataVersion);
    }

    @RequestMapping(value="/test/{testId}/version/{dataVersion}/all", method=RequestMethod.GET)
    public List<Para> getTestParasWithRef(@PathVariable String testId, @PathVariable String dataVersion) {
        return paraService.getTestParasWithRef(testId, dataVersion);
    }

    @RequestMapping(value="/test/{testId}",method=RequestMethod.DELETE)
    public String delStepsFormalParas( @PathVariable String testId, @RequestBody List<Long> stepIds){
        paraService.delStepFormalPara(testId, stepIds);
        return "success";
    }

    @RequestMapping(value="/para",method=RequestMethod.PUT)
    public String setParaName(@RequestBody Para newPara) throws Exception{
        paraService.setParasName(newPara);
        return "success";
    }

    @RequestMapping(value="/para",method=RequestMethod.DELETE)
    public String delParas(@RequestBody List<Para> paras) throws Exception{
        paraService.delParas(paras);
        return "success";
    }
}