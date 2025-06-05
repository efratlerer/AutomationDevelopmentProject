package Extensions.Responsive;

import org.openqa.selenium.WebDriver;

/**
 * מחלקת ניהול תצוגה המבוססת על Strategy Design Pattern.
 * <p>
 * מאפשרת להפעיל אסטרטגיה גמישה לשינוי רספונסיביות של חלון הדפדפן.
 * בונה את ההתנהגות לפי האובייקט של ResizeStrategy שנשלח לבנאי.
 */
public class ResponsiveManager {
    private final ResizeStrategy strategy;

    /**
     * בונה מופע של ResponsiveManager עם האסטרטגיה הנתונה.
     *
     * @param strategy אסטרטגיית שינוי תצוגה (Desktop, Tablet, Mobile וכו').
     */
    public ResponsiveManager(ResizeStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * מפעיל את האסטרטגיה שנבחרה לשינוי גודל הדפדפן.
     *
     * @param driver מופע של WebDriver שעליו תתבצע הפעולה.
     */
    public void apply(WebDriver driver) {
        strategy.resize(driver);
    }
}

