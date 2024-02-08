package org.ait.competence.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class ForeignLanguagePage extends BasePage {
    private final JavascriptExecutor js;
    public ForeignLanguagePage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    @FindBy(xpath = "//input[@id='name1-input']")
    WebElement foreignLanguageField;
    @FindBy(xpath = "//thead/tr[1]/th[1]/div[1]/div[2]/button[1]/*[1]")
    WebElement addForeignLanguageButton;
    public ForeignLanguagePage addForeignLanguage(String foreignLanguageName) {
        type(foreignLanguageField, foreignLanguageName);
        click(addForeignLanguageButton);
        return this;
    }

    @FindBy(xpath = "//p[contains(text(),'Japanese')]")
    WebElement addedForeignLanguage;
    public ForeignLanguagePage verifyNewForeignLanguageIsPresent(String foreignLanguageName) {
        new ForeignLanguagePage(driver, js).isTextPresent(addedForeignLanguage, foreignLanguageName);
        return this;
    }

    @FindBy(xpath = "//div[contains(text(), 'Foreign language with name')]")
    WebElement foreignLanguageExistsMessage;
    public ForeignLanguagePage verifyForeignLanguageExistsMessageIsPresent(String foreignLanguageWithName) {
        new ForeignLanguagePage(driver, js).isTextPresent(foreignLanguageExistsMessage,
                "Foreign language with name");
        return this;
    }

    @FindBy(xpath = "//p[contains(text(),'Japanese')]")
    WebElement updatedForeignLanguageField;
    public ForeignLanguagePage verifyUpdatedForeignLanguageIsPresent(String updatedForeignLanguageName) {
        isTextPresent(updatedForeignLanguageField, updatedForeignLanguageName);
        return new ForeignLanguagePage(driver, js);
    }

    @FindBy(xpath = "//tbody/tr[7]/td[3]/button[1]")
    WebElement deleteForeignLanguageButton;
    public ForeignLanguagePage deleteForeignLanguage() {
        click(deleteForeignLanguageButton);
        return new ForeignLanguagePage(driver, js);
    }

    public boolean verifyDeletedForeignLanguageIsNotPresent(String text) {
        try {
            WebElement element = driver.findElement(By.xpath("//p[text()='" + text + "']"));
            return false; // элемент найден
        } catch (NoSuchElementException e) {
            return true; // элемент не найден
        }
    }

}

