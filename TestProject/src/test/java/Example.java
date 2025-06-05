import Extensions.Drivermanager;
import Extensions.GetData;
import Extensions.Responsive.*;
import Extensions.MyTestWatcher;
import Extensions.formtester.FormTesterEngine;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import Extensions.Drivermanager;
import Extensions.GetData;
import Extensions.Responsive.MobileStrategy;
import Extensions.MyTestWatcher;
import Extensions.Responsive.ResponsiveManager;
import Extensions.Responsive.TabletStrategy;
import Extensions.formtester.FormTesterEngine;
import com.epam.reportportal.junit5.ReportPortalExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MyTestWatcher.class)
@ExtendWith(ReportPortalExtension.class)
public class Example {

    private static WebDriver driver;

    Drivermanager.BrowserType browser = Drivermanager.BrowserType.CHROME;

    @BeforeAll
    public void setUp() {
        driver = Drivermanager.getDriver(browser);
        ResponsiveManager responsive = new ResponsiveManager(new DesktopStrategy());
        responsive.apply(driver);
        driver.get(GetData.getData("url"));
    }

    @AfterAll
    public void tearDown() {
        Drivermanager.quitDriver();
    }

//
    @Test
    @Order(1)
    void testWithScreenshot() {
        driver.findElement(By.id("calculate_data")); // כאן אמור ליפול → ואז תהיה צילום מסך
    }

    @Test
    @Order(2)
    void testForm() {
        FormTesterEngine engine = new FormTesterEngine(driver, By.className(GetData.getData("formId")));
        engine.run();
    }
}
