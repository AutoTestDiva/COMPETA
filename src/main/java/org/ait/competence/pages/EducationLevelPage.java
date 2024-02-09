package org.ait.competence.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class EducationLevelPage extends BasePage {
    private final JavascriptExecutor js;
    public EducationLevelPage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    //@FindBy(xpath = "//input[@id='name1-input']")
    @FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input')]")
    WebElement educationLevelField;
    @FindBy(xpath = "//thead/tr[1]/th[1]/div[1]/div[2]/button[1]/*[1]")
    WebElement addEducationLevelButton;
    public EducationLevelPage addEducationLevel(String educationLevelName) {
        type(educationLevelField, educationLevelName);
        click(addEducationLevelButton);
        return this;
    }

    @FindBy(xpath = "//p[contains(text(),'Primary school')]")
    WebElement addedEducationLevel;
    public EducationLevelPage verifyNewEducationLevelIsPresent(String educationLevelName) {
        new EducationLevelPage(driver, js).isTextPresent(addedEducationLevel, educationLevelName);
        return this;
    }

    @FindBy(xpath = "//div[contains(text(), 'Education level with name')]")
    WebElement educationLevelExistsMessage;
    public EducationLevelPage verifyEducationLevelExistsMessageIsPresent(String educationLevelWithName) {
        new EducationLevelPage(driver, js).isTextPresent(educationLevelExistsMessage,
                "Education level with name");
        return this;
    }

    @FindBy(xpath = "//tbody/tr[1]/td[2]/button[1]")
    WebElement updateEducationLevelButton;
    @FindBy(xpath = "//p[contains(text(),'Primary School')]")
    WebElement educationLevelToUpdate;
    @FindBy(xpath = "//button[contains(text(), 'Save')]")
    WebElement saveUpdatedEducationLevelButton;
//    @FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input')]")
//    WebElement educationLevelField;
    public EducationLevelPage updateEducationLevel(String updatedEducationLevelName) {
        click(updateEducationLevelButton);
        click(educationLevelToUpdate);
        educationLevelToUpdate.sendKeys(Keys.CONTROL + "a"); // Выделяем текущее значение в поле ввода
        educationLevelToUpdate.sendKeys(Keys.DELETE); // Удаляем текущее значение из поля ввода
        type(educationLevelField, updatedEducationLevelName); // Вводим новое значение
        click(saveUpdatedEducationLevelButton);
        return new EducationLevelPage(driver, js);
    }

    @FindBy(xpath = "//p[contains(text(),'Bachelor`s Degree')]")
    WebElement updatedEducationLevelField;
    public EducationLevelPage verifyUpdatedEducationLevelIsPresent(String updatedEducationLevelName) {
        isTextPresent(updatedEducationLevelField, updatedEducationLevelName);
        return new EducationLevelPage(driver, js);
    }

    @FindBy(xpath = "//tbody/tr[1]/td[3]/button[1]")
    WebElement deleteEducationLevelButton;
    public EducationLevelPage deleteEducationLevel() {
        click(deleteEducationLevelButton);
        return new EducationLevelPage(driver, js);
    }

    public boolean verifyDeletedEducationLevelIsNotPresent(String text) {
        try {
            WebElement element = driver.findElement(By.xpath("//p[text()='" + text + "']"));
            return false; // элемент найден
        } catch (NoSuchElementException e) {
            return true; // элемент не найден
        }
    }
}
