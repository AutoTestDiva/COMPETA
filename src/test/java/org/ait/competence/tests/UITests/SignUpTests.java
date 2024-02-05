package org.ait.competence.tests.UITests;

import org.ait.competence.pages.HomePage;
import org.ait.competence.pages.SignUpPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignUpTests extends TestBase {
    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new HomePage(driver, js).selectSignUp();
    }

    @Test
    public void SignUpPositiveTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new SignUpPage(driver, js).signUp("Will","Student52@gmail.com", "Qwerty007!",
                        "Qwerty007!")
                .verifyLogInTextIsPresent("Log in to continue");
    }

    @Test
    public void SignUpNegativeTest() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        new SignUpPage(driver, js).signUpNegative("Will","Student52@gmail.com", "Qwerty007!",
                        "Qwerty007!")
                .verifyUserExistsMessageIsPresent();
    }

}
