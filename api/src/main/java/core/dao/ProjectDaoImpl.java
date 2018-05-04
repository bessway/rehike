package core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import core.pojo.ProjectPojo;

@Repository("projectDao")
public class ProjectDaoImpl{
    @Autowired
    private MongoTemplate mongoTemplate=null;
    public List<ProjectPojo> getAllProjects(){
        List<ProjectPojo> ret= mongoTemplate.findAll(ProjectPojo.class);
        return ret;
    } 
}