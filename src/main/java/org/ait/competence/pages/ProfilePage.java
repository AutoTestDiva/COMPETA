package org.ait.competence.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class ProfilePage extends BasePage {
    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//p[text()='Registered since:']")
    WebElement registrationDateText;

    public ProfilePage verifyRegisteredSinceText(String text) {
        Assert.assertTrue(shouldHaveText(registrationDateText, text, 10));
        return this;
    }

}
