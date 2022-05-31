package com.utils;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

/**
 * Class for managing browser actions
 */
public class BrowserManager {
    private final Browser browser;

    public BrowserManager() {
        browser = AqualityServices.getBrowser();
    }

    public void maximizeWindow() {
        browser.maximize();
    }

    public String getBrowserNameAndVersion() {
        Capabilities cp = browser.getDriver().getCapabilities();
        return String.format("%1$s %2$s", cp.getBrowserName(), cp.getVersion());
    }

    public void navigateTo(String url) {
        browser.goTo(url);
    }

    public void waitForPageToLoad() {
        browser.waitForPageToLoad();
    }

    public void navigateBack() {
        browser.goBack();
    }

    public void executeScript(String script) {
        browser.executeScript(script);
    }

    public void refreshPage() {
        browser.refresh();
    }

    public String getScreenshotAsBase64() {
        return ((TakesScreenshot) browser.getDriver()).getScreenshotAs(OutputType.BASE64);
    }

    public void quitBrowser() {
        browser.quit();
    }
}
