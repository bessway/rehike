package core.dao;

import java.util.List;

import core.pojo.Test;


public interface TestDao{
    public List<Test> getTestsByParentId(String parentId);
    public List<Test> getTestsByDesc(String testDesc);
    public void createTest(Test newTest);
    public Test updateTestIndex(String testId, String key, Long value);
    public void updateTest(Test t);
    public Test getTestById(String testId);
    public List<Test> searchPublicTest(String key);
}