package core.pojo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="uiobjects")
public class Uiobject{
    @Field("_id")
    private String uiObjectId = null;
    private String uiObjectPage = null;
    private String uiObjectType = null;
    private String uiObjectName = null;
    private String uiObjectPath = null;

    public void setUiObjectId(String uiObjectId){
        this.uiObjectId=uiObjectId;
    }
    public String getUiObjectId(){
        return this.uiObjectId;
    }

    public void setUiObjectPage(String uiObjectPage){
        this.uiObjectPage=uiObjectPage;
    }
    public String getUiObjectPage(){
        return this.uiObjectPage;
    }

    public void setUiObjectType(String uiObjectType){
        this.uiObjectType=uiObjectType;
    }
    public String getUiObjectType(){
        return this.uiObjectType;
    }

    public void setUiObjectName(String uiObjectName){
        this.uiObjectName=uiObjectName;
    }
    public String getUiObjectName(){
        return this.uiObjectName;
    }

    public void setUiObjectPath(String uiObjectPath){
        this.uiObjectPath=uiObjectPath;
    }
    public String getUiObjectPath(){
        return this.uiObjectPath;
    }
    public Uiobject(String objId){
        this.uiObjectId=objId;
    }
}