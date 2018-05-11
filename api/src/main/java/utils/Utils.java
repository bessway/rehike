package utils;

import java.util.HashMap;
import java.util.List;

import core.pojo.ObjectPojo;

public class Utils{
    public static HashMap<String,String> objectsMap=null;

    public static void addToObjectMap(String key, String value){
        if(objectsMap==null){
            objectsMap=new HashMap<String,String>();
        }
    }
}