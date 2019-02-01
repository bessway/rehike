package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class Utils{
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