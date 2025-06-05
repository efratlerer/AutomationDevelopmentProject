package Extensions.formtester;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * מנוע שמריץ את בדיקות השדות של טופס באופן אוטומטי.
 * משתמש במחלקת {@link FormScanner} ובמחלקת {@link FieldTester} לצורך הבדיקה.
 */
public class FormTesterEngine {

    /** דרייבר Selenium */
    private final WebDriver driver;

    /** סורק השדות מתוך הטופס */
    private final FormScanner scanner;

    /** תוצאות הבדיקות עבור כל שדה */
    private final List<FormTestResult> results = new ArrayList<>();

    /**
     * בונה מופע חדש של מנוע בדיקות טופס.
     *
     * @param driver       דרייבר Selenium.
     * @param formLocator  לוקייטור של הטופס לבדיקה.
     */
    public FormTesterEngine(WebDriver driver, By formLocator) {
        this.driver = driver;
        this.scanner = new FormScanner(driver, formLocator);
    }

    /**
     * מריץ את הבדיקות על כל שדה בטופס.
     * שומר את התוצאות, כולל טיפול בשגיאות כמו StaleElementReferenceException.
     */
    public void run() {
        List<WebElement> fields = scanner.getAllFields();
        for (WebElement field : fields) {
            String name = getFieldNameSafe(field);
            try {
                WebElement freshField = refetchField(name);
                if (freshField == null) {
                    results.add(new FormTestResult(name, "כשל", "לא נמצא שוב בדף"));
                    continue;
                }

                FieldTester.testField(freshField, driver);
                results.add(new FormTestResult(name, "הצלחה", ""));
            } catch (Exception e) {
                results.add(new FormTestResult(name, "כשל", e.getMessage()));
            }
        }

        results.forEach(System.out::println);
    }

    /**
     * מנסה לאתר מחדש שדה לפי name או id, מתוך הטופס.
     *
     * @param nameOrId שם או מזהה של השדה.
     * @return האלמנט אם נמצא, אחרת null.
     */
    private WebElement refetchField(String nameOrId) {
        try {
            if (nameOrId == null) return null;

            WebElement form = driver.findElement(scanner.formLocator);

            List<WebElement> byName = form.findElements(By.name(nameOrId));
            if (!byName.isEmpty()) return byName.get(0);

            List<WebElement> byId = form.findElements(By.id(nameOrId));
            if (!byId.isEmpty()) return byId.get(0);

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * מנסה לשלוף את השם של שדה (name או id), תוך התמודדות עם חריגת StaleElementReference.
     *
     * @param field שדה לבדיקה.
     * @return השם או המזהה של השדה, או null אם לא ניתן לשלוף.
     */
    private String getFieldNameSafe(WebElement field) {
        try {
            String name = field.getAttribute("name");
            if (name == null || name.isEmpty()) name = field.getAttribute("id");
            return (name != null && !name.isEmpty()) ? name : null;
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }
}
