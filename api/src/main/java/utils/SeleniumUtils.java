package utils;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

public class SeleniumUtils {
    private static Logger logger = Logger.getLogger(SeleniumUtils.class);
    private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
    private static Map<String, WebDriverWait> waits = new HashMap<String, WebDriverWait>();
    private static Integer maxWait = 10;

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
    
    public static String openSite(String url, String browserType) throws Exception {
        /*
         * WebDriver driver=null; if(!drivers.containsKey(url)){
         * driver=launchBrowser(browserType); drivers.put(url, driver); }
         * drivers.get(url).get(url);
         */
        logger.debug("openSite");
        logger.debug(url);
        logger.debug(browserType);
        return url;
    }
    
    public static void input(String target, String content) throws Exception {
        logger.debug("input");
        logger.debug(target);
        logger.debug(content);
    }
   
    public static void click(String target) throws Exception {
        logger.debug("click");
        logger.debug(target);
    }
    
    public static String getText(String target) throws Exception {
        logger.debug("getText");
        logger.debug(target);
        return "text in getText";
    }
    
    public static String assertEqual(String target,String attribute,String exValue) throws Exception {
        logger.debug("assertEqual");
        logger.debug(target);
        logger.debug(attribute);
        logger.debug(exValue);
        return "assert result";
    }

    public static String post(String url, String header, String body) throws Exception {
        logger.debug("post url:" + url);
        logger.debug("post header:" + header);
        logger.debug("post body:" + body);
        return url;
    }

    public static void closeDrivers(String browserHandlerName) {
        try {
            if (browserHandlerName == null) {
                for (String driver : drivers.keySet()) {
                    drivers.get(driver).quit();
                }
            } else {
                if (!drivers.get(browserHandlerName).toString().toLowerCase().contains("null")) {
                    drivers.get(browserHandlerName).quit();
                }
            }
            logger.debug("close all the drivers");
        } catch (NullPointerException e) {
            logger.debug("already quit driver");
        }
    }
}
