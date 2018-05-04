package core.service;

import org.springframework.stereotype.Service;

import core.dao.ProjectDaoImpl;
import core.pojo.ProjectPojo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service("TestService")
public class TestServiceImpl{
    @Autowired
    private ProjectDaoImpl project=null;

    public List<ProjectPojo> getProjects(){
        return this.project.getAllProjects();
    }
}