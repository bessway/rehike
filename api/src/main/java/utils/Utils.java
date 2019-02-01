package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Utils{
    private static Logger logger=Logger.getLogger(Utils.class);
    //缓存object,仅用于判断是否需要更新或添加object
    //添加或更新object时也会更新这个缓存
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
    public static String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}