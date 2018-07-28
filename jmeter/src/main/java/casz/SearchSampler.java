package casz;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import core.pojo.DaguanSearchPojo;
import core.pojo.DaguanSkuPojo;
import core.pojo.QueryOrderPojo;
import core.pojo.QuoteDetailPojo;
import core.pojo.VizseekSearchPojo;
import core.pojo.VizseekSkuPojo;
import utils.DBUtils;
import utils.ExcelUtils;
import utils.ServerUtils;
import utils.Utils;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;



public class SearchSampler extends AbstractJavaSamplerClient implements Serializable {
    private static Logger logger = Logger.getLogger(SearchSampler.class);
    private static final long serialVersionUID=1L;
    private static String token=null;
    private static Vector<String> queries=new Vector<String>();

    @Override
    public Arguments getDefaultParameters(){
        Arguments arguments=new Arguments();
        arguments.addArgument("isRefreshData","false");//是否刷新数据
        arguments.addArgument("inOrOut","in");//是否库内
        arguments.addArgument("type","excel");//文件类型
        arguments.addArgument("numOfQuery","2000");//查询文件数量
        arguments.addArgument("numInFile","5");//一个文件内的查询数
        arguments.addArgument("isRefreshQuery","false");//一个文件内的查询数
        arguments.addArgument("service","daguan");
        return arguments;
    }
    public static Vector<String> getFiles(String path,String name){
        Vector<String> files=new Vector<String>();
        try{
            File folder=Utils.readFile(path, false);
            for(File f:folder.listFiles()){
                if(f.getName().startsWith(name)){
                    files.add(f.getName());
                }
            }
        }catch(Exception e){
            logger.error("failed to create testfiles folder,"+e.getMessage());
        }
        return files;
    }
    @Override
    public void setupTest(JavaSamplerContext context) {
        Integer total=getFiles("data","quotedata").size();
        if(context.getParameter("isRefreshData").equals("true")||(total==0 && context.getParameter("isRefreshQuery").equals("true"))){
            //调用数据库获取数据，更新testfiles
            try{
                Utils.deleteFilesByName("[\\s\\S]*quotedata[\\s\\S]*xlsx");
                if(context.getParameter("inOrOut").equals("in")){
                    getData(200, false);
                }else{
                    getData(200,true);
                }
            }catch(Exception e){
                logger.error("failed to get data from DB,"+e.getMessage());
                
            }
        }
        
        try{
            if(context.getParameter("inOrOut").equals("in")){
                prepareQuery(Integer.valueOf(context.getParameter("numOfQuery")), Integer.valueOf(context.getParameter("numInFile")), false,total,context.getParameter("isRefreshQuery"));
            }else{
                prepareQuery(Integer.valueOf(context.getParameter("numOfQuery")), Integer.valueOf(context.getParameter("numInFile")), true,total,context.getParameter("isRefreshQuery"));
            }
            //获取token
            ServerUtils.init();
            if(token==null){
                Map<String,String> res=null;
                if(context.getParameter("service").equals("daguan")){
                    res=ServerUtils.getDaguanToken();
                }else{
                    res=ServerUtils.getVizseekToken();
                }
                token=res.get("access_token");
            }
        }catch(Exception e){
            logger.error("failed to generate query,"+e.getMessage());
        }
    }
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult results = new SampleResult();
        AssertionResult aresult=new AssertionResult();
        
