package org.ait.competence.tests;

import org.ait.competence.pages.HomePage;
import org.ait.competence.pages.SignUpPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignUpTests extends TestBase {

    @BeforeMethod
    public void precondition() {
        new HomePage(driver).selectSignUp();
    }

   /* @Test
    public void SignUpPositiveTest() {
        new SignUpPage(driver).signUp("student3@gmail.com", "Qwerty007!",
                "Qwerty007!")
                .verifyRegisteredSinceText("Registered since");
    }*/

    @Test
    public void SignUpNegativeTest() {
        new SignUpPage(driver).signUpNegative("student5@gmail", "Qwerty007!",
                "Qwerty007!")
                .verifyErrorMessageIsPresent();
    }

}
