package Extensions.formtester;


import Extensions.Drivermanager;

import Extensions.ScreenshotUtils;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.Assertion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * מחלקה זו מהווה תשתית לבדיקת שדות טופס בצורה גנרית.
 * היא מזהה את סוג השדה (מייל, מספר, לינק, כפתור וכו') ומריצה עליו בדיקה מתאימה.
 * המטרה היא לבדוק טפסים בצורה חכמה מבלי לקודד את הלוגיקה של כל שדה בנפרד.
 */
public class FieldTester {
    private static final Logger logger = LoggerFactory.getLogger(FieldTester.class);
    /**
     * Enum שמתאר את סוגי השדות האפשריים בטופס.
     */
    enum FieldType {
        CHECKBOX,EMAIL, NUMBER, LINK, TEXT, SELECT, BUTTON, RESET, SUBMIT, UNKNOWN,
    }

    /**
     * מזהה את סוג השדה לפי tagName, type, name ועוד.
     * כולל מנגנון חכם שמונע טעויות זיהוי (למשל: name שמכיל את המילה "link" אבל הוא בעצם username).
     */
    private static FieldType detectFieldType(WebElement field) {
        String tag = field.getTagName().toLowerCase();
        String type = field.getAttribute("type") != null ? field.getAttribute("type").toLowerCase() : "";
        String name = getFieldName(field).toLowerCase();

        if (!field.isEnabled()) return FieldType.UNKNOWN;

        if ("select".equals(tag)) return FieldType.SELECT;
        if ("button".equals(tag)) return FieldType.BUTTON;
        if ("submit".equals(type)) return FieldType.SUBMIT;
        if ("reset".equals(type)) return FieldType.RESET;

        if ("input".equals(tag) || "textarea".equals(tag)) {
            if ("email".equals(type) || name.contains("email")) return FieldType.EMAIL;
            if ("number".equals(type) || name.contains("phone") || name.contains("number")) return FieldType.NUMBER;
            if ("checkbox".equalsIgnoreCase(type)) { return FieldType.CHECKBOX; }
            if (name.contains("link") || name.contains("url") || name.contains("site")) {
                if (name.contains("user") || name.contains("name")) {
                    return FieldType.TEXT;
                } else {
                    return FieldType.LINK;
                }
            }
            return FieldType.TEXT;
        }

        return FieldType.UNKNOWN;
    }

    /**
     * נקודת הכניסה: פונקציה זו מקבלת שדה ומריצה עליו בדיקה מתאימה לפי הסוג שזוהה.
     */
    public static void testField(WebElement field, WebDriver driver) {
        FieldType type = detectFieldType(field);

        switch (type) {
            case EMAIL:
                testEmail(field);
                break;
            case NUMBER:
                testNumber(field);
                break;
            case LINK:
                testLink(field);
                break;
            case TEXT:
                testText(field);
                break;
            case SELECT:
                testSelect(field);
                break;
            case CHECKBOX:
                testCheckbox(field);
                break;
            case SUBMIT:
            case RESET:
            case BUTTON:
                testButtonSmart(field, driver);
                break;
            default:
                logger.error(() ->"לא זוהה סוג שדה עבור:" + getFieldName(field));

        }
    }

    /**
     * בדיקת שדה טקסט כללי – שולח טקסט לדוגמה לשדה.
     */
    private static void testText(WebElement field) {
        field.clear();
        field.sendKeys("בדיקה כללית");
    }

    /**
     * בדיקת שדה לינק – שולח טקסט, מזהה אם הוא תקין, ומחפש הודעות שגיאה מסביב.
     */
    private static void testLink(WebElement field) {
        field.clear();
        String testValue = "link";
        field.sendKeys(testValue);
        field.sendKeys(Keys.TAB);

        String enteredValue = field.getAttribute("value");
        if (!enteredValue.contains("http")) {
            logger.error(() ->" לא מכיל לינק: " + enteredValue);
        } else {
            logger.info(() ->"✅ לשדה הוכנס לינק תקין.");
        }

        try {
            WebElement parent = field.findElement(By.xpath(".."));
            List<WebElement> errorMessages = parent.findElements(By.xpath(".//*[contains(text(),'http') or contains(text(),'תקין') or contains(text(),'חובה')]"));
            if (!errorMessages.isEmpty()) {
                logger.error(() ->" הודעת שגיאה מזוהה בשדה הלינק: " + errorMessages.get(0).getText());
            }
        } catch (Exception ignored) {}
    }
    /**
     * מבצע בדיקות בסיסיות על אלמנט מסוג Checkbox:
     * <ul>
     *   <li>בודק אם האלמנט גלוי וזמין ללחיצה.</li>
     *   <li>שומר את מצב הסימון לפני לחיצה.</li>
     *   <li>לוחץ על האלמנט (משנה את מצב הסימון).</li>
     *   <li>בודק את מצב הסימון לאחר הלחיצה.</li>
     *   <li>מדפיס את המצב לפני ואחרי הלחיצה.</li>
     * </ul>
     *
     * @param checkbox אלמנט מסוג checkbox שיש לבדוק
     */
    public static void testCheckbox(WebElement checkbox) {
        if (checkbox.isEnabled() ) {
            boolean before = checkbox.isSelected();
            checkbox.click();
            boolean after = checkbox.isSelected();

            System.out.println("checkbox היה מסומן? " + before);
            System.out.println("checkbox עכשיו מסומן? " + after);
//            try {
//                Thread.sleep(9000); // 1000 מילישניות = 1 שנייה
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        } else {
          logger.warn(() ->"ה־checkbox לא ניתן ללחיצה");
        }
    }

    /**
     * בדיקת שדה מייל – שולח מייל שגוי ומוודא שהשדה מזהה זאת.
     */
    private static void testEmail(WebElement field) {
        field.clear();
        String testValue = "not-an-email";
        field.sendKeys(testValue);
        field.sendKeys(Keys.TAB);

        String enteredValue = field.getAttribute("value");
        if (!enteredValue.contains("@") || !enteredValue.contains(".")) {
            logger.error(() ->" שדה מייל לא מכיל כתובת מייל תקינה. ערך שהוזן: " + enteredValue);
        } else {
            logger.info(() ->" שדה מייל קיבל כתובת תקינה.");
        }

        try {
            WebElement parent = field.findElement(By.xpath(".."));
            List<WebElement> errorMessages = parent.findElements(By.xpath(".//*[contains(text(),'email') or contains(text(),'חובה') or contains(text(),'תקין')]"));
            if (!errorMessages.isEmpty()) {
                logger.error(() ->"הודעת שגיאה מזוהה בשדה מייל: " + errorMessages.get(0).getText());
            }
        } catch (Exception ignored) {}
    }

    /**
     * בדיקת שדה מספר – מכניס טקסט לא תקני ומוודא שהשדה לא קיבל אותו.
     */
    private static void testNumber(WebElement field) {
        field.clear();
        String testValue = "abc";
        field.sendKeys(testValue);
        field.sendKeys(Keys.TAB);

        String enteredValue = field.getAttribute("value");
        if (!Pattern.matches("\\d+", enteredValue)) {
            logger.error(() ->" שדה מספר לא קיבל ערך מספרי תקין. ערך שהוזן: " + enteredValue);
        } else {
            logger.info(() ->" שדה מספר קיבל ערך תקין.");
        }

        try {
            WebElement parent = field.findElement(By.xpath(".."));
            List<WebElement> errorMessages = parent.findElements(By.xpath(".//*[contains(text(),'מספר') or contains(text(),'חובה') or contains(text(),'לא תקין')]"));
            if (!errorMessages.isEmpty()) {
                logger.error(() ->"הודעת שגיאה מזוהה בשדה מספר: " + errorMessages.get(0).getText());
            }
        } catch (Exception ignored) {}
    }

    /**
     * בוחר ערך מתוך שדה Select אם יש יותר מאפשרות אחת.
     */
    private static void testSelect(WebElement field) {
        var options = field.findElements(By.tagName("option"));
        if (options.size() > 1) {
            options.get(1).click();
        }
    }

    /**
     * בדיקת כפתור חכמה – יודעת לזהות כפתור Submit, Reset או כל כפתור רגיל ולבדוק את ההשפעה שלו על הדף.
     *נבדק גם לפי שינוי ב-URL וגם לפי שינוי חזותי בעמוד (צילומי מסך)
     */
    private static void testButtonSmart(WebElement button, WebDriver driver) {
        System.out.println("enter");
        String type = button.getAttribute("type");
        String name = getFieldName(button);

        try {

            switch (type != null ? type.toLowerCase() : "") {
                case "submit":
                    // בדיקת Submit – צילום מסך והשוואה + בדיקת URL
                    String currentUrl = driver.getCurrentUrl();
                    File beforeFile = ScreenshotUtils.takeScreenshot(driver, "before_submit");
                    button.click();
                    Thread.sleep(1000);
                    File afterFile = ScreenshotUtils.takeScreenshot(driver, "after_submit");

                    boolean screenshotChanged = false;
                    try {
                        byte[] beforeBytes = Files.readAllBytes(beforeFile.toPath());
                        byte[] afterBytes = Files.readAllBytes(afterFile.toPath());
                        screenshotChanged = !Arrays.equals(beforeBytes, afterBytes);
                    } catch (IOException e) {
                        logger.error(() ->"שגיאה בהשוואת צילומי מסך: " + e.getMessage());
                    }

                    boolean urlChanged = false;
                    try {
                        urlChanged = new WebDriverWait(driver, 2)
                                .until(ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl)));
                    } catch (TimeoutException ignored) {}

                    if (urlChanged) {
                        logger.info(() ->" Submit נלחץ והתרחש ניתוב לעמוד אחר.");
                    } else if (screenshotChanged) {
                        logger.info(() ->" Submit גרם לשינוי ויזואלי בדף.");
                    } else {
                        logger.info(() ->" Submit נלחץ אך לא זוהה שינוי בדף.");
                    }
                    break;

                case "reset":
                    // בדיקת Reset – מזין טקסט ובודק אם נמחק
                    WebElement anyInput = driver.findElement(By.cssSelector("input[type='text']"));
                    anyInput.sendKeys("בדיקה");
                    button.click();
                    if (anyInput.getAttribute("value").isEmpty()) {
                        logger.info(() ->"כפתור Reset איפס את השדה.");
                    } else {
                        logger.error(() ->" כפתור Reset לא איפס.");
                    }
                    break;

                case "button":
                default:
                    // בדיקת כפתור רגיל – השוואת טקסט body לפני ואחרי
                    String before = driver.findElement(By.tagName("body")).getText();
                    button.click();
                    Thread.sleep(500);
                    String after = driver.findElement(By.tagName("body")).getText();
                    if (!before.equals(after)) {
                        logger.info(() ->" Button גרם לשינוי בדף: " + name);
                    } else {
                        logger.warn(() ->" Button נלחץ אך לא זוהה שינוי: " + name);
                    }
            }
        } catch (Exception e) {
            logger.error(() ->"️ שגיאה בבדיקת כפתור: " + name + " – " + e.getMessage());
        }
    }

    /**
     * מחלץ את שם השדה לפי name, id או טקסט פנימי, למטרות דיווח ובדיקה.
     */
    private static String getFieldName(WebElement field) {
        String name = field.getAttribute("name");
        if (name == null || name.isEmpty()) name = field.getAttribute("id");
        if ((name == null || name.isEmpty()) && field.getText() != null)
            name = field.getText().trim();
        return name != null ? name : "לא ידוע";
    }
}
