package Extensions.Responsive;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * מימוש של ResizeStrategy עבור תצוגת Mobile.
 * <p>
 * חלק ממימוש Strategy Design Pattern – קובע את גודל המסך כ־375x667.
 */
public class MobileStrategy implements ResizeStrategy {

    @Override
    public void resize(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(375, 667));
    }
}
