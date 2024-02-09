package org.ait.competence.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IndustryPage extends BasePage {

    private final JavascriptExecutor js;

    public IndustryPage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    //@FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input') and not(@id='name1-input')]")
    @FindBy(xpath = "//input[@id='name1-input']")
    WebElement IndustryField;
    @FindBy(xpath = "//thead/tr[1]/th[1]/div[1]/div[2]/button[1]/*[1]")
    WebElement addIndustryButton;
    public IndustryPage addIndustry(String industryName) {
        type(IndustryField, industryName);
        click(addIndustryButton);
        return new IndustryPage(driver, js);
    }

    @FindBy(xpath = "//p[contains(text(),'Tourism')]")
    WebElement addedIndustry;
    public IndustryPage verifyNewIndustryIsPresent(String industryName) {
        new ForeignLanguagePage(driver, js).isTextPresent(addedIndustry, industryName);
        return this;
    }

    @FindBy(xpath = "//div[contains(text(), 'Industry with name')]")
    WebElement industryExistsMessage;
    public IndustryPage verifyIndustryExistsMessageIsPresent(String industryWithName) {
        new AdministrationPage(driver, js).isTextPresent(industryExistsMessage,
                "Industry with name");
        return this;
    }

    @FindBy(xpath = "//tbody/tr[1]/td[2]/button[1]")
    WebElement updateIndustryButton;
    @FindBy(xpath = "//p[contains(text(),'Tourism')]")
    WebElement industryToUpdate;
    @FindBy(xpath = "//button[contains(text(), 'Save')]")
    WebElement saveUpdatedIndustryButton;
    @FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input') and not(@id='name1-input')]")
    WebElement industryFieldToUpdate;
    public IndustryPage updateIndustry(String updatedIndustryName) {
        scrollToElement(industryToUpdate);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Явное ожидание с таймаутом в 10 секунд
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'Tourism')]")));
        click(updateIndustryButton);
        click(industryToUpdate);
        industryToUpdate.sendKeys(Keys.CONTROL + "a"); // Выделяем текущее значение в поле ввода
        industryToUpdate.sendKeys(Keys.DELETE); // Удаляем текущее значение из поля ввода
        type(industryFieldToUpdate, updatedIndustryName); // Вводим новое значение
        click(saveUpdatedIndustryButton);
        return new IndustryPage(driver, js);
    }

    @FindBy(xpath = "//p[contains(text(),'Transport')]")
    WebElement updatedIndustryField;
    public IndustryPage verifyUpdatedIndustryIsPresent(String updatedIndustryName) {
        isTextPresent(updatedIndustryField, updatedIndustryName);
        return new IndustryPage(driver, js);
    }

    @FindBy(xpath = "//tbody/tr[1]/td[3]/button[1]")
    WebElement deleteIndustryButton;
    public IndustryPage deleteIndustry() {
        click(deleteIndustryButton);
        return new IndustryPage(driver, js);
    }

    public boolean verifyDeletedIndustryIsNotPresent(String text) {
        try {
            WebElement element = driver.findElement(By.xpath("//p[text()='" + text + "']"));
            return false; // элемент найден
        } catch (NoSuchElementException e) {
            return true; // элемент не найден
        }
    }
}
