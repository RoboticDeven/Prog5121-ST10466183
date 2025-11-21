package com.example;

import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Message {

    //NEW ARRAYS
    private static String[] sentMessages = new String[50];
    private static String[] disregardedMessages = new String[50];
    private static Message[] storedMessages = new Message[50];
    private static String[] messageHashes = new String[50];
    private static String[] messageIDs = new String[50];

    private static int sentCount = 0;
    private static int disregardedCount = 0;
    private static int storedCount = 0;

    //ORIGINAL FIELDS 
    private static int totalMessages = 0;
    private static Message[] messages = new Message[10];

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String content;
    private final String messageHash;

    // Constructor
    public Message(int i, String recipient, String content) {
        this.messageID = generateMessageID();
        this.messageNumber = ++totalMessages;
        this.recipient = recipient;
        this.content = content;
        this.messageHash = createMessageHash();

        addMessage(this);
    }

    // Add to internal message list
    private static void addMessage(Message message) {
        if (totalMessages > messages.length) {
            Message[] newArray = new Message[messages.length * 2];
            System.arraycopy(messages, 0, newArray, 0, messages.length);
            messages = newArray;
        }
        messages[message.messageNumber - 1] = message;
    }

    // Generate a 10-digit ID
    private String generateMessageID() {
        long number = (long) (Math.random() * 1_000_000_0000L);
        return String.format("%010d", number);
    }

    // Create message hash
    private String createMessageHash() {
        String[] words = content.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Handle user choice updates
    public String sendMessage(String choice) {
        switch (choice.toLowerCase()) {

            case "send":
                sentMessages[sentCount] = content;
                messageHashes[sentCount] = messageHash;
                messageIDs[sentCount] = messageID;
                sentCount++;
                return "Message sent";

            case "disregard":
                disregardedMessages[disregardedCount++] = content;
                return "Message disregarded";

            case "store":
                storeMessage();
                storedMessages[storedCount++] = this;
                return "Message stored for later";

            default:
                return "Invalid choice";
        }
    }

    // Store message into messages.json
    public void storeMessage() {
        JSONObject json = new JSONObject();
        json.put("messageID", messageID);
        json.put("messageHash", messageHash);
        json.put("recipient", recipient);
        json.put("content", content);

        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load stored messages from JSON file
    public static void loadStoredMessages() {
        try (Scanner scanner = new Scanner(new File("messages.json"))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                JSONObject obj = new JSONObject(line);

                Message msg = new Message(
                        0,
                        obj.getString("recipient"),
                        obj.getString("content")
                );

                storedMessages[storedCount++] = msg;
            }
        } catch (Exception e) {
            System.out.println("No stored messages found.");
        }
    }

    // VALIDATIONS
    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    public boolean checkRecipientCell(String recipient) {
        return recipient.matches("\\+27\\d{9}");
    }

    public boolean checkMessageLength(String message) {
        return message.length() <= 250 && message.length() > 0;
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

//GETTERS
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return content; }
    public String getMessageHash() { return messageHash; }

    @Override
    public String toString() {
        return "Message ID: " + messageID +
                "\nRecipient: " + recipient +
                "\nMessage: " + content +
                "\nMessage Hash: " + messageHash;
    }

    // Return all messages
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < totalMessages) {
            sb.append(messages[i].toString()).append("\n\n");
            i++;
        }
        return sb.toString();
    }
    // Return first sent message
    public static String firstSentMessage() {
    return sentCount > 0 ? sentMessages[0] : "";
}
    // REPORTING & SEARCH FEATURES 

    // Display sender & recipient of all sent messages
    public static String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentCount; i++) {
            if (messages[i] != null)
                sb.append("Sender: You → Recipient: ")
                        .append(messages[i].recipient)
                        .append("\n");
        }
        return sb.toString();
    }

    // Longest sent message
    public static String longestSentMessage() {
        String longest = "";
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null && sentMessages[i].length() > longest.length()) {
                longest = sentMessages[i];
            }
        }
        return longest;
    }

// Search by message ID
public static String searchByMessageID(String id) {
    for (int i = 0; i < sentCount; i++) {
        if (messageIDs[i] != null && messageIDs[i].equals(id)) {
            // Find the corresponding Message object
            Message m = null;
            for (int j = 0; j < totalMessages; j++) {
                if (messages[j] != null && messages[j].getMessageID().equals(id)) {
                    m = messages[j];
                    break;
                }
            }
            String recipient = (m != null) ? m.getRecipient() : "Unknown";
            return "Recipient: " + recipient + "\nMessage: " + sentMessages[i];
        }
    }
    return "Message ID not found.";
}



    // Search by recipient
    public static String searchByRecipient(String phone) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sentCount; i++) {
        // Find the corresponding Message object for the sent message
        for (int j = 0; j < totalMessages; j++) {
            if (messages[j] != null && messages[j].getMessage().equals(sentMessages[i])) {
                if (messages[j].getRecipient().equals(phone)) {
                    sb.append(sentMessages[i]).append("\n");
                }
                break;
            }
        }
    }
    return sb.length() == 0 ? "No messages found." : sb.toString();
}


    // Delete using message hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < sentCount; i++) {
            if (messageHashes[i] != null && messageHashes[i].equals(hash)) {
                messages[i] = null;
                sentMessages[i] = null;
                messageHashes[i] = null;
                messageIDs[i] = null;
                return "Message deleted.";
            }
        }
        return "Hash not found.";
    }

    // Full report of all sent messages
    public static String fullSentMessagesReport() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < sentCount; i++) {
            if (messages[i] != null) {
                sb.append("Message ID: ").append(messageIDs[i])
                        .append("\nRecipient: ").append(messages[i].recipient)
                        .append("\nMessage: ").append(sentMessages[i])
                        .append("\nHash: ").append(messageHashes[i])
                        .append("\n---------------------------\n");
            }
        }

        return sb.toString();
    }
    

    // Store all sent messages into messages.json (appends as JSON lines)
    public static void storeAllSentMessages() {
    try (FileWriter file = new FileWriter("messages.json", true)) {

        for (int i = 0; i < sentCount; i++) {

            String id = messageIDs[i];
            if (id == null) continue;

            // Find the corresponding Message object
            Message m = null;
            for (int j = 0; j < totalMessages; j++) {
                if (messages[j] != null && messages[j].messageID.equals(id)) {
                    m = messages[j];
                    break;
                }
            }

            if (m == null) continue; // should never happen

            JSONObject json = new JSONObject();
            json.put("messageID", m.messageID);
            json.put("messageHash", m.messageHash);
            json.put("recipient", m.recipient);
            json.put("content", m.content);
            json.put("status", "sent");

            file.write(json.toString());
            file.write(System.lineSeparator());
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
   
}
