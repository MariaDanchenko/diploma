package listener;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.apache.commons.io.FileUtils;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;

public class Listener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(Listener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        if (!(testClass instanceof BaseTest)) {
            logger.warn("Skipping screenshot capture: {} is not BaseTest", result.getTestClass().getName());
            return;
        }

        WebDriver driver = ((BaseTest) testClass).getDriver();
        if (driver == null || !(driver instanceof TakesScreenshot)) {
            logger.warn("Skipping screenshot capture: driver unavailable for {}", result.getName());
            return;
        }

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            String screenshotPath = "screenshots/" + result.getName() + ".png";
            FileUtils.copyFile(srcFile, new File(screenshotPath));
            logger.error("Test failed: {}. Screenshot saved to {}", result.getName(), screenshotPath, result.getThrowable());
        } catch (IOException e) {
            logger.error("Failed to save screenshot for {}", result.getName(), e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {}", result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {}", result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("Testing started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Testing finished: {}", context.getName());
    }
}
