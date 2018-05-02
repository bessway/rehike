package core;

import java.util.ArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import utils.DBUtils;
import pojo.CasePojo;


public class TestManager {
    ArrayList<CasePojo> cases = null;

    public void buildCase() throws Exception {
        this.cases = new Gson().fromJson(DBUtils.getData("case.json"), new TypeToken<ArrayList<CasePojo>>() {
        }.getType());
        for(CasePojo item:this.cases){
            System.out.println(item.toString());
        }
    }
    public static void main(String[] args) throws Exception{
        new TestManager().buildCase();
    }
}