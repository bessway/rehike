package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

public class Utils{
    public static Hashtable<String,String> objectsMap=null;
    public enum ExecStatus{
        READYTOSTART,
        RUNNING,
        FAILED,
        SUCCESS,
        EXCEPTION,
        FAILEDTOSTART,
        FORCESTOP,
    }
    public final static String execPass="exec pass";
    public final static String execFail="exec fail";
    public final static String execException="exec stopped";
    public final static String gParaSymbol="%%";
    public final static String sParaSymbol="@@";

    public static void addToObjectMap(String key, String value){
        if(objectsMap==null){
            objectsMap=new Hashtable<String,String>();
        }
        objectsMap.put(key,value);
    }
    public static Properties readPropery(String fileName) throws Exception{
        String path=Utils.class.getResource("/").toURI().getRawPath();
        path=URLDecoder.decode(path, "UTF-8");
        InputStream io=new BufferedInputStream(new FileInputStream(new File("/"+path+fileName)));
        Properties result= new Properties();
        result.load(io);
        return result;
    }
}