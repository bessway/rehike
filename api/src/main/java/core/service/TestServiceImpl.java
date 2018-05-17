package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.TestDao;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Utils;

@Service("TestService")
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDao testDao = null;
    @Autowired
    private DataService dataService=null;
    @Autowired
    private ObjectService objService=null;

    public List<HierachyPojo> getProjects() {
        return this.testDao.getAllProjects();
    }

    public List<HierachyPojo> getSubNodes(String parentId) {
        return this.testDao.getSubNodes(parentId);
    }

    public Map<String, Object> getCaseDetail(String caseId, String version) {
        CasePojo casz = testDao.getCaseDetail(caseId);
        CaseDataPojo data =dataService.getCaseData(caseId, version);

        return this.wrapCaseData(casz, data);
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
    
    public Boolean deleteNode(String nodeId) {
        List<String> allCases=new ArrayList<String>();
        List<String> rootNodeId=new ArrayList<String>();
        rootNodeId.add(nodeId);
        this.findAllNodes(rootNodeId, allCases);
        //delete data
        dataService.deleteMultipleCasesData(allCases);
        //delete case
        testDao.deleteMultipleCases(allCases);
        //delete node
        testDao.deleteMultipleNodes(allCases);

        return true;
    }
    public List<String> getValidCases(List<String> toValidate){
        return testDao.getValidCases(toValidate);
    }
    public Boolean findAllNodes(List<String> nodeIds,List<String> allCases){
        List<HierachyPojo> subNodes=testDao.getSubNodes(nodeIds);
        if(subNodes!=null && subNodes.size()>0){
            //只要有一个nodeId有子节点，那么这一级node一定不是case
            List<String> ids=new ArrayList<String>();
            for(HierachyPojo item:subNodes){
                ids.add(item.getRefId());
            }
            this.findAllNodes(ids, allCases);
            allCases.addAll(nodeIds);
        }else{
            //如果没有子节点，则可能是case
            allCases.addAll(nodeIds);
        }
        return true;
    }

    public Boolean updateCase(String nodeId, List<StepDetailPojo> steps) {
        //前端数据可能未排序
        Collections.sort(steps,StepDetailPojo.sidComp);
        List<StepPojo> aloneSteps=new ArrayList<StepPojo>();
        CaseDataPojo data=new CaseDataPojo();
        List<String> gPara=new ArrayList<String>();
        for(int i=0;i<steps.size();i++){
            //update object
            if(steps.get(i).getAction().equals("")){
                continue;
            }
            ObjectPojo stepObj=this.extractObj(steps.get(i));
            if(this.isToUpdateObj(stepObj)){
                String key=stepObj.getPage()+"."+stepObj.getType()+"."+stepObj.getName();
                objService.updateObject(stepObj);
                //update cached global objects
                Utils.addToObjectMap(key,stepObj.getXpath());
            }
            //build List<StepPojo>
            aloneSteps.add(i,this.extractStep(steps.get(i)));
            //build sharedParas
            data.appendSharedParas(this.extractSharedPara(steps.get(i)));
            //build globalParas
            gPara.addAll(this.extractGlobalPara(steps.get(i)));
            data.addStepsData(i, this.extractStepData(steps.get(i)));
        }
        testDao.updateCaseSteps(nodeId,aloneSteps);
        dataService.updateData(nodeId,"default",data);
        if(gPara.size()>0){
            dataService.updateGlobalParas(gPara);
        }
        
        return null;
    }
    public List<CasePojo> getCases(List<String> casesId){
        return testDao.getCases(casesId);
    }
    public ObjectPojo extractObj(StepDetailPojo step) {
        if (step.getPage() == null || step.getPage().equals("")) {
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
        if(step.getPage().equals("")){
            data.setTarget("");
        }else{
            data.setTarget(step.getPage()+"."+step.getType()+"."+step.getName());
        }
        for(int i=0;i<step.getParas().size();i++){
            if(step.getPara(i)!=null){
                ParaPojo p=new ParaPojo();
                p.setpIndex(i);
                p.setpValue(step.getPara(i));
                data.addStepPara(i, p);
            }
        }
        if(data.getStepParas()==null||data.getStepParas().size()==0){
            data.setStepParas(new ArrayList<ParaPojo>());
        }

        return data;
    }
    public StepPojo extractStep(StepDetailPojo onestep){
        StepPojo step=new StepPojo();
        step.setKey(onestep.getAction());
        step.setIndex(onestep.getId());
        return step;
    }
    public List<String> extractGlobalPara(StepDetailPojo step){
        List<String> gPara=new ArrayList<String>();
        if(step.getResponse().contains("{{")){
            gPara.add(step.getResponse());
        }
        for(String item:step.getParas()){
            if(item==null){
                continue;
            }
            if(item.contains("{{")){
                Pattern p=Pattern.compile(".*(\\{\\{.*\\}\\}).*");
                Matcher m=p.matcher(item);
                m.find();
                if(m.groupCount()>0){
                    for(int i=1;i<=m.groupCount();i++){
                        gPara.add(m.group(i));
                    }
                }
            }
        }
        return gPara;
    }
    public Map<String,String> extractSharedPara(StepDetailPojo step){
        Map<String,String> sPara=new HashMap<String,String>();
        if(step.getResponse().contains("[[")){
            sPara.put(step.getResponse(),"");
        }
        for(String item:step.getParas()){
            if(item==null){
                continue;
            }
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
        if(obj==null){
            return false;
        }
        if(Utils.objectsMap==null){
            return true;
        }
        
        String key=obj.getPage()+"."+obj.getType()+"."+obj.getName();
        if(obj.getXpath().equals(Utils.objectsMap.get(key))){
            return false;
        }
        
        return true;
    }

    public Map<String, Object> wrapCaseData(CasePojo casz, CaseDataPojo data) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (casz == null || data == null) {
            return result;
        }
        List<StepDetailPojo> steps = new ArrayList<StepDetailPojo>();
        for (int i = 0; i < casz.getSteps().size(); i++) {
            StepDetailPojo tmp = new StepDetailPojo();
            tmp.setAction(casz.getSteps().get(i).getKey());
            tmp.setid(casz.getSteps().get(i).getIndex());
            if (data.getStepsData().get(i).getTarget() != null && !data.getStepsData().get(i).getTarget().equals("")) {
                String[] target = data.getStepsData().get(i).getTarget().split("\\.");
                tmp.setPage(target[0]);
                tmp.setType(target[1]);
                tmp.setName(target[2]);

                if (Utils.objectsMap == null) {
                    Utils.objectsMap=(Hashtable<String,String>)objService.getAllObjects();
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