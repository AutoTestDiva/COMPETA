package org.ait.competence.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

public class AdministrationPage extends BasePage {
    private final JavascriptExecutor js;
    public AdministrationPage(WebDriver driver, JavascriptExecutor js) {
        super(driver, js);
        this.js = js;
    }

    @FindBy(xpath = "//input[contains(@class, 'MuiInputBase-input') and not(@id='name1-input')]")
    WebElement driverLicenseField;

    @FindBy(xpath = "//thead/tr[1]/th[1]/div[1]/div[2]/button[1]/*[1]")
    WebElement addDriverLicenseButton;
    public AdministrationPage addDriverLicense(String driverLicenseName) {
        type(driverLicenseField, driverLicenseName);
        click(addDriverLicenseButton);
        return new AdministrationPage(driver, js);
    }

    @FindBy(xpath = "//div[contains(text(), 'Driver licence with name')]")
    WebElement driverLicenseExistsMessage;
    public AdministrationPage verifyDriverLicenseExistsMessageIsPresent(String driverLicenceWithName) {
        new AdministrationPage(driver, js).isTextPresent(driverLicenseExistsMessage,
                "Driver licence with name");
        return this;
    }

    @FindBy(xpath = "//tbody/tr[4]/td[2]/button[1]")
    WebElement updateDriverLicenseButton;
    @FindBy(xpath = "//input[contains(@value, 'D')]")
    WebElement driverLicenseToUpdate;
    @FindBy(xpath = "//button[contains(text(), 'Save')]")
    WebElement saveUpdatedDriverLicenseButton;
    public AdministrationPage updateDriverLicense(String newDriverLicenseName) {
        click(updateDriverLicenseButton);
        click(driverLicenseToUpdate);
        driverLicenseToUpdate.sendKeys(Keys.CONTROL + "a"); // Выделяем текущее значение в поле ввода
        driverLicenseToUpdate.sendKeys(Keys.DELETE); // Удаляем текущее значение из поля ввода
        type(driverLicenseField, newDriverLicenseName); // Вводим новое значение
        click(saveUpdatedDriverLicenseButton);
        return this;
    }

    @FindBy(xpath = "//p[text()='M']")
    WebElement updatedDriverLicenseField;
    public AdministrationPage verifyNewDriverLicenseIsPresent(String newDriverLicenseName) {
        isTextPresent(updatedDriverLicenseField, newDriverLicenseName);
        return this;
    }

    @FindBy(xpath = "//tbody/tr[4]/td[3]/button[1]")
    WebElement deleteDriverLicenseButton;
    public AdministrationPage deleteDriverLicense() {
        click(deleteDriverLicenseButton);
        return new AdministrationPage(driver, js);
    }

    public boolean verifyNewDriverLicenseIsNotPresent(String text) {
        try {
            WebElement element = driver.findElement(By.xpath("//p[text()='" + text + "']"));
            return false; // элемент найден
        } catch (NoSuchElementException e) {
            return true; // элемент не найден
        }
    }

}
