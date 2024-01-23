package org.ait.competence.tests.UITests;

import org.ait.competence.pages.HomePage;
import org.ait.competence.pages.LandingPage;
import org.ait.competence.pages.LogInPage;
import org.ait.competence.pages.SignUpPage;
import org.ait.competence.tests.UITests.TestBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignUpTests extends TestBase {
    @BeforeMethod
    public void precondition() {
        new HomePage(driver).selectSignUp();
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
