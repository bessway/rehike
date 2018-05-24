package utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumUtils {
    private static Logger logger = Logger.getLogger(SeleniumUtils.class);
    private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
    private static Map<String, WebDriverWait> waits = new HashMap<String, WebDriverWait>();
    private static Integer maxWait = 10;
    private static String currDriver="";

    public static String assertEqualKey(String target, String attribute, String exValue) throws Exception {
        String actual=getAttributeKey(target, attribute);
        if(exValue.equals(actual)){
            return Utils.execPass;
        }else{
            throw new Exception("assert equal failed: actual = "+actual);
        }
    }
    public static String assertNotEqualKey(String target, String attribute, String exValue) throws Exception {
        String actual=getAttributeKey(target, attribute);
        if(!exValue.equals(actual)){
            return Utils.execPass;
        }else{
            throw new Exception("assert equal failed: actual = "+actual);
        }
    }
    public static String clickKey(String target) throws Exception {
        findElement(target).click();
        return Utils.execPass;
    }
    public static String getAttributeKey(String target,String attribute) throws Exception {
        if("text".equals(attribute)){
            return findElement(target).getText();
        }else{
            return findElement(target).getAttribute(attribute);
        }
    }
    public static String inputKey(String target, String content) throws Exception {
        WebElement tmp=findElement(target);
        tmp.clear();
        tmp.sendKeys(content);
        return Utils.execPass;
    }
    public static String navigateToKey(String url) throws Exception{
        getCurrDriver().get(url);
        return currDriver;
    }
    public static String openSiteKey(String url, String browserType) throws Exception {
        WebDriver driver = null;
        if (!drivers.containsKey(url)) {
            driver = launchBrowser(browserType);
            drivers.put(url, driver);
            WebDriverWait wait=new WebDriverWait(driver, maxWait);
            waits.put(url,wait);
        }
        drivers.get(url).get(url);
        currDriver=url;
        return url;
    }
    public static String selectKey(String target,String value) throws Exception{
        new Select(findElement(target)).selectByValue(value);
        return Utils.execPass;
    }
    public static String swithToFrameKey(String target) throws Exception{
        String handler=getCurrDriver().getWindowHandle();
        getCurrDriver().switchTo().frame(findElement(target));
        return handler;
    }
    public static String waitVisibleKey(String xpath) throws Exception{
        List<WebElement> result=null;
        //it will check present display
        result=getCurrWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath)));
        if(result!=null&&result.size()>0){
            return Utils.execPass;
        }else{
            throw new Exception("assert visible failed");
        }
    }
    public static String waitInvisibleKey(String xpath) throws Exception{
        Boolean result=false;
        //it will check present display
        result=getCurrWait().until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        if(result){
            return Utils.execPass;
        }else{
            throw new Exception("assert invisible failed");
        }
    }
    public static String closeAndSwitchBrowserKey(String url, String target) throws Exception{
        closeBrowsersKey(url);
        currDriver=target;
        getCurrDriver().manage().window().maximize();
        return target;
    }
    public static String swithToBrowserKey(String url) throws Exception{
        currDriver=url;
        getCurrDriver().manage().window().maximize();
        return url;
    }
    public static String closeBrowsersKey(String browserHandlerName) {
        try {
            if (browserHandlerName == null||browserHandlerName.equals("null")) {
                for (String driver : drivers.keySet()) {
                    drivers.get(driver).quit();
                    drivers.remove(driver);
                    waits.remove(driver);
                }
                logger.debug("close all the drivers");
            } else {
                if (!drivers.get(browserHandlerName).toString().toLowerCase().contains("null")) {
                    drivers.get(browserHandlerName).quit();
                    drivers.remove(browserHandlerName);
                    waits.remove(browserHandlerName);
                }
                logger.debug("close the driver "+ browserHandlerName);
            }
        } catch (NullPointerException e) {
            logger.debug("already quit driver "+ browserHandlerName);
        }
        return Utils.execPass;
    }
    public static String assertMatchKey(String target, String attribute, String pattern) throws Exception {
        List<WebElement> elements=findElements(target);
        for(WebElement ele:elements){
            String actual=ele.getAttribute(attribute);
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(actual);
            if(!m.find()){
                throw new Exception("assert match failed: actual = "+actual);
            }
        }
        return Utils.execPass;
    }
    public static String switchToWindowKey(String handler) throws Exception{
        getCurrDriver().switchTo().window(handler);
        return Utils.execPass;
    }
    public static String waitEnableKey(String target) throws Exception{
        if(findElement(target).isEnabled()){
            return Utils.execPass;
        }else{
            throw new Exception("assert enable failed");
        }
    }
    public static String waitDisableKey(String target) throws Exception{
        if(!findElement(target).isEnabled()){
            return Utils.execPass;
        }else{
            throw new Exception("assert disable failed");
        }
    }
    public static String getTitleKey() throws Exception{
        return getCurrDriver().getTitle();
    }
    public static String assertTitleKey(String exValue) throws Exception{
        String actual=getCurrDriver().getTitle();
        if(exValue.equals(actual)){
            return Utils.execPass;
        }else{
            throw new Exception("assert equal failed: actual = "+actual);
        }
        
    }
    public static String assertTitleMatchKey(String pattern) throws Exception{
        String actual=getCurrDriver().getTitle();
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(actual);
        if(m.find()){
            return Utils.execPass;
        }else{
            throw new Exception("assert equal failed: actual = "+actual);
        }
    }
    //TO-DO
    public static String postKey(String url, String header, String body) throws Exception {
        return url;
    }
    
    private static WebElement findElement(String xpath) throws Exception{
        List<WebElement> result=findElements(xpath);
        if(result.size()>1){
            throw new Exception("find more than one objects "+xpath);
        }
        return result.get(0);
    }
    private static List<WebElement> findElements(String xpath) throws Exception{
        List<WebElement> result=null;
        //it will check present display width
        result=getCurrWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath)));
        
        if(result==null||result.size()==0){
            throw new Exception("cannot find the objects "+xpath);
        }
        return result;
    }
    
    private static WebDriver getCurrDriver() throws Exception{
        if(drivers.size()==0 || drivers.get(currDriver)==null){
            throw new Exception("cannot find the driver: "+currDriver);
        }
        return drivers.get(currDriver);
    }
    private static Wait<WebDriver> getCurrWait() throws Exception{
        if(waits.size()==0 || waits.get(currDriver)==null){
            throw new Exception("cannot find the wait for driver, need to check whether the page is still open: "+currDriver);
        }
        return waits.get(currDriver);
    }

    public static String takeScreenshot() throws Exception{
        deleteScreenshot();
        File screenshotDir = new File(ReportUtils.reportRoot);
        if(!screenshotDir.exists()&& !screenshotDir .isDirectory()){
            screenshotDir.mkdir();
        }
        String name=String.valueOf(new Date().getTime())+".jpg";
        File path=new File(ReportUtils.reportRoot+name);
        File img=((TakesScreenshot)getCurrDriver()).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(img, path);
        
        return name;
    }
    private static void deleteScreenshot(){
        File screenshotDir = new File(ReportUtils.reportRoot);
        if(!screenshotDir.exists()){
            return;
        }
        File[] fls=screenshotDir.listFiles(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name){
                return name.endsWith(".jpg");
            }
        });
        for(File fl:fls){
            if(fl.lastModified()<new Date().getTime()-7*24*3600){
                fl.delete();
            }
        }
    }
    private static WebDriver launchBrowser(String type) throws Exception {
        WebDriver driver = null;
        String path = SeleniumUtils.class.getResource("/").toURI().getRawPath();
        logger.debug(path);
        logger.debug(System.getProperty("user.dir"));
        logger.debug(System.getProperty("os.name"));
        switch (type) {
        case "chrome":
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                System.setProperty("webdriver.chrome.driver", "/" + path + "chromedriver_2_37_0.exe");
            } else {
                System.setProperty("webdriver.chrome.driver", "/" + path + "chromedriver_2_37_0");
                File dirFile = new File("/" + path + "chromedriver_2_37_0");
                dirFile.setExecutable(true, false);
            }
            driver = new ChromeDriver();
            break;
        case "firefox":
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                System.setProperty("webdriver.firefox.marionette", "/" + path + "geckodriver_0_20_0.exe");
            } else {
                System.setProperty("webdriver.firefox.marionette", "/" + path + "geckodriver_0_20_0");
                File dirFile = new File("/" + path + "geckodriver_0_20_0");
                dirFile.setExecutable(true, false);
            }
            driver = new FirefoxDriver();
            break;
        case "ie":
            driver = new InternetExplorerDriver();
            break;
        default:
            logger.error("unsupported browser");
            throw new Exception(type + " browser is not supported");
        }
        driver.manage().window().maximize();
        return driver;
    }
}
