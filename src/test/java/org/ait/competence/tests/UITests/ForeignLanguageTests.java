package org.ait.competence.tests.UITests;

import org.ait.competence.pages.*;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ForeignLanguageTests extends TestBase {

    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new LandingPage(driver, js).selectLogIn();
        new LogInPage(driver, js).logIn("Student33@gmail.com", "Qwerty007!");
    }

    @Test
    public void addNewForeignLanguagePositiveTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguage();
        new ForeignLanguagePage(driver, js).addForeignLanguage("Japanese")
                .verifyNewForeignLanguageIsPresent("Japanese");
    }

    @Test
    public void addNewForeignLanguageNegativeTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguage();
        new ForeignLanguagePage(driver, js).addForeignLanguage("Japanese")
                .verifyForeignLanguageExistsMessageIsPresent("Foreign language with name");
    }

    @Test
    public void updateForeignLanguageTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguage();
        new AdministrationPage(driver, js).updateForeignLanguage("Greek")
                .verifyUpdatedForeignLanguageIsPresent("Greek");
    }

    @Test
    public void deleteForeignLanguageTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguage();
        new ForeignLanguagePage(driver, js).deleteForeignLanguage()
                .verifyDeletedForeignLanguageIsNotPresent("Bachelor`s Degree");
    }

}
