package org.ait.competence.tests.UITests;

import org.ait.competence.pages.LandingPage;
import org.ait.competence.pages.LogInPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LogInTests extends TestBase {
    @BeforeMethod
    public void precondition() {
        new LandingPage(driver).selectLogIn();
    }

    @Test
    public void logInPositiveTest() {
        new LogInPage(driver)
                .logIn("Student1@gmail.com", "Qwerty007!")
                .verifyLogOutBtnIsPresent();
    }

    @Test
    public void logInNegativeTest() {
        new LogInPage(driver)
                .logIn("Student1@gmail.com", "Qwerty007")
                .verifyErrorMessageIsPresent();
    }

}
