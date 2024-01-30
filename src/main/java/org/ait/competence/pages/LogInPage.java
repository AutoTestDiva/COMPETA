package org.ait.competence.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LogInPage extends BasePage {
    public LogInPage(WebDriver driver) {

        super(driver);
    }

    @FindBy(xpath = "//input[@id='name-input']")
    WebElement userNameField;

    @FindBy(xpath = "//input[@id='password-input']")
    WebElement passwordField;

    @FindBy(xpath = "//body/div[@id='root']/div[1]/div[1]/form[1]/div[1]/div[3]/button[1]")
    WebElement logInButton;

    public HomePage logIn(String email, String password) {
        type(userNameField, email);
        type(passwordField, password);
        click(logInButton);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("http://localhost:5173/#/"));

        System.out.println("Current URL: " + driver.getCurrentUrl());

        JavascriptExecutor js = null;
        return new HomePage(driver, js);
    }

}
