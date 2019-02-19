package core.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AllResponseBodyAdvice implements ResponseBodyAdvice{
    private Logger logger = LoggerFactory.getLogger(AllResponseBodyAdvice.class);
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Map<String,Object> result = null;
        if(o instanceof String){
            return "{\"status\":\"success\"}";
        //报运行异常
        }else if(o instanceof Exception){
            logger.error("running error", o);
            result = new HashMap<String,Object>();
            result.put("status","failed");
            result.put("error", ((Exception)o).getMessage());
            return result;
        //框架返回错误
        }else if(o instanceof LinkedHashMap){
            if(((LinkedHashMap)o).containsKey("path")){
                return o;
            }
        }

        result = new HashMap<String,Object>();
        result.put("status","success");
        result.put("data", o);
        return result;
    }
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Exception errorHandler(Exception ex) {
        return ex;
    }
}