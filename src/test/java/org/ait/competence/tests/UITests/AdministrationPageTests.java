package org.ait.competence.tests.UITests;

import org.ait.competence.pages.AdministrationPage;
import org.ait.competence.pages.HomePage;
import org.ait.competence.pages.LandingPage;
import org.ait.competence.pages.LogInPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdministrationPageTests extends TestBase {

    @BeforeMethod
    public void precondition () {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new LandingPage(driver, js).selectLogIn();
        new LogInPage(driver, js).logIn("Student33@gmail.com", "Qwerty007!");
    }

    @Test
    public void addNewDriverLicensePositiveTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).addDriverLicense("D");
    }

    @Test
    public void addNewDriverLicenseNegativeTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).addDriverLicense("D")
                .verifyDriverLicenseExistsMessageIsPresent("Driver licence with name");
    }

    @Test
    public void updateDriverLicenseTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).updateDriverLicense("M")
                .verifyNewDriverLicenseIsPresent("M");

    }

    @Test
    public void deleteDriverLicenseTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).deleteDriverLicense()
                .verifyNewDriverLicenseIsNotPresent("M");
    }

}
