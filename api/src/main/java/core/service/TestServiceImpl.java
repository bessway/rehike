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
    
    public List<Test> getTestsByParentId(String parentId){
        List<Test> ret = testDao.getTestsByParentId(parentId);
        if("000000000000000000000000000000".equals(parentId) && (ret==null || ret.size()==0)){
            Test newTest = new Test();
            newTest.setParentId(parentId);
            newTest.setTestDesc("first test");
            if(ret==null){
                ret=new ArrayList<Test>();
            }
            ret.add(this.createTest(newTest));
        }
        
        return ret;
    }
    public Test createTest(Test newTest){
        if(newTest.getTestDesc()==null ||"".equals(newTest.getTestDesc())){
            newTest.setTestDesc(String.valueOf(new Date().getTime()));
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
    public TestDetail getCaseDetail(String testId){
        return null;
    }
    private Long calTestId(String objectId){
        String idString=objectId.substring(objectId.length()-6);
        Long index=Long.valueOf(idString, 16);

        return index;
    }
}