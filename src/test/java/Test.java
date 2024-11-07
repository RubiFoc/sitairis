import org.example.BlockUserCommand;
import org.example.EditOrderCommand;
import org.example.XMLUtils;
import org.example.AdministratorActions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

class AdministratorActionsTest {

    @InjectMocks
    private AdministratorActions administratorActions;

    @Mock
    private BlockUserCommand blockUserCommandMock;

    @Mock
    private EditOrderCommand editOrderCommandMock;

    private Scanner scanner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scanner = new Scanner(System.in);
    }

    @Test
    void testPerformActionBlockUser() throws Exception {
        when(scanner.nextInt()).thenReturn(1); // Выбор "Блокировать пользователя"
        when(scanner.nextLine()).thenReturn("testUser");

        administratorActions.performAction(scanner);

        verify(blockUserCommandMock, times(1)).execute();
    }

    @Test
    void testPerformActionEditOrder() throws Exception {
        when(scanner.nextInt()).thenReturn(2); // Выбор "Редактировать заказ"
        when(scanner.nextLine()).thenReturn("testOrder");

        administratorActions.performAction(scanner);

        verify(editOrderCommandMock, times(1)).execute();
    }

    @Test
    void testPerformActionExit() {
        when(scanner.nextInt()).thenReturn(0); // Выбор "Выход"

        assertDoesNotThrow(() -> administratorActions.performAction(scanner));
    }
}
