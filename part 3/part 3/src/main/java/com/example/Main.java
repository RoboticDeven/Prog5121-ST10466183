package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main {
    private static final Login login = new Login();
    private static final JSONArray messageList = new JSONArray();
    private static int messageLimit = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        boolean loggedIn = false;
        boolean registered = false;

        String regName = "", regSurname = "";

        // Load previously stored messages from JSON file
        Message.loadStoredMessages();

        while (running) {
            System.out.println("\nQuickChat Menu:");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("3) Quick Chat");
            System.out.println("4) Show recently sent messages");
            System.out.println("5) Quit");
            System.out.print("Select an option: ");
            String input = scanner.nextLine();

            switch (input) {

                case "1":  // Registration
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

                    login.registerUser(login.new User(username, password, phone, regName, regSurname));
                    System.out.println("Registration successful!");
                    registered = true;
                    break;

                case "2": // Login
                    System.out.print("Enter Username: ");
                    String loginUser = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String loginPass = scanner.nextLine();

                    if (login.returnLoginStatus(loginUser, loginPass)) {
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

                case "3": // Quick Chat
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
                            System.out.println("Please enter a message under 250 characters.");
                            continue;
                        }

                        System.out.println("Choose what to do with this message:");
                        System.out.println("1) Send Now");
                        System.out.println("2) Store for Later");
                        System.out.println("3) Disregard");
                        System.out.print("Enter choice: ");

                        String choice = scanner.nextLine();

                        if (choice.equals("1")) {

                            System.out.println(msg.sendMessage("send"));

                            String popupMessage =
                                    "Message ID: " + msg.getMessageID() +
                                    "\nRecipient: " + recipient +
                                    "\nMessage: " + content +
                                    "\nHash: " + msg.getMessageHash();

                            javax.swing.JOptionPane.showMessageDialog(
                                    null,
                                    popupMessage,
                                    "QuickChat Message",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                            );

                        } else if (choice.equals("2")) {
                            msg.sendMessage("store");
                            saveMessageToJSON(msg);

                        } else if (choice.equals("3")) {
                            msg.sendMessage("disregard");

                        } else {
                            System.out.println("Invalid choice.");
                            continue;
                        }

                        messagesSent++;
                    }

                    System.out.println("You have reached your message limit.");
                    break;

                case "4": // Message tools menu
                    messageTools(scanner);
                    break;

                case "5":
                    System.out.println("Goodbye! Total messages sent: " + Message.returnTotalMessages());
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    // Save message to JSON (for historical stored messages)
    private static void saveMessageToJSON(Message msg) {
        JSONObject obj = new JSONObject();
        obj.put("Message ID", msg.getMessageID());
        obj.put("Recipient", msg.getRecipient());
        obj.put("Content", msg.getMessage());
        obj.put("Hash", msg.getMessageHash());
        messageList.add(obj);

        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(messageList.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tools for searching, reporting, deleting
    private static void messageTools(Scanner scanner) {
        System.out.println("\nMessage Tools:");
        System.out.println("1) Display senders & recipients");
        System.out.println("2) Display longest message");
        System.out.println("3) Search message by ID");
        System.out.println("4) Search by recipient");
        System.out.println("5) Delete message by hash");
        System.out.println("6) Full sent messages report");
        System.out.print("Choose: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println(Message.displaySendersAndRecipients());
                break;

            case "2":
                System.out.println("Longest message:");
                System.out.println(Message.longestSentMessage());
                break;

            case "3":
                System.out.print("Enter Message ID: ");
                System.out.println(Message.searchByMessageID(scanner.nextLine()));
                break;

            case "4":
                System.out.print("Enter recipient number (+27…): ");
                System.out.println(Message.searchByRecipient(scanner.nextLine()));
                break;

            case "5":
                System.out.print("Enter message hash: ");
                System.out.println(Message.deleteByHash(scanner.nextLine()));
                break;

            case "6":
                System.out.println(Message.fullSentMessagesReport());
                break;

            default:
                System.out.println("Invalid option.");
        }
    }
}
