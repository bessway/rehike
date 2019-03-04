package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.dao.TestDao;
import core.pojo.Test;
import core.pojo.TestDetail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.Utils;

@Service("TestService")
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDao testDao = null;
    @Autowired
    private ParaService paraService = null;
    
    public List<Test> getTestsByParentId(String parentId){
        List<Test> ret = testDao.getTestsByParentId(parentId);
        if("000000000000000000000000000000".equals(parentId) && (ret==null || ret.size()==0)){
            Test newTest = new Test();
            newTest.setParentId(parentId);
            newTest.setTestDesc("new project");
            if(ret==null){
                ret=new ArrayList<Test>();
            }
            ret.add(this.createTest(newTest));
        }
        
        return ret;
    }
    public Test createTest(Test newTest){
        if(newTest.getTestDesc()==null ||"".equals(newTest.getTestDesc())){
            if("000000000000000000000000000000".equals(newTest.getParentId())){
                newTest.setTestDesc("new project" + String.valueOf(new Date().getTime()));
            }else{
                newTest.setTestDesc("new test" + String.valueOf(new Date().getTime()));
            }
        }
        newTest.setStatus(1);
        newTest.setIsRefered(0);
        testDao.createTest(newTest);
        List<Test> rets = testDao.getTestsByDesc(newTest.getTestDesc());
        return testDao.updateTestIndex(rets.get(0).getTestId(), "index", this.calTestId(rets.get(0).getTestId()));
    }
    public void saveTest(Test test){
        testDao.updateTest(test);
    }
    public List<Test> searchPublicTest(String key){
        return testDao.searchPublicTest(Utils.escapeExprSpecialWord(key));
    }
    public TestDetail getTestContent(String testId){
        //TODO
        return null;
    }
    public Test getTestDetail(String testId){
        return testDao.getTestById(testId);
    }
    public void setTestToRef(String testId){
        testDao.setTestToRef(testId);
    }
    private Long calTestId(String objectId){
        String idString=objectId.substring(objectId.length()-6);
        Long index=Long.valueOf(idString, 16);

        return index;
    }
    public List<String> findAllExecutableTests(List<String> testIds){
        List<String> result = new ArrayList<String>();
        List<String> newTestIds = new ArrayList<String>();
        List<Test> tests = testDao.getTestsById(testIds);
        for(Test item:tests){
            //有步骤则是case
            if(item.getSteps()!=null && item.getSteps().size()>0){
                result.add(item.getTestId());
            }else{
                newTestIds.add(item.getTestId());
            }
        }
        if(newTestIds.size()>0){
            this.findSubTests(newTestIds, result);
        }
        return result;
    }
    private void findSubTests(List<String> nodeIds, List<String> result){
        List<Test> subTests=testDao.getTestsByParentIds(nodeIds);
        List<String> newTestIds = new ArrayList<String>();
        for(Test item:subTests){
            //有步骤则是case
            if(item.getSteps()!=null && item.getSteps().size()>0){
                result.add(item.getTestId());
            }else{
                newTestIds.add(item.getTestId());
            }
        }
        if(newTestIds.size()>0){
            this.findSubTests(newTestIds, result);
        }
    }
    public List<Test> getTests(List<String> testIds){
        return testDao.getTestsById(testIds);
    }
    public Test copyOneTest(Test oldTest){
        String oldTestId=oldTest.getTestId();
        Test old = testDao.getTestById(oldTestId);
        old.setTestId(null);
        old.setTestDesc("copy from " + old.getTestDesc());
        testDao.createTest(old);
        List<Test> newTests = testDao.getTestsByDesc(old.getTestDesc());
        paraService.copyAllParas(oldTestId, newTests.get(0).getTestId());
        return testDao.updateTestIndex(newTests.get(0).getTestId(), "index", this.calTestId(newTests.get(0).getTestId()));
    }
}