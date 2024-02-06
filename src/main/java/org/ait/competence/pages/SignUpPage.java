package org.ait.competence.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpPage extends BasePage {

    private final JavascriptExecutor js;
    public SignUpPage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    @FindBy(xpath = "//input[@id=':r1:']")
    WebElement nickNameField;
    @FindBy(xpath = "//input[@id=':r3:']")
    WebElement emailField;

    @FindBy(xpath = "//input[@id=':r5:']")
    WebElement passwordField;

    @FindBy(xpath = "//input[@id=':r7:']")
    WebElement repeatPasswordField;

    @FindBy(xpath = "//body/div[@id='root']/div[1]/div[1]/div[1]/div[1]/form[1]/div[5]/button[1]")
    WebElement registerButton;

    public HomePage signUp(String nickName, String email, String password, String repeatPassword) {
        type(nickNameField, nickName);
        type(emailField, email);
        type(passwordField, password);
        type(repeatPasswordField, repeatPassword);
        click(registerButton);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = null;
        return new HomePage(driver, js);
    }

    public SignUpPage signUpNegative(String nickName, String email, String password, String repeatPassword) {
        type(nickNameField, nickName);
        type(emailField, email);
        type(passwordField, password);
        type(repeatPasswordField, repeatPassword);
        click(registerButton);
        return this;
    }

    @FindBy(xpath = "//div[contains(text(),'User with this email')]")
    WebElement errorMessageUserExists;

    public SignUpPage verifyUserExistsMessageIsPresent() {
        new SignUpPage(driver, js).isTextPresent(errorMessageUserExists,
                "User with this email");
        return this;
    }

}
