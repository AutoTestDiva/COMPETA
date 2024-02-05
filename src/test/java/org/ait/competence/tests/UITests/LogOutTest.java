package org.ait.competence.tests.UITests;

import org.ait.competence.pages.HomePage;
import org.ait.competence.pages.LandingPage;
import org.ait.competence.pages.LogInPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class LogOutTest extends TestBase {

    //private JavascriptExecutor js;

    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js;
        //JavascriptExecutor js = (JavascriptExecutor) driver;
        js = (JavascriptExecutor) driver;
        new LandingPage(driver, js).selectLogIn();
        new LogInPage(driver).logIn("Student33@gmail.com", "Qwerty007!");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@aria-label='Administrator menu']")));
    }


    @Test
    public void logOutTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.selectLogOut()
         .verifyLogInTextIsPresent("Log in to continue");
    }

}
