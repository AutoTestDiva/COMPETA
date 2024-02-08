package org.ait.competence.tests.UITests;

import org.ait.competence.pages.*;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EducationLevelTests extends TestBase {

    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new LandingPage(driver, js).selectLogIn();
        new LogInPage(driver, js).logIn("Student33@gmail.com", "Qwerty007!");
    }

    @Test
    public void addNewEducationLevelPositiveTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectEducationLevel();
        new EducationLevelPage(driver, js).addEducationLevel("Primary school")
                .verifyNewEducationLevelIsPresent("Primary school");
    }

    @Test
    public void addNewEducationLevelNegativeTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).selectEducationLevel();
        new EducationLevelPage(driver, js).addEducationLevel("Primary school")
                .verifyEducationLevelExistsMessageIsPresent("Education level with name");
    }

    @Test
    public void updateEducationLevelTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).updateEducationLevel("Bachelor`s Degree")
                .verifyUpdatedEducationLevelIsPresent("Bachelor`s Degree");
    }

    @Test
    public void deleteEducationLevelTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        HomePage homePage = new HomePage(driver, js);
        homePage.scrollToTop();
        homePage.scrollToElement(homePage.administratorMenuButton);
        homePage.selectAdministratorMenu();
        homePage.selectAdministration();
        new AdministrationPage(driver, js).deleteEducationLevel()
                .verifyDeletedEducationLevelIsNotPresent("Bachelor`s Degree");
    }

}
