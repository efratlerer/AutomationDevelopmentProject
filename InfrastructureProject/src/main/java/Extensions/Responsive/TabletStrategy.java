package Extensions.Responsive;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * מימוש של ResizeStrategy עבור תצוגת Tablet.
 * <p>
 * חלק ממימוש Strategy Design Pattern – קובע את גודל המסך כ־768x1024.
 */
public class TabletStrategy implements ResizeStrategy {

    @Override
    public void resize(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(768, 1024));
    }
}
