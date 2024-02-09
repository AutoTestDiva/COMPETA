package org.ait.competence.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ForeignLanguageLevelPage extends BasePage {
    private final JavascriptExecutor js;
    public ForeignLanguageLevelPage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    @FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input')]")
    WebElement foreignLanguageLevelField;
    @FindBy(xpath = "//thead/tr[1]/th[1]/div[1]/div[2]/button[1]/*[1]")
    WebElement addForeignLanguageLevelButton;
    public ForeignLanguageLevelPage addForeignLanguageLevel(String foreignLanguageLevelName) {
        type(foreignLanguageLevelField, foreignLanguageLevelName);
        click(addForeignLanguageLevelButton);
        return this;
    }

    @FindBy(xpath = "//p[contains(text(),'C3')]")
    WebElement addedForeignLanguageLevel;
    public ForeignLanguageLevelPage verifyNewForeignLanguageLevelIsPresent(String foreignLanguageLevelName) {
        new ForeignLanguagePage(driver, js).isTextPresent(addedForeignLanguageLevel, foreignLanguageLevelName);
        return this;
    }

    @FindBy(xpath = "//div[contains(text(), 'Foreign language level with name')]")
    WebElement foreignLanguageLevelExistsMessage;
    public ForeignLanguageLevelPage verifyForeignLanguageLevelExistsMessageIsPresent(String foreignLanguageLevelWithName) {
        new ForeignLanguagePage(driver, js).isTextPresent(foreignLanguageLevelExistsMessage,
                "Foreign language level with name");
        return this;
    }

    @FindBy(xpath = "//button[@id='mui-p-11787-T-4']")
    WebElement updateForeignLanguageLevelButton;
    @FindBy(xpath = "//p[contains(text(),'C3')]")
    WebElement foreignLanguageLevelToUpdate;
    @FindBy(xpath = "//button[contains(text(), 'Save')]")
    WebElement saveUpdatedForeignLanguageLevelButton;
    public ForeignLanguageLevelPage updateForeignLanguageLevel(String updatedForeignLanguageLevelName) {
        scrollToElement(foreignLanguageLevelToUpdate);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Явное ожидание с таймаутом в 10 секунд
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[contains(text(),'C3')]")));
        click(updateForeignLanguageLevelButton);
        click(foreignLanguageLevelToUpdate);
        foreignLanguageLevelToUpdate.sendKeys(Keys.CONTROL + "a"); // Выделяем текущее значение в поле ввода
        foreignLanguageLevelToUpdate.sendKeys(Keys.DELETE); // Удаляем текущее значение из поля ввода
        type(foreignLanguageLevelField, updatedForeignLanguageLevelName); // Вводим новое значение
        click(saveUpdatedForeignLanguageLevelButton);
        return new ForeignLanguageLevelPage(driver, js);
    }

    @FindBy(xpath = "//p[contains(text(),'C4')]")
    WebElement updatedForeignLanguageField;
    public ForeignLanguageLevelPage verifyUpdatedForeignLanguageLevelIsPresent(String updatedForeignLanguageLevelName) {
        isTextPresent(updatedForeignLanguageField, updatedForeignLanguageLevelName);
        return new ForeignLanguageLevelPage(driver, js);
    }

    @FindBy(xpath = "//tbody/tr[7]/td[3]/button[1]")
    WebElement deleteForeignLanguageLevelButton;
    public ForeignLanguageLevelPage deleteForeignLanguageLevel() {
        click(deleteForeignLanguageLevelButton);
        return new ForeignLanguageLevelPage(driver, js);
    }

    public boolean verifyDeletedForeignLanguageLevelIsNotPresent(String text) {
        try {
            WebElement element = driver.findElement(By.xpath("//p[text()='" + text + "']"));
            return false; // элемент найден
        } catch (NoSuchElementException e) {
            return true; // элемент не найден
        }
    }
}
