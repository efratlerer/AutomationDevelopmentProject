package Extensions;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.util.Optional;

public class MyTestWatcher implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println(" טסט עבר: " + context.getDisplayName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println(" טסט נכשל: " + context.getDisplayName());

        context.getTestInstance().ifPresent(instance -> {
            try {
                Method getDriverMethod = instance.getClass().getMethod("getDriver");
                WebDriver driver = (WebDriver) getDriverMethod.invoke(instance);
                ScreenshotUtils.takeScreenshot(driver, "error_" + context.getDisplayName());
            } catch (Exception e) {
                System.out.println(" לא הצליח לצלם מסך: " + e.getMessage());
            }
        });
    }


    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        System.out.println("⚠ טסט מושבת: " + context.getDisplayName() + " - " + reason.orElse("לא צוינה סיבה"));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println("⏹ טסט בוטל: " + context.getDisplayName());
    }
}