        CloseableHttpResponse res=null;
        String fileName=null;
        try{
            if(token==null){
                throw new Exception();
            }
            Random rand=new Random();
            fileName=queries.get(rand.nextInt(queries.size()));
            results.setSampleLabel("daguan-excel");
            
            results.sampleStart();
            
            String load_path=context.getParameter("numInFile")+"/";
            if(context.getParameter("service").equals("daguan")){
                res=ServerUtils.getDaguanRecommandSku(token, Utils.readFile(load_path+fileName, false));
            }else{
                res=ServerUtils.getVizseekRecommandSku(token, Utils.readFile(load_path+fileName, false));
            }
            if(res==null){
                String num=fileName.replace(".xlsx", "").split("_")[3];
                results.setSuccessful(false);
                aresult.setFailure(true);
                aresult.setFailureMessage("response null:"+num);
            }
            
            results.setSuccessful(true);
        }catch(Exception e){
            String num=fileName.replace(".xlsx", "").split("_")[3];
            results.setSuccessful(false);
            aresult.setFailure(true);
            aresult.setFailureMessage(num+":"+e.getMessage());
            logger.error("failed to run test,"+fileName+","+e.getMessage());
        }
        results.sampleEnd();
        results.addAssertionResult(aresult);
        try{
            if(res!=null){
                if(context.getParameter("service").equals("daguan")){
                    List<DaguanSearchPojo> searchResult=null;
                    searchResult=ServerUtils.handleDaguanSearchResult(res);
                    results.setResponseData(searchResult.get(0).getHits().get(0).getSku(),null);
                    
                }else{
                    List<VizseekSearchPojo> searchResult=ServerUtils.handleVizseekSearchResult(res);
                    results.setResponseData(searchResult.get(0).getHits().get(0).getSku(),null);
                }
            }
            results.setDataType(SampleResult.TEXT);
        }catch(Exception e){
            logger.error("failed to parse daguan response,"+e.getMessage());
        }
        
