package Extensions;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * מחלקה לשליפת נתונים מתוך קובץ XML (כגון testConfig.xml).
 */
public class GetData {

    public GetData() {
    }

    /**
     * שולף ערך של תג XML לפי שם התג.
     *
     * @param nodeName שם התג (Node) בקובץ ה-XML
     * @return הערך של התג, או null אם לא נמצא או שהייתה שגיאה
     */
    public static String getData(String nodeName) {
        DocumentBuilder dBuilder;
        Document doc = null;
        File fXmlFile = new File("./testConfig.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(fXmlFile);
        } catch (Exception e) {
            System.out.println("Exception in reading XML file: " + e);
        }

        if (doc != null) {
            doc.getDocumentElement().normalize();
            return doc.getElementsByTagName(nodeName).item(0).getTextContent();
        }

        return null;
    }
}
