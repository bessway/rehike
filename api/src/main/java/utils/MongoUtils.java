package utils;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mongodb.MongoClient;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import core.pojo.AgentPojo;
import core.pojo.BuildPojo;
import core.pojo.CaseDataPojo;
import core.pojo.CasePojo;
import core.pojo.KeyPojo;
import core.pojo.ObjectPojo;

public class MongoUtils {
    private static Logger logger = Logger.getLogger(MongoUtils.class);
    private static String mongoFile = "agent.properties";
    private static Properties mongoProperty = null;
    private static MongoClient client = null;
    private static MongoTemplate mongo=null;

    private static MongoTemplate getDB() {
        if(mongo==null){
            if(client==null){
                try {
                    mongoProperty=Utils.readPropery(mongoFile);
                    client = new MongoClient(mongoProperty.getProperty("mongodb.host"),
                            Integer.valueOf(mongoProperty.getProperty("mongodb.port")));
                } catch (Exception e) {
                    logger.error("failed to connect mongodb: " + e.getCause());
                }
            }
            mongo=new MongoTemplate(client, mongoProperty.getProperty("mongodb.db"));
        }
        return mongo;
    }

    public static BuildPojo getExecution(String jobName, Integer buildId) {
        Query query=Query.query(Criteria.where("jobName").is(jobName).and("buildId").is(buildId));
        BuildPojo result=getDB().findOne(query, BuildPojo.class);
        
        return result;
    }

    public static List<String> getGlobalParas() {
        Query query=Query.query(Criteria.where("caseId").is("global"));
        Map<String,Object> result=getDB().findOne(query, Map.class,"data");
        
        return (List<String>)result.get("paras");
    }

    public static List<KeyPojo> getAllKeys() {
        List<KeyPojo> result=getDB().findAll(KeyPojo.class);
        return result;
    }

    public static List<CasePojo> getCases(List<String> casesId) {
        Query query=Query.query(Criteria.where("caseId").in(casesId));
        List<CasePojo> result=getDB().find(query, CasePojo.class);

        return result;
    }

    public static List<CaseDataPojo> getCasesData(List<String> casesId, String version) {
        Query query=Query.query(Criteria.where("caseId").in(casesId).and("version").is(version));
        List<CaseDataPojo> result=getDB().find(query, CaseDataPojo.class);

        return result;
    }
    public static Map<String,String> getAllObjects() {
        List<ObjectPojo> objs=getDB().findAll(ObjectPojo.class);
        
        Map<String,String> result=new Hashtable<String,String>(); 
        for(ObjectPojo item:objs){ 
            String key=item.getPage()+"."+item.getType()+"."+item.getName();
            result.put(key,item.getXpath());
        }
        Utils.objectsMap=(Hashtable<String,String>)result;
        return result;
    }
    public static void updateExecStatus(BuildPojo suite) {
        Query query=Query.query(Criteria.where("jobName").is(suite.getJobName()).and("buildId").is(suite.getBuildId()));
        Update update=Update.update("buildStatus", suite.getBuildStatus());
        update.set("endTime",suite.getEndTime());
        update.set("forceStop",suite.isFroceStop());
        update.set("cases", suite.getCases());

        getDB().findAndModify(query, update, BuildPojo.class);
    }

    public static void updateCaseStatus(String jobName, Integer buildId, String caseId, Utils.ExecStatus status) {
        Query query=Query.query(Criteria.where("jobName").is(jobName).and("buildId").is(buildId));
        Update update=Update.update("cases."+caseId, status);

        getDB().findAndModify(query, update, BuildPojo.class);

    }
    public static void updateAgentStatus(String jobName, Boolean isFree) {
        Query query=Query.query(Criteria.where("jobName").is(jobName));
        Update update=Update.update("status", isFree);

        getDB().findAndModify(query, update, AgentPojo.class);
    }

    public static void closeMongo(){
        if (client != null) {
            client.close();
            client = null;
        }
    }
}