import org.junit.jupiter.api.*;

import com.example.Message;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.reflect.Field;

public class MessageTest {

    private Message message1;
    private Message message2;

    @BeforeEach
    public void setUp() {
        message1 = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        message2 = new Message(2, "+27857597588", "Hi Keegan, did you receive the payment?");
    }

    @AfterEach
    public void tearDown() throws Exception {
        //  Clean up file after each test
        File file = new File("messages.json");
        if (file.exists()) {
            file.delete();
        }

        //  Reset static fields using reflection
        Field totalMessagesField = Message.class.getDeclaredField("totalMessages");
        totalMessagesField.setAccessible(true);
        totalMessagesField.setInt(null, 0);

        Field messagesField = Message.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        messagesField.set(null, new Message[10]);
    }

    
    // Message Length Validation
    @Test
    public void testMessageLengthWithinLimit() {
        assertTrue(message1.checkMessageLength(message1.getMessage()),
                "Expected message length to be valid");
    }

    @Test
    public void testMessageLengthExceedsLimit() {
        String longMessage = "x".repeat(251);
        assertFalse(message1.checkMessageLength(longMessage),
                "Message exceeds 250 characters by " + (longMessage.length() - 250) + ", please reduce size");
    }

    
    // Recipient Number Validation
    
    @Test
    public void testValidRecipientNumber() {
        assertTrue(message1.checkRecipientCell(message1.getRecipient()),
            "Expected valid recipient number " + message1.getRecipient() + " to pass validation. Cellphone number successfully captured.");

        assertTrue(message1.checkRecipientCell("+27888968976"),
            "Cellphone number successfully captured.");

        assertFalse(message1.checkRecipientCell("27888968976"),
            "Expected 27888968976 to be invalid because it lacks the '+' prefix. Cellphone number is incorrectly formatted or does not contain an international code. Please correct the number and try again");

        assertEquals(true, message1.checkRecipientCell("+27888968976"),
            "Expected +27888968976 to return true for recipient number validation. Cellphone number successfully captured.");

        assertEquals(false, message1.checkRecipientCell("27888968976"),
            "Expected 27888968976 to return false for recipient number validation. Cellphone number is incorrectly formatted or does not contain an international code. Please correct the number and try again");
    }

    // Message ID Validation
    
    @Test
    public void testMessageIDGenerated() {
        assertTrue(message1.checkMessageID(),
                "Expected generated message ID to be 10 digits long");
        assertEquals(10, message1.getMessageID().length(),
                "Expected message ID length to be 10");
    }

    
    // Message Hash Validation
    
    @Test
    public void testMessageHashFormat() {
        String hash = message1.getMessageHash();
        assertNotNull(hash, "Message hash should not be null");

        String[] parts = hash.split(":");
        assertEquals(3, parts.length, "Expected 3 parts in the hash");
        assertEquals(message1.getMessageID().substring(0, 2), parts[0], "Expected prefix from message ID");
        assertTrue(parts[1].matches("\\d+"), "Expected numeric message number");
        assertTrue(hash.equals(hash.toUpperCase()), "Expected hash to be uppercase");
    }

    
    // Sending Messages
    
    @Test
    public void testSendMessageOptionSend() {
        String result = message1.sendMessage("send");
        assertEquals("Message sent", result);
    }

    @Test
    public void testSendMessageOptionStore() {
        String result = message1.sendMessage("store");
        assertEquals("Message stored for later", result);
    }

    @Test
    public void testSendMessageOptionDisregard() {
        String result = message1.sendMessage("disregard");
        assertEquals("Message disregarded", result);
    }

    @Test
    public void testSendMessageOptionInvalid() {
        String result = message1.sendMessage("unknown");
        assertEquals("Invalid choice", result);
    }

  
    // File Storage Test
   
    @Test
    public void testStoreMessageCreatesFile() throws Exception {
        message1.storeMessage();
        File file = new File("messages.json");
        assertTrue(file.exists(), "Expected messages.json file to be created");

        String content = Files.readString(Paths.get("messages.json"));
        assertTrue(content.contains(message1.getMessageID()), "File should contain message ID");
    }

 
    // Total Messages Count
    
    @Test
    public void testReturnTotalMessages() {
        int total = Message.returnTotalMessages();
        assertTrue(total >= 2, "Expected total messages to be at least 2 after setup");
    }

    
    // Array Expansion Logic
    
    @Test
    public void testArrayExpansionBeyondInitialCapacity() {
        int initialTotal = Message.returnTotalMessages();
        for (int i = 0; i < 15; i++) {
            new Message(i, "+27711111111", "Expansion Test");
        }
        int newTotal = Message.returnTotalMessages();
        assertTrue(newTotal >= initialTotal + 15, "Expected total messages to increase when adding more than initial capacity");
    }

    // Print All Messages Test
    @Test
    public void testPrintMessagesContainsContent() {
        String printed = Message.printMessages();
        assertTrue(printed.contains(message1.getMessageID()), "Printed messages should contain message1 ID");
        assertTrue(printed.contains(message2.getMessageID()), "Printed messages should contain message2 ID");
    }
}
