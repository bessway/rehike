package utils;

import java.util.HashMap;
import java.util.List;

import core.pojo.ObjectPojo;

public class Utils{
    public static List<ObjectPojo> objects=null;
    public static HashMap<String,String> objectsMap=null;
    public static void cacheObject(List<ObjectPojo> objs){
        if(objs==null){
            return;
        }
        objects=objs;
        for(ObjectPojo item:objs){
            String key=item.getPage()+"."+item.getType()+"."+item.getName();
            objectsMap.put(key, item.getXpath());
        }
    }
    public static void updateCachedGlobal(ObjectPojo obj){
        if(Utils.objects==null){
            return;
        }
        
    }
}