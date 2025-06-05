package Extensions.Responsive;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

/**
 * מימוש של ResizeStrategy עבור תצוגת Desktop.
 * <p>
 * חלק ממימוש Strategy Design Pattern – קובע את גודל המסך כ־1920x1080.
 */
public class DesktopStrategy implements ResizeStrategy {

    @Override
    public void resize(WebDriver driver) {
        driver.manage().window().setSize(new Dimension(1920, 1080));
    }
}
