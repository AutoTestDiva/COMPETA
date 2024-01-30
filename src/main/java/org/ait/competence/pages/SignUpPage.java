package org.ait.competence.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignUpPage extends BasePage {
    public SignUpPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id=':r1:']")
    WebElement emailField;

    @FindBy(xpath = "//input[@id=':r3:']")
    WebElement passwordField;

    @FindBy(xpath = "//input[@id=':r5:']")
    WebElement repeatPasswordField;

    @FindBy(xpath = "//button[text()='Register']")
    WebElement registerButton;

    public HomePage signUp(String email, String password, String repeatPassword) {
        type(emailField, email);
        type(passwordField, password);
        type(repeatPasswordField, repeatPassword);
        click(registerButton);
        JavascriptExecutor js = null;
        return new HomePage(driver, js);
    }

    public SignUpPage signUpNegative(String email, String password, String repeatPassword) {
        type(emailField, email);
        type(passwordField, password);
        type(repeatPasswordField, repeatPassword);
        click(registerButton);
        return this;
    }

    @FindBy(xpath = "//div[contains(text(),\"User with this email 'student1@gmail.com' already \")]")
    WebElement errorMessageUserExists;

    public SignUpPage verifyUserExistsMessageIsPresent() {
        new SignUpPage(driver).isTextPresent(errorMessageUserExists,
                "User with this email 'student1@gmail.com' already exists");
        return this;
    }

}
