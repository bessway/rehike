package core.dao;

import java.util.List;

import core.pojo.Test;


public interface TestDao{
    public List<Test> getTestsByParentId(String parentId);
    public List<Test> getTestsByParentIds(List<String> parentIds);
    public List<Test> getTestsByDesc(String testDesc);
    public void createTest(Test newTest);
    public Test updateTestIndex(String testId, String key, Long value);
    public void updateTest(Test t);
    public Test getTestById(String testId);
    public List<Test> getTestsById(List<String> testIds);
    public List<Test> searchPublicTest(String key);
    public void setTestToRef(String testId);
}