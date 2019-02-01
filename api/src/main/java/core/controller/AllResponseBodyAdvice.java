package core.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class AllResponseBodyAdvice implements ResponseBodyAdvice{
    private Gson gson = new Gson();
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("status",200);
        result.put("data", o);
        return gson.toJson(result);
    }
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }
}