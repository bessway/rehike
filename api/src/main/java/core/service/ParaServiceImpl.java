package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.ParaDao;
import core.pojo.Para;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("ParaService")
public class ParaServiceImpl implements ParaService {
    @Autowired
    private ParaDao paraDao = null;
    @Autowired
    private TestService testService = null;
    //TODO 同一个test内，非引用参数名称不能重复
    public Para createTestPara(Para newPara){
        newPara.setParaId(new Date().getTime());
        paraDao.createPara(newPara);
        if(newPara.getDataVersion()==null){
            newPara.setDataVersion("default");
        }
        return paraDao.findParaById(newPara);
    }
    public List<Para> copyRefParasToStep(String srcTestId, String tarTestId, Integer stepId){
        List<Para> paras = paraDao.getFormalParas(srcTestId);
        for(Para item :paras){
            item.setIsFormalPara(0);
            item.setRefTestId(srcTestId);
            item.setTestId(tarTestId);
            item.setStepId(stepId);
            item.setDataVersion("default");
        }
        paraDao.bulkCreatePara(paras);
        List<Para> ret = paraDao.findStepParas(tarTestId, stepId);
        return ret;
    }
    public void setParasFormal(List<Para> paras){
        List<Long> ids=new ArrayList<Long>();
        for(Para item:paras){
            ids.add(item.getParaId());
        }
        paraDao.bulkSetParasFormal(paras.get(0).getTestId(), ids);
        testService.setTestToRef(paras.get(0).getTestId());
    }
    public void setParasValue(List<Para> paras){
        paraDao.bulkSetParasValue(paras);
    }
    public List<Para> getTestParas(String testId, String dataVersion){
        return paraDao.getParasByTest(testId, dataVersion);
    }
    public List<Para> getTestRefParas(String testId, Integer stepId, String dataVersion){
        return paraDao.getRefParasByTest(testId, stepId, dataVersion);
    }
    public List<Para> getTestParasWithRef(String testId,String dataVersion){
        return paraDao.getParasByTestWithRef(testId, dataVersion);
    }
}