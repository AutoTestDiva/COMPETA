package org.ait.competence.tests.UITests;

import org.ait.competence.utils.MyListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestBase {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TestBase.class);
    String browser = System.getProperty("browser", Browser.CHROME.browserName());
    WebDriver driver;

    @BeforeMethod
    public void startLogger(Method m) {
        logger.info("****************************************************************");
        logger.info("Start method --> " + m.getName());
    }

    @BeforeMethod
    public void startLogger() {
        System.err.close();
        if (browser.equalsIgnoreCase(Browser.CHROME.browserName())) {
            driver = new ChromeDriver();
            logger.info("All test run in Chrome Browser");
        } else if (browser.equalsIgnoreCase(Browser.FIREFOX.browserName())){
            driver = new FirefoxDriver();
            logger.info("All test run in Firefox Browser");
        }

        WebDriverListener listener = new MyListener();
        driver = new EventFiringDecorator(listener).decorate(driver);

        driver.get("http://localhost:5173/");
        logger.info("The link --> " + driver.getCurrentUrl());
        logger.info("**********************************************************");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @BeforeMethod
    public void init() {
        driver = new ChromeDriver();
        driver.get("http://localhost:5173/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod(enabled = false)
    public void tearDown() {
        driver.quit();
    }

    private static void disableSeleniumLogging() {
        System.setProperty("webdriver.chrome.silentOutput", "true");
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    }

}
