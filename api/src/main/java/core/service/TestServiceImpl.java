package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.DataDaoImpl;
import core.dao.ObjectDaoImpl;
import core.dao.TestDaoImpl;
import core.pojo.CaseDataPojo;
import core.pojo.StepDetailPojo;
import core.pojo.StepPojo;
import core.pojo.CasePojo;
import core.pojo.HierachyPojo;
import core.pojo.ObjectPojo;
import core.pojo.ParaPojo;
import core.pojo.StepDataPojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Utils;

@Service("TestService")
public class TestServiceImpl {
    @Autowired
    private TestDaoImpl testDao = null;
    @Autowired
    private DataDaoImpl dataDao = null;
    @Autowired
    private ObjectDaoImpl objDao = null;

    public List<HierachyPojo> getProjects() {
        return this.testDao.getAllProjects();
    }

    public List<HierachyPojo> getSubNodes(String parentId) {
        return this.testDao.getSubNodes(parentId);
    }

    public HashMap<String, Object> getCaseDetail(String caseId, String version) {
        CasePojo casz = testDao.getCaseDetail(caseId);
        CaseDataPojo data = dataDao.getCaseData(caseId, version);

        return this.wrapCaseData(casz, data);
    }

    public Boolean deleteCase(String caseId) {

        return true;
    }

    public HierachyPojo addNode(HierachyPojo node) {
        node.setLabel("new node" + String.valueOf(new Date().getTime()));
        testDao.addNode(node);
        return testDao.getNodeByLabel(node.getLabel());
    }

    public HierachyPojo updateNodeParentId(String nodeId, HierachyPojo parentNode) {
        return testDao.updateParentNode(nodeId, parentNode.getRefId());
    }

    public HierachyPojo updateNodeName(String nodeId, HierachyPojo newName) {
        return testDao.updateCaseName(nodeId, newName.getLabel());
    }

