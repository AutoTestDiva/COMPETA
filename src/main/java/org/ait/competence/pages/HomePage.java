package org.ait.competence.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.testng.annotations.Factory;

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
}
