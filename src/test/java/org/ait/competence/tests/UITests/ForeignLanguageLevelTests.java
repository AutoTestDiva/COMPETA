package org.ait.competence.tests.UITests;

import org.ait.competence.pages.*;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ForeignLanguageLevelTests extends TestBase {

    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new LandingPage(driver, js).selectLogIn();
        new LogInPage(driver, js).logIn("Student33@gmail.com", "Qwerty007!");
    }

    @Test
    public void addNewForeignLanguageLevelPositiveTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguageLevel();
        new ForeignLanguageLevelPage(driver, js).addForeignLanguageLevel("C3")
                .verifyNewForeignLanguageLevelIsPresent("C3");
    }

    @Test
    public void addNewForeignLanguageLevelNegativeTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguageLevel();
        new ForeignLanguageLevelPage(driver, js).addForeignLanguageLevel("C3")
                .verifyForeignLanguageLevelExistsMessageIsPresent("Foreign language level with name");
    }

    @Test
    public void updateForeignLanguageLevelTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguageLevel();
        new ForeignLanguageLevelPage(driver, js).updateForeignLanguageLevel("C4")
                .verifyUpdatedForeignLanguageLevelIsPresent("C4");
    }

    @Test
    public void deleteForeignLanguageLevelTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectForeignLanguageLevel();
        new ForeignLanguageLevelPage(driver, js).deleteForeignLanguageLevel()
                .verifyDeletedForeignLanguageLevelIsNotPresent("C4");
    }
}
