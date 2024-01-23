package org.ait.competence.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Factory;

import java.time.Duration;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//header/div[1]/button[2]")
    WebElement signUpButton;

    public SignUpPage selectSignUp() {
        click(signUpButton);
        return new SignUpPage(driver);
    }

    @FindBy(xpath = "//header/div[1]/button[1]")
    WebElement logOutBtn;

    public HomePage verifyLogOutBtnIsPresent() {
        Assert.assertTrue(isElementPresent(logOutBtn, 10));
        return this;
    }

    @FindBy(xpath = "//div[contains(text(),'Incorrect username or password')]")
    WebElement errorMessage;

    public HomePage verifyErrorMessageIsPresent() {
        Assert.assertTrue(isElementPresent(errorMessage, 10));
        return this;
    }

    @FindBy(xpath = "//h4[contains(text(),'Log in to continue')]")
    WebElement loginToContinue;

    public SignUpPage verifyLogInTextIsPresent(String text) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(loginToContinue));

            String actualText = loginToContinue.getText();
            Assert.assertTrue(actualText.contains(text),
                    "Expected text '" + text + "' not found in the element. Actual text: " + actualText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SignUpPage(driver);
    }

}
