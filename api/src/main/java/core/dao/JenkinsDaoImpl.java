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

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;
import utils.Utils;

@Repository("JenkinsDao")
public class JenkinsDaoImpl implements JenkinsDao{
    @Autowired
    private MongoTemplate mongoTemplate=null;

    public Boolean saveExecution(BuildPojo suite){
        mongoTemplate.insert(suite);
        return true;
    }
    public List<AgentPojo> getAllAgents(Boolean isPublic){
        Query query=Query.query(Criteria.where("isPublic").is(isPublic));
        query.fields().exclude("_id");
        query.fields().exclude("isPublic");
        query.fields().exclude("owner");
        return mongoTemplate.find(query, AgentPojo.class);
    }
    public List<BuildPojo> getRunningJobs(){
        List<Utils.ExecStatus> inUseStatus=new ArrayList<Utils.ExecStatus>();
        inUseStatus.add(Utils.ExecStatus.READYTOSTART);
        inUseStatus.add(Utils.ExecStatus.RUNNING);
        Query query=Query.query(Criteria.where("buildStatus").in(inUseStatus));
        query.fields().exclude("_id");
        query.fields().include("jobName");
        return mongoTemplate.find(query, BuildPojo.class);
    }
    public List<BuildPojo> getAllBuilds(Date startDate){
        Query query=Query.query(Criteria.where("createTime").gt(startDate));
        query.fields().exclude("forceStop");
        query.fields().exclude("paras");
        query.fields().exclude("_id");
        return mongoTemplate.find(query, BuildPojo.class);
    }
    public BuildPojo getExecution(String jobName,Integer buildId){
        Query query=Query.query(Criteria.where("jobName").is(jobName).and("buildId").is(buildId));
        return mongoTemplate.findOne(query, BuildPojo.class);
    }
    public Boolean updateExecutionStatus(BuildPojo suite){
        Query query=Query.query(Criteria.where("jobName").is(suite.getJobName()).and("buildId").is(suite.getBuildId()));
        Update update=Update.update("buildStatus", suite.getBuildStatus());
        update.set("cases", suite.getCases());
        update.set("forceStop",suite.isFroceStop());
        update.set("endTime",suite.getEndTime());
        BuildPojo result=mongoTemplate.findAndModify(query, update, BuildPojo.class);
        return true;
    }
    public Boolean updateCaseStatus(String jobName,Integer buildId,String caseId,Utils.ExecStatus status){
        Query query=Query.query(Criteria.where("jobName").is(jobName).and("buildId").is(buildId));
        Update update=Update.update("cases."+caseId, status);
        mongoTemplate.findAndModify(query, update, BuildPojo.class);
        return true;
    }
    public Boolean updateAgentStatus(String jobName,Boolean isFree){
        Query query=Query.query(Criteria.where("jobName").is(jobName));
        Update update=Update.update("status", isFree);
        mongoTemplate.findAndModify(query, update, AgentPojo.class);
        return true;
    }
}