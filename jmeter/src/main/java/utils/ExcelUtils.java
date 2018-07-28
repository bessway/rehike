package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;

public class ExcelUtils{
    public static void exportExcel(List<?> content,Class<?> pojoClass,String fileName,String title) throws Exception{
        ExportParams paras=new ExportParams(null,"orders");
        if(fileName.endsWith("xlsx")){
            paras.setType(ExcelType.XSSF);
        }else{
            paras.setType(ExcelType.HSSF);
        }
        
        Workbook workbook=ExcelExportUtil.exportExcel(paras, pojoClass, content);
        File f=Utils.createFile(fileName,false);
        workbook.write(new FileOutputStream(f));
        workbook.close();
    }
    public static <T> List<T> loadExcel(File file,Integer headerRowNum,Class<T> pojoClass)throws Exception{
        if(!file.exists()){
            throw new Exception("file "+file.getName()+" not exist");
        }
        ImportParams paras=new ImportParams();
        paras.setHeadRows(headerRowNum);
        paras.setNeedVerfiy(false);
        paras.setTitleRows(0);
        List<T> result=null;
        result=ExcelImportUtil.importExcel(file, pojoClass, paras);
        
        return result;
    }

    public static <T> List<T> loadExcel(String fileName, Integer headerRowNum,Class<T> pojoClass)throws Exception{
        return loadExcel(Utils.readFile(fileName, false), headerRowNum, pojoClass);
    }
}