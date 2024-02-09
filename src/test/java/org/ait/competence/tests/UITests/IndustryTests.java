package org.ait.competence.tests.UITests;

import org.ait.competence.pages.*;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IndustryTests extends TestBase {

    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new LandingPage(driver, js).selectLogIn();
        new LogInPage(driver, js).logIn("Student33@gmail.com", "Qwerty007!");
    }

    @Test
    public void addNewIndustryPositiveTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectIndustry();
        new IndustryPage(driver, js).addIndustry("Tourism")
                .verifyNewIndustryIsPresent("Tourism");
    }

    @Test
    public void addNewIndustryPNegativeTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectIndustry();
        new IndustryPage(driver, js).addIndustry("Tourism")
                .verifyIndustryExistsMessageIsPresent("Industry with name");
    }

    @Test
    public void updateIndustryTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectIndustry();
        new IndustryPage(driver, js).updateIndustry("Transport")
                .verifyUpdatedIndustryIsPresent("Transport");
    }

    @Test
    public void deleteIndustryTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectIndustry();
        new IndustryPage(driver, js).deleteIndustry()
                .verifyDeletedIndustryIsNotPresent("Transport");
    }
}
