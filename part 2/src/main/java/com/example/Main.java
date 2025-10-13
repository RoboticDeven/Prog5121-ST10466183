package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {
    private static final Login login = new Login();
    private static final JSONArray messageList = new JSONArray();
    private static int messageLimit = -1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean registered = false;
        boolean loggedIn = false;

        String regName = "", regSurname = "";

        while (running) {
            System.out.println("\nQuickChat Menu:");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("3) Quick Chat");
            System.out.println("4) Show recently sent messages (Coming soon!)");
            System.out.println("5) Quit");
            System.out.print("Select an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    // Registration
                    System.out.print("Enter Username (must have _ and <=5 chars): ");
                    String username = scanner.nextLine();
                    System.out.print("Enter Password (min 8 chars, capital, number, special): ");
                    String password = scanner.nextLine();
                    System.out.print("Enter Phone (+27 format): ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    regName = scanner.nextLine();
                    System.out.print("Enter Surname: ");
                    regSurname = scanner.nextLine();

                    if (!login.checkUserName(username)) {
                        System.out.println("Username is not correctly formatted.");
                        break;
                    }
                    if (!login.checkPasswordComplexity(password)) {
                        System.out.println("Password is not correctly formatted.");
                        break;
                    }
                    if (!login.checkCellPhoneNumber(phone)) {
                        System.out.println("Phone number is not correctly formatted.");
                        break;
                    }

                    Login.User newUser = login.new User(username, password, phone, regName, regSurname);
                    login.registerUser(newUser);
                    System.out.println("Registration successful!");
                    registered = true;
                    break;

                case "2":
                    // Login
                    System.out.print("Enter Username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String loginPassword = scanner.nextLine();

                    if (login.returnLoginStatus(loginUsername, loginPassword)) {
                        System.out.println("Welcome to QuickChat! " + regName + " " + regSurname);
                        loggedIn = true;
                        System.out.print("How many messages would you like to send? ");
                        try {
                            messageLimit = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException ex) {
                            messageLimit = 5;
                        }
                    } else {
                        System.out.println("Invalid login credentials.");
                    }
                    break;

                case "3":
                    // Quick Chat
                    if (!loggedIn) {
                        System.out.println("Please login first.");
                        break;
                    }
                    int messagesSent = Message.returnTotalMessages();
                    while (messagesSent < messageLimit) {
                        System.out.print("Enter recipient (+27 format): ");
                        String recipient = scanner.nextLine();
                        System.out.print("Enter message: ");
                        String content = scanner.nextLine();

                        Message msg = new Message(messagesSent, recipient, content);

                        if (!msg.checkRecipientCell(recipient)) {
                            System.out.println("Invalid recipient phone number.");
                            continue;
                        }
                        if (!msg.checkMessageLength(content)) {
                            System.out.println("Please enter a message of less than 250 characters.");
                            continue;
                        }

                        System.out.println("Choose what to do with this message:");
                        System.out.println("1) Send Now");
                        System.out.println("2) Store for Later");
                        System.out.println("3) Disregard");
                        System.out.print("Enter choice: ");
                        String choice = scanner.nextLine();

                        switch (choice) {
                            case "1":
                                System.out.println(msg.sendMessage("send"));
                                for (int i = 1; i <= messagesSent + 1; i++) {
                                    String messageHash = msg.getMessageHash();
                                    String popupMessage = "Message Number: " + i +
                                            "\nMessage ID: " + msg.getMessageID() +
                                            "\nRecipient: " + recipient +
                                            "\nMessage: " + content +
                                            "\nMessage Hash: " + messageHash;
                                    javax.swing.JOptionPane.showMessageDialog(null, popupMessage, "QuickChat Message", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                                }
                                break;
                            case "2":
                                saveMessageToJSON(msg);
                                System.out.println(msg.sendMessage("store"));
                                break;
                            case "3":
                                System.out.println(msg.sendMessage("disregard"));
                                break;
                            default:
                                System.out.println("Invalid choice.");
                                continue;
                        }
                        
                        messagesSent++;
                    }
                    System.out.println("You've reached your message limit.");
                    break;
                case "4":
                    System.out.println(showRecentMessagesDialog());
                    break;
                case "5":
                    System.out.println("Thank you, hope to see you soon.\nTotal messages sent: " + Message.returnTotalMessages());
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    // SAVE MESSAGE TO JSON
    private static void saveMessageToJSON(Message msg) {
        JSONObject obj = new JSONObject();
        obj.put("Message ID", msg.getMessageID());
        obj.put("Recipient", msg.getRecipient());
        obj.put("Content", msg.getMessage());
        obj.put("Hash", msg.getMessageHash());
        messageList.add(obj);

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(messageList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SHOW RECENT MESSAGES
    private static String showRecentMessagesDialog() {
        if (messageList.isEmpty()) {
            return "No messages have been stored yet.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Recently stored messages:\n");
        for (Object obj : messageList) {
            JSONObject msgObj = (JSONObject) obj;
            sb.append("Message ID: ").append(msgObj.get("Message ID")).append("\n");
            sb.append("Recipient: ").append(msgObj.get("Recipient")).append("\n");
            sb.append("Content: ").append(msgObj.get("Content")).append("\n");
            sb.append("Hash: ").append(msgObj.get("Hash")).append("\n");
            sb.append("-------------------------\n");
        }
        return sb.toString();
    }
}
