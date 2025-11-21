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
// Sent Messages Array Population Test
    // Helper to reset Message static fields when Message.resetAll() is not available
    private void resetAll() {
        try {
            Field totalMessagesField = Message.class.getDeclaredField("totalMessages");
            totalMessagesField.setAccessible(true);
            totalMessagesField.setInt(null, 0);

            Field messagesField = Message.class.getDeclaredField("messages");
            messagesField.setAccessible(true);
            messagesField.set(null, new Message[10]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() throws Exception {
        resetAll();

        // Create messages
        Message m1 = new Message(1, "+278345557896", "Did you get the cake?");
        Message m2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message m3 = new Message(3, "+27834884567", "Yohoooo, I am at your gate.");
        Message m4 = new Message(4, "+27838884567", "It is dinner time !");

        // Send/store/disregard
        m1.sendMessage("send");
        m2.sendMessage("store");
        m3.sendMessage("disregard");
        m4.sendMessage("send");

        // Ensure sent messages array contains the sent message
        Field sentMessagesField = Message.class.getDeclaredField("sentMessages");
        sentMessagesField.setAccessible(true);
        String[] sentMessagesArray = (String[]) sentMessagesField.get(null);
        assertTrue(java.util.Arrays.asList(sentMessagesArray).contains("Did you get the cake?"));

    }
    
//Display Longest Message Test
    @Test
    public void testDisplayLongestMessage() {
    Message m1 = new Message(5, "+278345557896", "Did you get the cake?");
    Message m2 = new Message(6, "+27838884567", "Where are you? You are late! I have asked you to be on time.");

    m1.sendMessage("send");
    m2.sendMessage("send");

    String longest = Message.longestSentMessage();

    assertEquals("Where are you? You are late! I have asked you to be on time.", longest,
            "System should return the longest message.");
}

    @Test
    public void testSearchByMessageID() {
    resetAll();

    Message m4 = new Message(7, "0838884567", "It is dinner time !");
            m4.sendMessage("send");

            String id = m4.getMessageID();
            String result = Message.searchByMessageID(id);

            assertEquals("Recipient: 0838884567\nMessage: It is dinner time !", result,
                    "Expected system to find the message with the given ID.");
        }
        
   @Test
    public void testSearchMessagesByRecipient() {
        resetAll();

        Message m2 = new Message(8, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        Message m5 = new Message(9, "+27838884567", "Ok, I am leaving without you.");

        m2.sendMessage("send");
        m5.sendMessage("send");

        String result = Message.searchByRecipient("+27838884567");

        // Expected string concatenates both messages with newline
        String expected = "Where are you? You are late! I have asked you to be on time.\n" +
                          "Ok, I am leaving without you.\n";

        assertEquals(expected, result,
                "Expected both messages to be returned for the given recipient.");
    }

@Test
public void testDeleteMessageByHash() {
    Message m2 = new Message(2, "+27838884567",
            "Where are you? You are late! I have asked you to be on time.");
    m2.sendMessage("send");

    String hash = m2.getMessageHash();

    String result = Message.deleteByHash(hash);

    assertEquals("Message deleted.", result,
            "Expected message to be successfully deleted.");
}

}
