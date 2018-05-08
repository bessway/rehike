package core.pojo;


import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="hierachy")
public class HierachyPojo{
    @Field("_id")
    private String refId=null;
    private String label=null;
    private String parentId=null;

    public String toString(){
        return "refId:"+this.refId
        +"\nlabel:"+this.label
        +"\nparent:"+this.parentId;
    }
}