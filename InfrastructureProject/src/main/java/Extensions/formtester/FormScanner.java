package Extensions.formtester;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * מחלקה האחראית לסריקה של טופס ושליפת כל שדות הקלט בו.
 */
public class FormScanner {

    /** דרייבר של Selenium לביצוע הפעולות בדפדפן */
    private final WebDriver driver;

    /** לאוקייטור של הטופס שמכיל את כל השדות */
    final By formLocator;

    /**
     * בונה מופע חדש של {@code FormScanner}.
     *
     * @param driver       הדרייבר לביצוע פעולות ב-Selenium.
     * @param formLocator  הלוקייטור של הטופס שמכיל את השדות.
     */
    public FormScanner(WebDriver driver, By formLocator) {
        this.driver = driver;
        this.formLocator = formLocator;
    }

    /**
     * מחזיר את כל השדות שנמצאים בטופס - כולל input, select, textarea ו-buttons.
     *
     * @return רשימה של כל אלמנטי השדות בטופס.
     */
    public List<WebElement> getAllFields() {
        WebElement form = driver.findElement(formLocator);
        return form.findElements(By.cssSelector("input, select, textarea, button"));
    }
}
