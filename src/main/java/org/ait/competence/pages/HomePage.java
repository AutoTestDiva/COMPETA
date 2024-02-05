package org.ait.competence.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class HomePage extends BasePage {
    private final JavascriptExecutor js;

    public HomePage(WebDriver driver, JavascriptExecutor js) {
        super(driver);
        this.js = js;
    }

    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    public void hideAd() {
        js.executeScript("document.getElementById('adplus-anchor').style.display='none';");
    }

    @FindBy(xpath = "//header/div[1]/button[2]")
    WebElement signUpButton;

    public SignUpPage selectSignUp() {
        click(signUpButton);
        return new SignUpPage(driver, js);
    }

    @FindBy(xpath = "//button[contains(@class, 'MuiButtonBase-root MuiButton-root MuiButton-text MuiButton-textInherit MuiButton-sizeMedium MuiButton-textSizeMedium MuiButton-colorInherit MuiButton-root MuiButton-text MuiButton-textInherit MuiButton-sizeMedium MuiButton-textSizeMedium MuiButton-colorInherit css-1y942vo-MuiButtonBase-root-MuiButton-root')]")
    public WebElement logOutBtn;

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
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
            wait.until(ExpectedConditions.visibilityOf(loginToContinue));

            String actualText = loginToContinue.getText();
            Assert.assertTrue(actualText.contains(text),
                    "Expected text '" + text + "' not found in the element. Actual text: " + actualText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SignUpPage(driver, js);
    }

    public void scrollToElement(WebElement element) {
        if (js != null) {
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } else {
            System.err.println("JavascriptExecutor is not initialized!");
        }
    }

    @FindBy(xpath = "//button[@aria-label='Administrator menu']")
    public WebElement administratorMenuButton;
    public HomePage selectAdministratorMenu() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        clickWithJSExecutor(administratorMenuButton, 0, 300);
        return new HomePage(driver, js);
    }

    @FindBy(xpath = "//body[1]/div[2]/div[3]/ul[1]/li[1]")
    WebElement myProfile;
    public HomePage selectMyProfile() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        clickWithJSExecutor(myProfile, 0, 300);
        return this;
    }

    @FindBy(xpath = "//p[text()='Registered since:']")
    WebElement registrationDateText;
    public ProfilePage verifyRegisteredSinceIsPresent(String text) {
        Assert.assertTrue(shouldHaveText(registrationDateText, text, 10));
        return new ProfilePage(driver);
    }

    public HomePage selectLogOut() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(logOutBtn)).click();
        return this;
    }

}