        return results;
    }

    public static void getData(Integer numOfFile,Boolean isOut)throws Exception{
        Date lastDate=new Date();
        for(int i=1;i<=numOfFile;i++){
            lastDate=getOneData(i,lastDate,isOut);
        }
    }
    public static Date getOneData(Integer index,Date lastDate,Boolean isOut)throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<QuoteDetailPojo> quoteDetails=null;
        if(isOut){
            quoteDetails=DBUtils.getQuoteDetailOutter(1000,sdf.format(lastDate));
        }else{
            quoteDetails=DBUtils.getQuoteDetailInner(1000,sdf.format(lastDate));
        }
        
        if(quoteDetails==null||quoteDetails.size()==0){
            throw new Exception("no more data");
        }
        Date newDate=quoteDetails.get(quoteDetails.size()-1).getCreatedon();
        if(isOut){
            ExcelUtils.exportExcel(quoteDetails, QuoteDetailPojo.class, "outquotedata"+index+".xlsx", null);
        }else{
            ExcelUtils.exportExcel(quoteDetails, QuoteDetailPojo.class, "quotedata"+index+".xlsx", null);
        }
        
        return newDate;
    }
    public static void prepareQuery(Integer numOfQuery,Integer numInQuery,Boolean isOut,Integer dataFileNum,String isRefreshQuery) throws Exception{
        if(isRefreshQuery.equals("true")){
            Utils.deleteFilesByName("query[\\s\\S]*xlsx");
            Random rand=new Random();


            Integer fIndex=0;
            Integer iIndex=0;
            for(int i=1;i<=numOfQuery;i++){
                if(queries.size()==numOfQuery){
                    break;
                }
                fIndex=rand.nextInt(dataFileNum)+1;
                List<QuoteDetailPojo> quoteDetails=null;
                if(isOut){
                    quoteDetails=ExcelUtils.loadExcel("outquotedata"+fIndex+".xlsx", 1, QuoteDetailPojo.class);
                }else{
                    quoteDetails=ExcelUtils.loadExcel("quotedata"+fIndex+".xlsx", 1, QuoteDetailPojo.class);
                }

                if(quoteDetails.size()<=numInQuery){
                    iIndex=1;
                    numInQuery=quoteDetails.size();
                }else{
                    iIndex=rand.nextInt(quoteDetails.size()-numInQuery)+1;
                }
                List<QueryOrderPojo> queryOrders=new ArrayList<QueryOrderPojo>();
                Integer iCount=1;
                for(int j=iIndex-1;j<iIndex+numInQuery-1;j++){
                    QuoteDetailPojo qd=quoteDetails.get(j);
                    QueryOrderPojo order=new QueryOrderPojo();
                    order.setIndex(iCount++);
                    String type="";
                    if(qd.getReqmBrand()!=null){
                        type=qd.getReqmBrand();
                    }
                    if(qd.getReqmProModels()!=null){
                        type=type+" "+qd.getReqmProModels();
                    }
                    order.setType(type);

                    if(qd.getReqmProName()==null){
                        order.setName("");
                    }else{
                        order.setName(qd.getReqmProName());
                    }
                    if(qd.getReqmProUnit()==null){
                        order.setUnit("");
                    }else{
                        order.setUnit(qd.getReqmProUnit());
                    }
                    if(qd.getReqmQuantity()==null){
                        order.setQuantity(1);
                    }else{
                        order.setQuantity(qd.getReqmQuantity());
                    }
                    
                    queryOrders.add(order);
                }
                String queryName="query_"+fIndex+"_"+iIndex+"_"+numInQuery+".xlsx";
                ExcelUtils.exportExcel(queryOrders, QueryOrderPojo.class, queryName, null);
                queries.add(queryName);
            }


        }else{
            String load_path=numInQuery+"/";
            queries=getFiles(load_path,"query");
        }
    }
    public static void compareDaguanResult(List<DaguanSearchPojo> result,String filename) throws Exception{
        List<QuoteDetailPojo> data=findDataInFile(filename);
        List<QueryOrderPojo> query=ExcelUtils.loadExcel(filename, 1, QueryOrderPojo.class);
        for(int i=0;i<data.size();i++){
            Integer position=isQueryItemExistInDaguan(data.get(i),i,result);
            if(position==-1){
                query.get(i).setComment("sku not returned,"+data.get(i).getSkuNo());
            }else if(position/10!=i){
                query.get(i).setComment("search result is not sorted,"+data.get(i).getSkuNo()+" should be at "+String.valueOf(i)+", but actually at returned at "+String.valueOf(position/10));
                if(position%10!=0){
                    query.get(i).setComment(query.get(i).getComment()+" and sku not on the top:"+data.get(i).getSkuNo()+",actual position is "+String.valueOf(position%10));
                }
            }else if(position%10!=0){
                query.get(i).setComment("sku not on the top:"+data.get(i).getSkuNo()+",actual position is "+String.valueOf(position%10));
            }else{
                query.get(i).setComment("sku matched");
            }
            query.get(i).setSku(data.get(i).getSkuNo());
        }
        ExcelUtils.exportExcel(query, QueryOrderPojo.class, "daguanresult_"+filename, null);
    }
    
    public static Integer isQueryItemExistInDaguan(QuoteDetailPojo item,Integer index,List<DaguanSearchPojo> result){
        String sku=item.getSkuNo();
        List<DaguanSkuPojo> expectResult=result.get(index).getHits();
        for(int j=0;j<expectResult.size();j++){
            if(sku.equals(expectResult.get(j).getSku())){
                return index*10+j;
            }
        }

        for(int i=0;i<result.size();i++){
            List<DaguanSkuPojo> scoredResult=result.get(i).getHits();
            for(int j=0;j<scoredResult.size();j++){
                if(sku.equals(scoredResult.get(j).getSku())){
                    return i*10+j;
                }
            }
        }
        return -1;
    }
    public static void compareVizseekResult(List<VizseekSearchPojo> result,String filename) throws Exception{
        List<QuoteDetailPojo> data=findDataInFile(filename);
        List<QueryOrderPojo> query=ExcelUtils.loadExcel(filename, 1, QueryOrderPojo.class);
        for(int i=0;i<data.size();i++){
            //验证是否每个预期的sku都出现在结果集
            Integer position=-1;
            try{
                position=isQueryItemExistInVizseek(data.get(i),i,result);
            }catch(Exception e){
                System.out.println(e);
            }
            if(position==-1){
                query.get(i).setComment("sku not returned,"+data.get(i).getSkuNo());
            }else if(position/10!=i){
                query.get(i).setComment("search result is not sorted,"+data.get(i).getSkuNo()+" should be at "+String.valueOf(i)+", but actually at returned at "+String.valueOf(position/10));
                if(position%10!=0){
                    query.get(i).setComment(query.get(i).getComment()+" and sku not on the top:"+data.get(i).getSkuNo()+",actual position is "+String.valueOf(position%10));
                }
            }else if(position%10!=0){
                query.get(i).setComment("sku not on the top:"+data.get(i).getSkuNo()+",actual position is "+String.valueOf(position%10));
            }else{
                query.get(i).setComment("sku matched");
            }
            query.get(i).setSku(data.get(i).getSkuNo());
        }
        ExcelUtils.exportExcel(query, QueryOrderPojo.class, "vizseekresult_"+filename, null);
    }
    public static Integer isQueryItemExistInVizseek(QuoteDetailPojo item,Integer index,List<VizseekSearchPojo> result){
        String sku=item.getSkuNo();
        List<VizseekSkuPojo> expectResult=result.get(index).getHits();
        for(int j=0;j<expectResult.size();j++){
            if(sku.equals(expectResult.get(j).getSku())){
                return index*10+j;
            }
        }
        for(int i=0;i<result.size();i++){
            List<VizseekSkuPojo> scoredResult=result.get(i).getHits();
            for(int j=0;j<scoredResult.size();j++){
                if(sku.equals(scoredResult.get(j).getSku())){
                    return i*10+j;
                }
            }
        }
        return -1;
    }
    public static List<QuoteDetailPojo> findDataInFile(String queryFileName) throws Exception{
        queryFileName=queryFileName.replace("query", "").replace(".xlsx", "");
        String[] indexes=queryFileName.split("_");
        List<QuoteDetailPojo> quoteDetails=ExcelUtils.loadExcel("quotedata"+indexes[1]+".xlsx", 1, QuoteDetailPojo.class);
        quoteDetails=quoteDetails.subList(Integer.parseInt(indexes[2])-1, Integer.parseInt(indexes[2])+Integer.parseInt(indexes[3])-1);

        return quoteDetails;
    }
    public static void run200Test(String service) throws Exception{
        String token=null;
        ServerUtils.init();
        CloseableHttpResponse res=null;
        if(service.equals("daguan")){
            token=ServerUtils.getDaguanToken().get("access_token");
        }else{
            token=ServerUtils.getVizseekToken().get("access_token");
        }
        for(String queryfilename:getFiles("200","query")){
            System.out.println(queryfilename);
            //try{
                if(service.equals("daguan")){
                    res=ServerUtils.getDaguanRecommandSku(token, Utils.readFile(queryfilename, false));
                    List<DaguanSearchPojo> daguanResult=ServerUtils.handleDaguanSearchResult(res);
                    compareDaguanResult(daguanResult,queryfilename);
                }else{
                    res=ServerUtils.getVizseekRecommandSku(token, Utils.readFile(queryfilename, false));
                    List<VizseekSearchPojo> vizseekResult=ServerUtils.handleVizseekSearchResult(res);
                    compareVizseekResult(vizseekResult, queryfilename);
                }
            //}catch(Exception e){
            //    System.out.println(e);
            //}
        }
    /*    token=ServerUtils.getVizseekToken().get("access_token");
        for(String queryfilename:getFiles("query")){
            res=ServerUtils.getVizseekRecommandSku(token, Utils.readFile(queryfilename, false));
            List<VizseekSearchPojo> vizseekResult= ServerUtils.handleVizseekSearchResult(res);
            compareVizseekResult(vizseekResult,queryfilename);
        }
        */
    }
    public static void main( String[] args ) throws Exception{
        PropertyConfigurator.configure(Utils.readPropery("log4j.properties"));
        //prepare data for perfomance
            
        Arguments params = new Arguments(); 
        params.addArgument("isRefreshData","false");
        params.addArgument("inOrOut","in");
        params.addArgument("type","excel");
        params.addArgument("numOfQuery","1");
        params.addArgument("numInFile","9999");
        params.addArgument("isRefreshQuery","false");
        params.addArgument("service","daguan");
        JavaSamplerContext arg0 = new JavaSamplerContext(params); 
        SearchSampler test = new SearchSampler(); 
        test.setupTest(arg0);
        System.out.println(new Date().getTime());
        Long start=new Date().getTime();
        if(token!=null){
            test.runTest(arg0);
        }
        //run200Test("vizseek");
        Long end=new Date().getTime();
        System.out.println(end-start);
    }
}
