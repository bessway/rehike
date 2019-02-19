package core.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import core.pojo.Agent;
import core.pojo.Task;
import utils.Utils;

@Repository("JenkinsDao")
public class JenkinsDaoImpl implements JenkinsDao{
    @Autowired
    private MongoTemplate mongoTemplate=null;

    public void saveExecution(Task suite){
        mongoTemplate.insert(suite);
    }
    public List<Agent> getAllAgents(Integer isPublic){
        Query query=Query.query(Criteria.where("isPublic").is(isPublic));
        query.fields().exclude("_id");
        query.fields().exclude("isPublic");
        query.fields().exclude("owner");
        return mongoTemplate.find(query, Agent.class);
    }
    public List<Task> getRunningJobs(){
        List<String> inUseStatus=new ArrayList<String>();
        inUseStatus.add(Utils.ExecStatus.READYTOSTART.name());
        inUseStatus.add(Utils.ExecStatus.RUNNING.name());
        Query query=Query.query(Criteria.where("taskStatus").in(inUseStatus));
        query.fields().exclude("_id");
        query.fields().include("jenkinsJobName");
        return mongoTemplate.find(query, Task.class);
    }
    public List<Task> getAllTasks(Date startDate){
        Query query=Query.query(Criteria.where("createTime").gt(startDate));
        query.fields().exclude("forceStop");
        query.fields().exclude("_id");
        return mongoTemplate.find(query, Task.class);
    }
    public Task getExecution(String jobName,Integer taskId){
        Query query=Query.query(Criteria.where("jenkinsJobName").is(jobName).and("jenkinsBuildId").is(taskId));
        return mongoTemplate.findOne(query, Task.class);
    }
    public void updateExecutionStatus(Task suite){
        Query query=Query.query(Criteria.where("jenkinsJobName").is(suite.getJenkinsJobName()).and("jenkinsBuildId").is(suite.getJenkinsBuildId()));
        Update update=Update.update("taskStatus", suite.getTaskStatus());
        if(suite.getTests()!=null && suite.getTests().size()>0){
            update.set("tests", suite.getTests());
        }
        update.set("forceStop",suite.isFroceStop());
        update.set("endTime",suite.getEndTime());
        mongoTemplate.findAndModify(query, update, Task.class);
    }
    public void updateTestStatus(String jobName,Integer taskId,String caseId,String status){
        Query query=Query.query(Criteria.where("jenkinsJobName").is(jobName).and("jenkinsBuildId").is(taskId));
        Update update=Update.update("tests."+caseId, status);
        mongoTemplate.findAndModify(query, update, Task.class);
    }
    public void updateAgentStatus(String jobName,Integer isAvailable){
        Query query=Query.query(Criteria.where("jobName").is(jobName));
        Update update=Update.update("status", isAvailable);
        mongoTemplate.findAndModify(query, update, Agent.class);
    }
}