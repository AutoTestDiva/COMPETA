package org.ait.competence.tests.UITests;

import org.ait.competence.pages.HomePage;
import org.ait.competence.pages.SignUpPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignUpTests extends TestBase {
    @BeforeMethod
    public void precondition() {
        JavascriptExecutor js = null;
        new HomePage(driver, js).selectSignUp();
    }

    @Test
    public void SignUpPositiveTest() {
        new SignUpPage(driver).signUp("student23@gmail.com", "Qwerty007!",
                        "Qwerty007!")
                //.isLogInPageOpen("Log in to continue");
                .verifyLogInTextIsPresent("Log in to continue");
    }

    @Test
    public void SignUpNegativeTest() {
        new SignUpPage(driver).signUpNegative("student1@gmail.com", "Qwerty007!",
                        "Qwerty007!")
                .verifyUserExistsMessageIsPresent();
    }

}
