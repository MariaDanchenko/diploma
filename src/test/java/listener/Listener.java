package listener;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.BaseTest;
import tests.BaseIsolatedTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class Listener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        WebDriver driver;
        if (testClass instanceof BaseTest bt) {
            driver = bt.getDriver();
        } else if (testClass instanceof BaseIsolatedTest bit) {
            driver = bit.getDriver();
        } else {
            log.warn("Skipping screenshot capture: {} is not supported base class", result.getTestClass().getName());
            return;
        }

        if (driver == null) {
            log.warn("Skipping screenshot capture: driver unavailable for {}", result.getName());
            return;
        }

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try (FileInputStream screenshot = new FileInputStream(srcFile)) {
            Allure.addAttachment(result.getName(), "image/png", screenshot, ".png");
            log.error("Test failed: {}.", result.getName(), result.getThrowable());
        } catch (IOException e) {
            log.error("Failed to attach screenshot for {}", result.getName(), e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        log.info("Testing started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Testing finished: {}", context.getName());
    }
}
