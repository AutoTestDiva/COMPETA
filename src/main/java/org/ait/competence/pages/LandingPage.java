package org.ait.competence.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class LandingPage extends BasePage {

    private final JavascriptExecutor js;

    public LandingPage(WebDriver driver, JavascriptExecutor js) {
        super(driver);
        this.js = js;
    }

    @FindBy(xpath = "//header/div[1]/button[1]")
    WebElement logInButton;

    public LogInPage selectLogIn() {
        click(logInButton);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new LogInPage(driver);
    }

}
