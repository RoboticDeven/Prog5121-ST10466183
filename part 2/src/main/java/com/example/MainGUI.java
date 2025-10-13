package com.example;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MainGUI extends JFrame {
    private final Login login;
    private JTabbedPane tabbedPane;

    // Registration
    private JTextField regUsernameField, regPhoneField, regNameField, regSurnameField;
    private JPasswordField regPasswordField;
    private JTextArea regMessageArea;

    // Login
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    // Chat
    private JTextField recipientField;
    private JTextArea messageField, chatArea;

    private JSONArray messageList = new JSONArray();
    private int messageLimit = -1;

    public MainGUI() {
        // Prompt user to enter a tab number and switch to that tab
        String input = JOptionPane.showInputDialog(
            null,
            "Enter a tab number to open (1: Register, 2: Login, 3: Quick Chat):",
            "Tab Selection",
            JOptionPane.QUESTION_MESSAGE
        );
        int tabIndex = 0;
        if (input != null) {
            try {
            int num = Integer.parseInt(input.trim());
            if (num >= 1 && num <= 3) {
                tabIndex = num - 1;
            }
            } catch (NumberFormatException ex) {
            // Default to first tab if input is invalid
            tabIndex = 0;
            }
        }
        login = new Login();
        setTitle("QuickChat");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. Register", createRegisterPanel());
        tabbedPane.addTab("2. Login", createLoginPanel());
        tabbedPane.addTab("3. Quick Chat", createQuickChatPanel());
        tabbedPane.setEnabledAt(2, false);

        add(tabbedPane);
        setVisible(true);
    }

    //  REGISTER PANEL 
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));

        regUsernameField = new JTextField();
        regPasswordField = new JPasswordField();
        regPhoneField = new JTextField();
        regNameField = new JTextField();
        regSurnameField = new JTextField();
        regMessageArea = new JTextArea();
        regMessageArea.setEditable(false);

        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(regUsernameField);
        panel.add(new JLabel("Password:"));
        panel.add(regPasswordField);
        panel.add(new JLabel("Phone (+27 format):"));
        panel.add(regPhoneField);
        panel.add(new JLabel("Name:"));
        panel.add(regNameField);
        panel.add(new JLabel("Surname:"));
        panel.add(regSurnameField);
        panel.add(registerButton);
        panel.add(new JLabel());
        panel.add(new JScrollPane(regMessageArea));

        registerButton.addActionListener(e -> {
            String username = regUsernameField.getText();
            String password = new String(regPasswordField.getPassword());
            String phone = regPhoneField.getText();
            String name = regNameField.getText();
            String surname = regSurnameField.getText();

            if (!login.checkUserName(username)) {
                regMessageArea.setText("Username is not correctly formatted, please ensure your username contains an underscore and is no more than 5 characters in length.");
                return;
            }
            if (!login.checkPasswordComplexity(password)) {
                regMessageArea.setText("Password is not correctly formatted, please ensure your password contains at least eight characters, a capital letter, a number, and a special character.");
                return;
            }
            if (!login.checkCellPhoneNumber(phone)) {
                regMessageArea.setText("Phone number is not correctly formatted, or does not contain international code.");
                return;
            }

            Login.User newUser = login.new User(username, password, phone, name, surname);
            login.registerUser(newUser);
            regMessageArea.setText("Registration successful!");
        });

        return panel;
    }

    // LOGIN PANEL 
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        loginUsernameField = new JTextField();
        loginPasswordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(loginUsernameField);
        panel.add(new JLabel("Password:"));
        panel.add(loginPasswordField);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = loginUsernameField.getText();
            String password = new String(loginPasswordField.getPassword());

            if (login.returnLoginStatus(username, password)) {
                JOptionPane.showMessageDialog(this, "Welcome to QuickChat! " + regNameField.getText() + " " + regSurnameField.getText());
                tabbedPane.setEnabledAt(2, true);
                tabbedPane.setSelectedIndex(2);

                String input = JOptionPane.showInputDialog("How many messages would you like to send?");
                try {
                    messageLimit = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    messageLimit = 5;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login credentials.");
            }
        });

        return panel;
    }

    //  QUICK CHAT PANEL 
    private JPanel createQuickChatPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Top form
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        recipientField = new JTextField();
        messageField = new JTextArea(5, 20);
        topPanel.add(new JLabel("Recipient (+27 format):"));
        topPanel.add(recipientField);
        topPanel.add(new JLabel("Message:"));
        topPanel.add(new JScrollPane(messageField));

        // Chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane chatScroll = new JScrollPane(chatArea);
        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScroll.setPreferredSize(new Dimension(200, 200));

        // Buttons
        JButton sendButton = new JButton("Send Message");
        JButton showButton = new JButton("Show Recently Sent");
        JButton quitButton = new JButton("Quit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(showButton);
        buttonPanel.add(quitButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(chatScroll, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> {
            if (messageLimit != -1 && Message.returnTotalMessages() >= messageLimit) {
                JOptionPane.showMessageDialog(this, "You’ve reached your message limit.");
                return;
            }

            String recipient = recipientField.getText();
            String content = messageField.getText();

            // Use a unique message ID, e.g., total messages + 1
            int messageID = Message.returnTotalMessages() + 1;
            Message msg = new Message(messageID, recipient, content);

            if (!msg.checkRecipientCell(recipient)) {
                JOptionPane.showMessageDialog(this, "Invalid recipient phone number.");
                return;
            }

            if (!msg.checkMessageLength(content)) {
                JOptionPane.showMessageDialog(this, "Please enter a message of less than 250 characters.");
                return;
            }

            // Send button menu
            String[] options = {"Send Now", "Store for Later", "Disregard"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choose what you want to do with this message:",
                    "Message Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            // Handle user choice
            switch (choice) {
                case 0:
                    chatArea.append(msg.toString() + "\n\n");
                    JOptionPane.showMessageDialog(this, msg.sendMessage("send"));
                    break;
                case 1:
                    chatArea.append("Stored Message:\n" + msg.toString() + "\n\n");
                    saveMessageToJSON(msg);
                    JOptionPane.showMessageDialog(this, msg.sendMessage("store"));
                    break;
                case 2:
                    JOptionPane.showMessageDialog(this, msg.sendMessage("disregard"));
                    break;
                default:
                    // Do nothing
                    break;
            }

            // Clear fields after action
            messageField.setText("");
        });
        
        showButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Coming soon!");
        });
        /*showButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, Message.printMessages());
        });*/

        quitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thank you, hope to see you soon.\nTotal messages sent: " + Message.returnTotalMessages());
            System.exit(0);
        });

        return panel;
    }

    // SAVE MESSAGE TO JSON
    private void saveMessageToJSON(Message msg) {
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
}
