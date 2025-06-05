package Extensions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * מחלקה לניהול דרייבר דפדפן בסגנון Singleton.
 * יוצרת ומחזירה מופע יחיד של WebDriver בהתאם לסוג הדפדפן שנבחר.
 */
public class Drivermanager {

    /**
     * סוגי הדפדפנים הנתמכים.
     */
    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE
    }

    // מופע יחיד של הדרייבר
    private static WebDriver driver;

    /**
     * מחזירה מופע יחיד של WebDriver לפי סוג הדפדפן שנבחר.
     * אם הדרייבר כבר קיים, מחזירה את אותו מופע.
     *
     * @param browser סוג הדפדפן (CHROME או FIREFOX)
     * @return מופע של WebDriver
     */
    public static WebDriver getDriver(BrowserType browser) {
        if (driver == null) {
            switch (browser) {
                case CHROME:
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    break;
                case FIREFOX:
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                 case EDGE:
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("דפדפן לא נתמך: " + browser);
            }
        }
        return driver;
    }

    /**
     * סוגר את הדפדפן אם קיים ומאפס את הדרייבר.
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
