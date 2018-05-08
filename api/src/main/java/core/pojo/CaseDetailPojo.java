package core.pojo;

import java.util.ArrayList;

public class CaseDetailPojo{
    private Integer id=null;
    private String action=null;
    private String page=null;
    private String type=null;
    private String name=null;
    private String path=null;
    private ArrayList<String> paras=null;
    private String response=null;

    public void setid(Integer id){
        this.id=id;
    }
    public void setAction(String action){
        this.action=action;
    }
    public void setPage(String page){
        this.page=page;
    }
    public void setType(String type){
        this.type=type;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPath(String path){
        this.path=path;
    }
    public void setResponse(String response){
        this.response=response;
    }
    public void setParas(ArrayList<String> paras){
        this.paras=paras;
    }
    public void addPara(String para){
        if(this.paras==null){
            this.paras=new ArrayList<String>();
        }
        this.paras.add(para);
    }
}