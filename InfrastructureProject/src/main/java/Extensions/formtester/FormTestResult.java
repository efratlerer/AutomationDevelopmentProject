package Extensions.formtester;

/**
 * מייצג תוצאה של בדיקת שדה בטופס.
 */
public class FormTestResult {

    /** שם השדה שנבדק */
    private final String fieldName;

    /** סטטוס הבדיקה ("הצלחה" / "כשל") */
    private final String status;

    /** הודעת שגיאה אם קיימת */
    private final String errorMessage;

    /**
     * בונה תוצאה חדשה עבור בדיקה של שדה.
     *
     * @param fieldName     שם השדה.
     * @param status        סטטוס הבדיקה.
     * @param errorMessage  הודעת שגיאה, אם קיימת.
     */
    public FormTestResult(String fieldName, String status, String errorMessage) {
        this.fieldName = fieldName;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    /**
     * תיאור טקסטואלי של תוצאת הבדיקה.
     *
     * @return מחרוזת עם פירוט השדה, הסטטוס והשגיאה (אם קיימת).
     */
    @Override
    public String toString() {
        return "שדה: " + fieldName + ", סטטוס: " + status + ", שגיאה: " + errorMessage;
    }
}
