package Extensions.Responsive;

import org.openqa.selenium.WebDriver;

/**
 * ממשק אסטרטגיה לשינוי גודל חלון הדפדפן.
 * <p>
 * מבוסס על עיקרון ה-Strategy Design Pattern,
 * המאפשר להגדיר התנהגות (שינוי תצוגה) בצורה גמישה ודינמית מבלי לשנות את קוד השימוש.
 * כל מחלקה שמממשת את הממשק הזה מייצגת אסטרטגיה שונה (דסקטופ, טאבלט, מובייל).
 */
public interface ResizeStrategy {

    /**
     * משנה את גודל חלון הדפדפן בהתאם לאסטרטגיה הספציפית.
     *
     * @param driver מופע של WebDriver שעליו תתבצע הפעולה.
     */
    void resize(WebDriver driver);
}

