package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

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

    public static String buildPath(String fileName,Boolean isResource) throws Exception{
        String path="";
        //if(isResource){
        //    path=Utils.class.getResource("/"+fileName).toURI().toString();
        //    System.out.println(Utils.class.getResource("/"+fileName).toURI());
        //    return path;
            //path=Utils.class.getResourceAsStream("/"+fileName);
        //}else{
            path="/testfiles/";
            path=URLDecoder.decode(path, "UTF-8");
            File folder=new File(path);
            if(!folder.exists()){
                folder.mkdir();
            }
            return path+fileName; 
        //}
    }
    public static Properties readPropery(String fileName) throws Exception{
        InputStream io=new BufferedInputStream(new FileInputStream(readFile(fileName,true)));
        Properties result= new Properties();
        result.load(io);
        io.close();
        return result;
    }
    public static File readFile(String fileName,Boolean isResource) throws Exception{
        File f=null;
        //if(isResource){
        //    f= new File(URI.create(buildPath(fileName,isResource)));
        //}else{
            f= new File(buildPath(fileName,isResource));
        //}
        
        if(!f.exists()){
            throw new Exception("file not exist: "+fileName);
        }
        return f;
    }
    public static File createFile(String fileName,Boolean isResource) throws Exception{
        File file=new File(buildPath(fileName,isResource));
        file.createNewFile();
        return file;
    }
    public static void deleteFilesByName(String regx) throws Exception{
        File folder=new File(Utils.buildPath("",false));
        for(File f:folder.listFiles()){
            if(f.getName().matches(regx)){
                f.delete();
            }
        }
    }
}