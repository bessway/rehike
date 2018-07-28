package utils;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.security.MessageDigest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import java.nio.charset.StandardCharsets;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import core.pojo.DaguanSearchPojo;
import core.pojo.VizseekSearchPojo;


public class ServerUtils {
    private static String agentConfigFile = "agent.properties";
    private static Logger logger = Logger.getLogger(ServerUtils.class);
    private static Properties aProperty=null;
    private static Gson gson=new GsonBuilder().enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f' };

    public static void init() throws Exception{
        try{
            aProperty = Utils.readPropery(agentConfigFile);
        }catch(Exception e){
            logger.debug("cannot find config file: "+agentConfigFile);
            throw new Exception("cannot find config file: "+agentConfigFile);
        }
    }

    private static CloseableHttpResponse callMethod(CloseableHttpClient client,String host,String method,HttpRequestBase request) throws Exception{
        CloseableHttpResponse res=null;
        if(request.getHeaders("Content-Type")==null){
            request.addHeader("Content-Type", "application/json;charset=UTF-8");
        }
        request.addHeader("Accept", "application/json;charset=UTF-8");
        String url="http://" + host+method;
        try{
            request.setURI(URI.create(url));
            
            res =client.execute(request);
            Integer status = res.getStatusLine().getStatusCode();
            if (status != 200) {
                throw new Exception("call " + method+" failed, status code "+String.valueOf(status));
            }
        }catch(Exception e){
            logger.debug(e.getMessage()+"\n"+e.getCause());
            throw new Exception(e.getMessage());
        }
        return res;
    }
    private static CloseableHttpResponse callDaguanMethod(String method,HttpRequestBase request) throws Exception{
        return callMethod(HttpClients.createDefault(),aProperty.getProperty("daguan.host"),method,request);
    }
    private static CloseableHttpResponse callVizsseekMethod(String method,HttpRequestBase request) throws Exception{
        return callMethod(HttpClients.createDefault(),aProperty.getProperty("vizseek.host"),method,request);
    }
    public static Map<String,String> getDaguanToken() throws Exception{
        String method="/api/auth";
        Map<String,String> getToken=new HashMap<String,String>();
        getToken.put("client_id",aProperty.getProperty("daguan.client_id"));
        getToken.put("password",aProperty.getProperty("daguan.password"));
        getToken.put("username",aProperty.getProperty("daguan.user"));
        getToken.put("grant_type","password");

        HttpPost post=new HttpPost();
        post.setEntity(new StringEntity(gson.toJson(getToken),"utf-8"));
        CloseableHttpResponse res=callDaguanMethod(method, post);
        Map<String,String> result=gson.fromJson(EntityUtils.toString(res.getEntity()),new TypeToken<Map<String,String>>() {
        }.getType());
        res.close();
        return result; 
    }
    public static CloseableHttpResponse getDaguanRecommandSku(String token,File f) throws Exception{
        String method="/api/search?customAPI=10&fileExtension=xlsx&filterStr=uid%253D30%2526PS%253D5%2526fileType%253Dimage&scene=demo&searchTypeStr=file";

        //File f=Utils.readFile(fileName, false);
        HttpPut put=new HttpPut();
        put.setHeader("Authorization", "bearer "+token);
        put.setHeader("Connection","keep-alive");

        MultipartEntityBuilder builder=MultipartEntityBuilder.create();
        builder.addBinaryBody("file", f, ContentType.create("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), f.getName());
        put.setEntity(builder.build());

        return callDaguanMethod(method, put);
    }
    public static List<DaguanSearchPojo> handleDaguanSearchResult(CloseableHttpResponse res) throws Exception{
        if(res==null){
            throw new Exception("response is null");
        }
        String result=EntityUtils.toString(res.getEntity(),"UTF-8");
        EntityUtils.consume(res.getEntity());
        res.close();
        Map<String,Object> reply=gson.fromJson(result,new TypeToken<Map<String,Object>>() {}.getType());
        result=gson.toJson(reply.get("CustomReply"));
        Map<String,List<DaguanSearchPojo>> output=gson.fromJson(result,new TypeToken<Map<String,List<DaguanSearchPojo>>>() {}.getType());

        return output.get("Output");
    }
    public static Map<String,String> getVizseekToken() throws Exception{
        String method="/api/token";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("grant_type", "password"));
		params.add(new BasicNameValuePair("client_id", aProperty.getProperty("vizseek.client_id")));
		params.add(new BasicNameValuePair("username", aProperty.getProperty("vizseek.user")));
		params.add(new BasicNameValuePair("password", encode(aProperty.getProperty("vizseek.password"))));
        HttpPost post=new HttpPost();

        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse res=callVizsseekMethod(method, post);
        Map<String,String> result=gson.fromJson(EntityUtils.toString(res.getEntity()),new TypeToken<Map<String,String>>() {
        }.getType());
        res.close();
        return result; 
    }
    public static CloseableHttpResponse getVizseekRecommandSku(String token,File f) throws Exception{
        String method="/api/Search?searchTypeStr=file&customAPI=10&fileExtension=.xlsx&filterStr=uid%3D"+aProperty.getProperty("vizseek.company_id")+"%26PS%3D5%26fileType%3Dimage";

        //File f=Utils.readFile(fileName, false);
        HttpPut put=new HttpPut();
        put.setHeader("Authorization", "bearer "+token);
        put.setHeader("Connection","keep-alive");
        put.setHeader("Content-Type", "application/json;charset=UTF-8");

        put.setEntity(new StringEntity("\"[\\\"" + encodeFileToBase64Binary(f) + "\\\"]\""));

        return callVizsseekMethod(method, put);
    }
    public static List<VizseekSearchPojo> handleVizseekSearchResult(CloseableHttpResponse res) throws Exception{
        if(res==null){
            throw new Exception("response is null");
        }
        String result=EntityUtils.toString(res.getEntity(),"UTF-8");
        EntityUtils.consume(res.getEntity());
        res.close();
        Map<String,Object> reply=gson.fromJson(result,new TypeToken<Map<String,Object>>() {}.getType());
        result=String.valueOf(reply.get("CustomReply"));
        Map<String,List<VizseekSearchPojo>> output=gson.fromJson(result,new TypeToken<Map<String,List<VizseekSearchPojo>>>() {}.getType());

        return output.get("Output");
    }
    private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// 把密文转换成十六进制的字符串形式
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}
	public static String encode(String str) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(str.getBytes());
			return getFormattedText(messageDigest.digest());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    private static String encodeFileToBase64Binary(File file) throws Exception {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}