package core.service;

import java.util.List;

import core.pojo.Test;
import core.pojo.TestDetail;


public interface TestService{
    public List<Test> getTestsByParentId(String parentId);
    public Test createTest(Test newTest);
    public void saveTest(Test test);
    public Test getTestDetail(String testsId);
    public TestDetail getTestContent(String testId);
    public List<Test> searchPublicTest(String key);
    public void setTestToRef(String testId);
    public List<String> findAllExecutableTests(List<String> nodeIds);
    public List<Test> getTests(List<String> testIds);
}