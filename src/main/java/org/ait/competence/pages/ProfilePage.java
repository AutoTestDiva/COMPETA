package org.ait.competence.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class ProfilePage extends BasePage {
    private final JavascriptExecutor js;
    public ProfilePage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

}
