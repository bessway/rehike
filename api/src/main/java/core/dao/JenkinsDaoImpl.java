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
        List<Utils.ExecStatus> inUseStatus=new ArrayList<Utils.ExecStatus>();
        inUseStatus.add(Utils.ExecStatus.READYTOSTART);
        inUseStatus.add(Utils.ExecStatus.RUNNING);
        Query query=Query.query(Criteria.where("buildStatus").in(inUseStatus));
        query.fields().exclude("_id");
        query.fields().include("jobName");
        return mongoTemplate.find(query, Task.class);
    }
    public List<Task> getAllTasks(Date startDate){
        Query query=Query.query(Criteria.where("createTime").gt(startDate));
        query.fields().exclude("forceStop");
        query.fields().exclude("paras");
        query.fields().exclude("_id");
        return mongoTemplate.find(query, Task.class);
    }
    public Task getExecution(String jobName,Integer taskId){
        Query query=Query.query(Criteria.where("jobName").is(jobName).and("buildId").is(taskId));
        return mongoTemplate.findOne(query, Task.class);
    }
    public void updateExecutionStatus(Task suite){
        Query query=Query.query(Criteria.where("jobName").is(suite.getJenkinsJobName()).and("buildId").is(suite.getJenkinsBuildId()));
        Update update=Update.update("buildStatus", suite.getTaskStatus());
        if(suite.getTests()!=null && suite.getTests().size()>0){
            update.set("cases", suite.getTests());
        }
        update.set("forceStop",suite.isFroceStop());
        update.set("endTime",suite.getEndTime());
        mongoTemplate.findAndModify(query, update, Task.class);
    }
    public void updateTestStatus(String jobName,Integer taskId,String caseId,Utils.ExecStatus status){
        Query query=Query.query(Criteria.where("jobName").is(jobName).and("buildId").is(taskId));
        Update update=Update.update("cases."+caseId, status);
        mongoTemplate.findAndModify(query, update, Task.class);
    }
    public void updateAgentStatus(String jobName,Integer isFree){
        Query query=Query.query(Criteria.where("jobName").is(jobName));
        Update update=Update.update("status", isFree);
        mongoTemplate.findAndModify(query, update, Agent.class);
    }
}