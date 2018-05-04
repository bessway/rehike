package core.controller;

import java.util.ArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import utils.DBUtils;
import core.pojo.KeyPojo;


public class KeyManager{
    private ArrayList<KeyPojo> keys=null;
    public ArrayList<KeyPojo> getKeys() throws Exception{
        this.keys=new Gson().fromJson(DBUtils.getData("keys.json"), new TypeToken<ArrayList<KeyPojo>>() {
        }.getType());
        for(KeyPojo item:this.keys){
            System.out.println(item.toString());
        }
        return this.keys;
    }
    public static void main(String[] args) throws Exception{
        new KeyManager().getKeys();
    }
}