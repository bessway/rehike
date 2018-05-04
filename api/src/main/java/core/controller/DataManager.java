package core.controller;

import java.util.ArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import utils.DBUtils;
import core.pojo.CaseDataPojo;

public class DataManager{
    private ArrayList<CaseDataPojo> casedatas=null;
    public ArrayList<CaseDataPojo> getCaseDatas() throws Exception{
        this.casedatas=new Gson().fromJson(DBUtils.getData("casedata.json"), new TypeToken<ArrayList<CaseDataPojo>>() {
        }.getType());
        for(CaseDataPojo item:this.casedatas){
            System.out.println(item.toString());
        }
        return this.casedatas;
    }
    public static void main(String[] args) throws Exception{
        new DataManager().getCaseDatas();
    }
}