    public Boolean updateCase(String nodeId, List<StepDetailPojo> steps) {
        //前端数据可能未排序
        Collections.sort(steps,StepDetailPojo.sidComp);
        List<StepPojo> aloneSteps=new ArrayList<StepPojo>();
        CaseDataPojo data=new CaseDataPojo();
        HashMap<String,String> gPara=new HashMap<String,String>();
        Boolean isObjUpdated=false;
        for(int i=0;i<steps.size();i++){
            //update object
            ObjectPojo stepObj=this.extractObj(steps.get(i));
            if(this.isToUpdateObj(stepObj)){
                objDao.updateObject(stepObj);
                isObjUpdated=true;
            }
            //build List<StepPojo>
            aloneSteps.add(i,this.extractStep(steps.get(i)));
            //build sharedParas
            data.appendSharedParas(this.extractSharedPara(steps.get(i)));
            //build globalParas
            gPara.putAll(this.extractGlobalPara(steps.get(i)));
            data.addStepsData(i, this.extractStepData(steps.get(i)));
        }
        testDao.updateCaseSteps(nodeId,aloneSteps);
        dataDao.updateData(nodeId,"default",data.getStepsData());
        dataDao.updateGlobalParas(gPara);
        if(isObjUpdated){
            Utils.cacheObject(objDao.getAllObjects());
        }
        
        return null;
    }
    public ObjectPojo extractObj(StepDetailPojo step) {
        if (step.getAction() == null || step.getAction().equals("")) {
            return null;
        }
        ObjectPojo obj = new ObjectPojo();
        obj.setName(step.getName());
        obj.setPage(step.getPage());
        obj.setType(step.getType());
        obj.setXpath(step.getPath());
        return obj;
    }
    public StepDataPojo extractStepData(StepDetailPojo step){
        StepDataPojo data=new StepDataPojo();
        data.setsIndex(step.getId());
        data.setResponse(step.getResponse());
        data.setTarget(step.getPage()+"."+step.getType()+"."+step.getName());
        for(int i=0;i<step.getParas().size();i++){
            ParaPojo p=new ParaPojo();
            p.setpIndex(i);
            p.setpValue(step.getPara(i));
            data.addStepPara(i, p);
        }

        return data;
    }
    public StepPojo extractStep(StepDetailPojo onestep){
        StepPojo step=new StepPojo();
        step.setKey(onestep.getAction());
        step.setIndex(onestep.getId());
        return step;
    }
    public HashMap<String,String> extractGlobalPara(StepDetailPojo step){
        HashMap<String,String> gPara=new HashMap<String,String>();
        if(step.getResponse().contains("{{")){
            gPara.put(step.getResponse(),"");
        }
        for(String item:step.getParas()){
            if(item.contains("{{")){
                Pattern p=Pattern.compile(".*(\\{\\{.*\\}\\}).*");
                Matcher m=p.matcher(item);
                m.find();
                if(m.groupCount()>0){
                    for(int i=1;i<=m.groupCount();i++){
                        gPara.put(m.group(i),"");
                    }
                }
            }
        }
        return gPara;
    }
    public Hashtable<String,String> extractSharedPara(StepDetailPojo step){
        Hashtable<String,String> sPara=new Hashtable<String,String>();
        if(step.getResponse().contains("[[")){
            sPara.put(step.getResponse(),"");
        }
        for(String item:step.getParas()){
            if(item.contains("[[")){
                Pattern p=Pattern.compile(".*(\\[\\[.*\\]\\]).*");
                Matcher m=p.matcher(item);
                m.find();
                if(m.groupCount()>0){
                    for(int i=1;i<=m.groupCount();i++){
                        sPara.put(m.group(i),"");
                    }
                }
            }
        }
        
        return sPara;
    }
    public Boolean isToUpdateObj(ObjectPojo obj){
        if(Utils.objectsMap==null){
            return true;
        }
        if(obj==null){
            return false;
        }
        
        String key=obj.getPage()+"."+obj.getType()+"."+obj.getName();
        if(obj.getXpath().equals(Utils.objectsMap.get(key))){
            return false;
        }
        
        return true;
    }

    public HashMap<String, Object> wrapCaseData(CasePojo casz, CaseDataPojo data) {
        HashMap<String, Object> result = new HashMap<String, Object>();
        if (casz == null || data == null) {
            return result;
        }
        ArrayList<StepDetailPojo> steps = new ArrayList<StepDetailPojo>();
        for (int i = 0; i < casz.getSteps().size(); i++) {
            StepDetailPojo tmp = new StepDetailPojo();
            tmp.setAction(casz.getSteps().get(i).getKey());
            tmp.setid(casz.getSteps().get(i).getIndex());
            if (data.getStepsData().get(i).getTarget() != null && !data.getStepsData().get(i).getTarget().equals("")) {
                String[] target = data.getStepsData().get(i).getTarget().split("\\.");
                tmp.setPage(target[0]);
                tmp.setType(target[1]);
                tmp.setName(target[2]);

                if (Utils.objects == null) {
                    Utils.cacheObject(objDao.getAllObjects());
                }
                tmp.setPath(Utils.objectsMap.get(data.getStepsData().get(i).getTarget()));
            } else {
                tmp.setPage("");
                tmp.setType("");
                tmp.setName("");
                tmp.setPath("");
            }
            if (data.getStepsData().get(i).getResponse() != null
                    && !data.getStepsData().get(i).getResponse().equals("")) {
                tmp.setResponse(data.getStepsData().get(i).getResponse());
            } else {
                tmp.setResponse("");
            }
            for (int j = 0; j < data.getStepsData().get(i).getStepParas().size(); j++) {
                tmp.addPara(j,data.getStepsData().get(i).getStepParas().get(j).getpValue());
            }
            steps.add(i, tmp);
        }
        result.put("steps", steps);
        result.put("caseId", casz.getCaseId());
        return result;
    }
}