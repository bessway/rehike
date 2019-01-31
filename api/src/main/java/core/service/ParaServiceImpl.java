package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.ParaDao;
import core.pojo.Para;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Utils;

@Service("ParaService")
public class ParaServiceImpl implements ParaService {
    @Autowired
    private ParaDao paraDao = null;
    
    public Para createTestPara(Para newPara){
        newPara.setParaId(new Date().getTime());
        paraDao.createPara(newPara);
        return paraDao.findParaById(newPara);
    }
    public List<Para> copyRefParasToStep(String srcTestId, String tarTestId, Integer stepId){
        List<Para> paras = paraDao.getFormalParas(srcTestId);
        for(Para item :paras){
            item.setIsFormalPara(0);
            item.setRefTestId(srcTestId);
            item.setTestId(tarTestId);
            item.setStepId(stepId);
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
    }
    public void setParasValue(List<Para> paras){
        paraDao.bulkSetParasValue(paras);
    }
}