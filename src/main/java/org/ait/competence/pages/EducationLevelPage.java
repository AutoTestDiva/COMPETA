package org.ait.competence.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class EducationLevelPage extends BasePage {
    private final JavascriptExecutor js;
    public EducationLevelPage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    @FindBy(xpath = "//input[@id='name1-input']")
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
        new AdministrationPage(driver, js).isTextPresent(educationLevelExistsMessage,
                "Education level with name");
        return this;
    }

    @FindBy(xpath = "//p[contains(text(),'Bachelor`s Degree')]")
    WebElement updatedEducationLevelField;
    public EducationLevelPage verifyUpdatedEducationLevelIsPresent(String updatedEducationLevelName) {
        isTextPresent(updatedEducationLevelField, updatedEducationLevelName);
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
