## 📁 מבנה הפרויקט

src
├── main
│ └── java
│ └── הרחבות
│ ├── formtester
│ │ ├── FieldTester.java
─ │ FormTester.java ─ .
│ │ ├── FormTesterEngine.java
│ │ └── FormTestResult.java
│ ├── Responsive
│ │
├── DesktopStrategy.java │
│ │ ├── ResizeStrategy.java
│ │ ├── ResponsiveManager.java
│ │ └── TabletStrategy.java │ ├──
Drivermanager.java ─a ├a. ├── Logger.java │ ├── MyTestWatcher.java │ └── ScreenshotUtils.java ├── test │ └── java │ ─ דוגמה─.


---

## ✨ תכונות עיקריות

- ✅ **בדיקה גנרית של טפסים** – סריקה ואימות של כל שדה בטופס ללא קידוד ידני.
- 📱 **תמיכה ברספונסיביות** – אסטרטגיות נפרדות למובייל, טאבלט ודסקטופ. מנוהל באמצעות תבנית Strategy
- 🔍 **ולידציות חכמות** – כולל בדיקות ערכים, מצבים נדרשים, שדות חסרים ועוד.
- 🧩 **אדריכלות גמישה** – ניתן לשלב בפרויקטים אחרים כתשתית אוטומציה.
- 📸 **צילום מסך אוטומטי** – במקרה של כישלון, התמונה נשמרת לבדיקה.
-    **מנהלת את הדפדפן לפי סוג  באמצעות תבנית Singleton, כדי לוודא שקיים רק מופע אחד של WebDriver
- 📊 **תמיכה ב־ReportPortal** – ניתוח תוצאות ודוחות בזמן אמת.
-    **מחלצת מידע מתוך קובץ XML כדי לאפשר קונפיגורציה חיצונית של הפרויקט.
---

## 🛠 טכנולוגיות בשימוש

- Java
- Maven
- Selenium
- TestNG
- ReportPortal
- JUnit 5

---

⚙️ איך זה עובד?
FormScanner – מזהה את כל שדות הטופס.

FieldTester – מריץ בדיקות על כל שדה (לדוגמה: ריק, ערך לא תקין, וכדומה).

FormTesterEngine – מפעיל את כל המנגנון ומנהל את זרימת הבדיקה.

ResponsiveManager – מחליף בין אסטרטגיות תצוגה שונות (נייד, טאבלט, שולחן עבודה).

FormTestResult – מרכז את תוצאות הבדיקה.

📂 דוחות ותוצרים
התוצאות מופיעות ב-ReportPortal 

תמונות מה בדיקה נשמרות בתיקיית /screenshots.


🧑‍💻 נבנה על ידי אפרת לרר
חלק פרויקט פרקטיקום באוטומציה בחברת VERYSOFT
