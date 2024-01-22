package org.ait.competence.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LandingPage extends BasePage {

    public LandingPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//header/div[1]/button[1]")
    WebElement logInButton;

    public LogInPage selectLogIn() {
        click(logInButton);

        try {
            Thread.sleep(5000); // Подождать 5 секунд
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new LogInPage(driver);
    }
}
