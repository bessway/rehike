package core.aop;



import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect{
    /*
    1.Logger.getLogger()是使用log4j的方式记录日志；
    2.LogFactory.getLog()则来自apache的common-logging包。
    3.这里默认的是使用springboot的日志系统
    */
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * core.service..*.*(..))")
    public void service(){}

    @Before("service()")
    public void doBeforeService(JoinPoint point){
        this.logger.debug(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName()+ "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    @After("service()")
    public void doAfterService(JoinPoint point){
        this.logger.debug(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName()+ "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Pointcut("execution(public * core.controller..*.*(..))")
    public void controller(){}

    @Before("controller()")
    public void doBeforeController(JoinPoint point){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        this.logger.debug(request.getMethod() + " " + request.getRequestURI().toString());
        this.logger.debug(Arrays.toString(point.getArgs()));
    }
    @AfterReturning(returning="ret", pointcut="controller()")
    public void doAfterController(Object ret){
        this.logger.debug(String.valueOf(ret));
    }

    @Pointcut("execution(public * core.dao..*.*(..))")
    public void dao(){}

    @Before("dao()")
    public void doBeforeDao(JoinPoint point){
        this.logger.debug(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName()+ "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    @After("dao()")
    public void doAfterDao(JoinPoint point){
        this.logger.debug(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName()+ "-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}