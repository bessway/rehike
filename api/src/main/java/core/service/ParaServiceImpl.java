package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.ParaDao;
import core.pojo.Para;
import core.pojo.Step;
import core.pojo.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ParaService")
public class ParaServiceImpl implements ParaService {
    @Autowired
    private ParaDao paraDao = null;
    @Autowired
    private TestService testService = null;
    // 同一个test内，非引用参数名称不能重复,只要default存在则算重复
    public Para createTestPara(Para newPara) throws Exception{
        Para exist = paraDao.findTestParaByName(newPara.getTestId(),newPara.getParaName(), "default");
        if(exist!=null){
            throw new Exception("同一个用例内参数名称不能重复");
        }
        newPara.setParaId(new Date().getTime());
        paraDao.createPara(newPara);
        if(newPara.getDataVersion()==null){
            newPara.setDataVersion("default");
        }
        return paraDao.findParaById(newPara);
    }
    public List<Para> copyRefParasToStep(String srcTestId, String tarTestId, List<Long> stepId){
        List<Para> paras = paraDao.getFormalParas(srcTestId);
        for(Para item :paras){
            item.setIsFormalPara(0);
            item.setRefTestId(srcTestId);
            item.setTestId(tarTestId);
            item.setStepId(stepId);
            item.setDataVersion("default");
        }
        paraDao.bulkCreatePara(paras);
        List<Para> ret = paraDao.findStepParas(tarTestId, stepId, "default");
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
    //需要按版本设置
    public void setParasValue(List<Para> paras){
        paraDao.bulkSetParasValue(paras);
    }
    public List<Para> getTestParas(String testId, String dataVersion){
        return paraDao.getParasByTest(testId, dataVersion);
    }
    public List<Para> getTestRefParas(String testId, List<Long> stepId, String dataVersion){
        return paraDao.getRefParasByTest(testId, stepId, dataVersion);
    }
    public List<Para> getTestParasWithRef(String testId,String dataVersion){
        return paraDao.getParasByTestWithRef(testId, dataVersion);
    }
    //删除步骤时如果是引用步骤，需要删除所有版本的引用变量
    public void delStepFormalPara(String testId, List<Long> stepIds){
        paraDao.delStepFormalPara(testId, stepIds);
    }
    //修改参数名需要修改所有参数值版本
    public void setParasName(Para newPara) throws Exception{
        Para exist = paraDao.findTestParaByName(newPara.getTestId(),newPara.getParaName(), newPara.getDataVersion());
        if(exist!=null){
            throw new Exception("同一个用例内参数名称不能重复");
        }
        paraDao.setParasName(newPara.getTestId(), newPara.getParaId(), newPara.getParaName());
    }
    // 前端会检查参数是否正在被使用
    // 而且前端不会显示引用参数，所以引用参数在这里不会被删除
    public void delParas(List<Para> paras) throws Exception{
        List<Long> delIds = new ArrayList<Long>();
        for(Para item:paras){
            delIds.add(item.getParaId());
        }
        paraDao.delSelfParas(paras.get(0).getTestId(), delIds);
    }
    //返回新老paraId对应表
    public Map<Long,Long> copyAllParas(String oldTestId,String newTestId){
        List<Para> paras=paraDao.getParasByTestWithRef(oldTestId, "default");
        if(paras==null||paras.size()<1){
            return null;
        }
        for(Para item:paras){
            item.setTestId(newTestId);
            // 如果不是引用的参数，需要重置paraId
            if(item.getRefTestId()==null || "".equals(item.getRefTestId())){
                item.setParaId(new Date().getTime());
                try{
                    Thread.sleep(1);
                }catch(Exception e){
                }
            }
        }
        paraDao.bulkCreatePara(paras);
        //匹配新老paraId
        List<Para> newParas=paraDao.getParasByTestWithRef(newTestId, "default");
        Map<Long,Long> result=new HashMap<Long,Long>();
        for(Para oldPara:paras){
            for(Para newPara:newParas){
                //非引用参数，根据参数名来匹配新老id
                if(oldPara.getRefTestId()==null || "".equals(oldPara.getRefTestId())){
                    if(oldPara.getParaName().equals(newPara.getParaName())){
                        result.put(oldPara.getParaId(), oldPara.getParaId());
                        break;
                    }
                }else{
                    //引用参数，使用参数名加stepId匹配
                    if(oldPara.getParaName().equals(newPara.getParaName()) 
                        && oldPara.getStepId().get(0).equals(newPara.getStepId().get(0))){
                        result.put(oldPara.getParaId(), oldPara.getParaId());
                        break;
                    }
                }
            }
        }
        return result;
    }
    public void delNouseParaInTest(String testId, List<Long> stepIds){
        paraDao.delNouseParaInTest(testId, stepIds);
    }
}