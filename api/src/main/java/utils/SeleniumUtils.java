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

public class SeleniumUtils {
    public static Logger logger = Logger.getLogger(SeleniumUtils.class);
    private static HashMap<String, WebDriver> drivers = new HashMap<String, WebDriver>();
    private static HashMap<String, WebDriverWait> waits = new HashMap<String, WebDriverWait>();
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

    public static String openSite(String url,String browserType) throws Exception{
        WebDriver driver=null;
        if(!drivers.containsKey(url)){
            driver=launchBrowser(browserType);
            drivers.put(url, driver);
        }
        drivers.get(url).get(url);
         
        return url;
    }
    public static String post(String url,String header,String body) throws Exception{
        System.out.println("post url:"+url);
        System.out.println("post header:"+header);
        System.out.println("post body:"+body);
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
        } catch (NullPointerException e) {
            logger.debug("already quit driver");
        }
    }
}
