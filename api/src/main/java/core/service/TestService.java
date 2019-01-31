package core.service;

import java.util.List;
import java.util.Map;

import core.pojo.Test;
import core.pojo.TestDetail;


public interface TestService{
    public List<Test> getTestsByParentId(String parentId);
    public Test createTest(Test newTest);
    public void saveTest(Test test);
}