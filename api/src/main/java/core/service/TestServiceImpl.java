package core.service;

import org.springframework.stereotype.Service;

import core.dao.TestDaoImpl;
import core.pojo.CasePojo;
import core.pojo.HierachyPojo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service("TestService")
public class TestServiceImpl{
    @Autowired
    private TestDaoImpl project=null;

    public List<HierachyPojo> getProjects(){
        return this.project.getAllProjects();
    }
    public List<HierachyPojo> getSubNodes(String parentId){
        return this.project.getSubNodes(parentId);
    }
    public CasePojo getCaseDetail(String caseId){
        return this.project.getCaseDetail(caseId);
    }
}