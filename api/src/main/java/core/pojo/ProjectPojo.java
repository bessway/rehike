package core.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="projects")
public class ProjectPojo{
    private Long refId=null;
    private String label=null;

    public String toString(){
        return "refId:"+String.valueOf(this.refId)
        +"\nlabel:"+this.label;
    }
}