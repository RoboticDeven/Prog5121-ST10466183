package com.example;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;


public class Message {
    private static int totalMessages = 0;
    private static Message[] messages = new Message[10];  // initial capacity of 10

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

    // Add message to array (and expand array if full)
    private static void addMessage(Message message) {
        if (totalMessages > messages.length) {
            Message[] newArray = new Message[messages.length * 2]; // double size
            System.arraycopy(messages, 0, newArray, 0, messages.length);
            messages = newArray;
        }
        messages[message.messageNumber - 1] = message;
    }

    // Generate a 10-digit ID
    private String generateMessageID() {
        long number = (long) (Math.random() * 1_000_000_0000L); // 10 digits
        return String.format("%010d", number);
    }

    // Generate message hash: format → 00:ID:FIRSTLAST
    private String createMessageHash() {
        String[] words = content.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String prefix = messageID.substring(0, 2);
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Handle user choice: Send / Disregard / Store
    public String sendMessage(String choice) {
        switch (choice.toLowerCase()) {
            case "send":
                return "Message sent";
            case "disregard":
                return "Message disregarded";
            case "store":
                storeMessage();
                return "Message stored for later";
            default:
                return "Invalid choice";
        }
    }

    // Save message to a file (JSON-like text)
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

    // Validate message ID (should be 10 digits)
    public boolean checkMessageID() {
        return messageID.length() == 10;
    }

    // Validate recipient phone number (South African +27 followed by 9 digits)
    public boolean checkRecipientCell(String recipient) {
        return recipient.matches("\\+27\\d{9}");
    }

    // Validate message length (< 250 chars)
    public boolean checkMessageLength(String message) {
        return message.length() <= 250 && message.length() > 0;
    }

    // Return total number of messages sent
    public static int returnTotalMessages() {
        return totalMessages;
    }
    // Show recent messages dialog
    public static String showRecentMessagesDialog() {
        return "Coming Soon";
    }

    // Getters
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

    // Return all messages as a string using a while loop
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < totalMessages) {
            sb.append(messages[i].toString()).append("\n\n");
            i++;
        }
        return sb.toString();
    }
}
