package utils;

import java.io.File;
import java.io.FileInputStream;

public class DBUtils{
    public static String getData(String fName) throws Exception{
        String path=DBUtils.class.getResource("/").toURI().getRawPath();
        File f=new File("/"+path+fName);
        Long fileSize=f.length();
        byte[] content=new byte[fileSize.intValue()];
        FileInputStream fis=new FileInputStream(f);
        fis.read(content);
        fis.close();
        System.out.println(new String(content));
        return new String(content);
    }
    public static void main(String[] args) throws Exception{
        getData("case.json");
    }
}