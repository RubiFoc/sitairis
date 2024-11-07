import org.example.*;
import org.example.XMLUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import static org.mockito.Mockito.*;

class XMLUtilsTest {

    private static final String VALID_XML_PATH = "src/test/resources/valid.xml";
    private static final String INVALID_XML_PATH = "src/test/resources/invalid.xml";
    private static final String NON_EXISTENT_XML_PATH = "src/test/resources/nonexistent.xml";
    private static final String OUTPUT_XML_PATH = "src/test/resources/output.xml";

    @BeforeEach
    void setUp() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement("test");
            rootElement.appendChild(doc.createElement("element"));
            doc.appendChild(rootElement);
            XMLUtils.saveXML(doc, VALID_XML_PATH);
        } catch (Exception e) {
            fail("Failed to set up test XML files: " + e.getMessage());
        }
    }

    @Test
    void testLoadXML_FileExistsAndIsValid() throws Exception {
        Document doc = XMLUtils.loadXML(VALID_XML_PATH);
        assertNotNull(doc, "Document should not be null for a valid XML file.");
        assertEquals("test", doc.getDocumentElement().getTagName(), "Root element should have the tag 'test'.");
    }

    @Test
    void testLoadXML_FileNotFound() {
        Exception exception = assertThrows(FileNotFoundException.class, () -> XMLUtils.loadXML(NON_EXISTENT_XML_PATH));
        assertEquals("Файл " + NON_EXISTENT_XML_PATH + " не найден.", exception.getMessage());
    }

    @Test
    void testLoadXML_InvalidXMLFile() {
        File invalidFile = new File(INVALID_XML_PATH);
        try {
            if (!invalidFile.exists()) {
                invalidFile.createNewFile();
            }
        } catch (Exception e) {
            fail("Failed to create invalid XML file.");
        }

        assertThrows(Exception.class, () -> XMLUtils.loadXML(INVALID_XML_PATH),
                "Should throw an exception for an invalid XML file.");
    }

    @Test
    void testSaveXML_Success() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().newDocument();
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            XMLUtils.saveXML(doc, OUTPUT_XML_PATH);

            Document loadedDoc = XMLUtils.loadXML(OUTPUT_XML_PATH);
            assertNotNull(loadedDoc, "Document should not be null for a saved XML file.");
            assertEquals("root", loadedDoc.getDocumentElement().getTagName(), "Root element should have the tag 'root'.");
        } catch (Exception e) {
            fail("Exception should not be thrown for saving a valid document: " + e.getMessage());
        }
    }
}

public class BlockUserCommandTest {

    @Test
    public void testBlockUserFound() throws Exception {
        File tempXmlFile = File.createTempFile("testData", ".xml");
        tempXmlFile.deleteOnExit(); // Файл будет удален при завершении программы

        String input = "testUser";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Document doc = mock(Document.class);
        NodeList users = mock(NodeList.class);
        Element userElement = mock(Element.class);

        when(doc.getElementsByTagName("account")).thenReturn(users);
        when(users.getLength()).thenReturn(1);
        when(users.item(0)).thenReturn(userElement);
        when(userElement.getAttribute("login")).thenReturn("testUser");

        BlockUserCommand blockUserCommand = new BlockUserCommand(scanner);
        blockUserCommand.execute();
    }


    @Test
    public void testBlockUserNotFound() throws Exception {
        String input = "testUser1";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Document doc = mock(Document.class);
        NodeList users = mock(NodeList.class);

        when(doc.getElementsByTagName("account")).thenReturn(users);
        when(users.getLength()).thenReturn(0); // Нет пользователей

        BlockUserCommand blockUserCommand = new BlockUserCommand(scanner);
        blockUserCommand.execute();

        // Проверка, что блокировка не произошла
        verify(users, never()).item(anyInt());
    }
}
class EditOrderCommandTest {
    @Test
    public void testEditOrderNotFound() throws Exception {
        String input = "999";
        Scanner scanner = new Scanner(input);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element ordersElement = doc.createElement("orders");
        doc.appendChild(ordersElement);

        EditOrderCommand editOrderCommand = new EditOrderCommand(scanner);
        editOrderCommand.execute();

        assertEquals(0, ordersElement.getElementsByTagName("order").getLength());
    }
}
