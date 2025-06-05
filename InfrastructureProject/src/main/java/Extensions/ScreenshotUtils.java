package Extensions;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * מחלקה לצילום תמונות מסך ושמירתן לתיקייה בשם "screenshots".
 */
public class ScreenshotUtils {

    /**
     * לוקחת צילום מסך של המסך הנוכחי בדפדפן.
     *
     * @param driver מופע של WebDriver
     * @param fileNamePrefix קידומת לשם הקובץ
     * @return קובץ של צילום המסך, או null אם הייתה שגיאה
     */
    public static File takeScreenshot(WebDriver driver, String fileNamePrefix) {
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // תאריך ושעה לקובץ
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = fileNamePrefix + "_" + timestamp + ".png";
            String path = "screenshots/" + fileName;

            // יצירת תיקייה אם לא קיימת
            Files.createDirectories(Paths.get("screenshots"));
            File destFile = new File(path);
            Files.copy(srcFile.toPath(), destFile.toPath());

            System.out.println("Screenshot saved: " + path);
            return destFile;
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
            return null;
        }
    }
}
