# Prog5121 POE-ST10466183
Thorn Scheepers  
Java chatapp

# QuickChat – User Registration, Login, and Messaging System

---

##  Overview

**QuickChat** is a Java Swing-based desktop application that allows users to:
- Register an account with validation for username, password, and phone number.
- Log in using their registered credentials.
- Send, store, or disregard chat messages.
- Save messages to a JSON file and review sent messages.

It includes robust input validation, object-oriented design, and comprehensive **JUnit 5** test coverage for both **Login** and **Message** functionalities.

---

##  Features

###  User Registration
Captures and validates:
- Username  
- Password  
- Phone number  
- Name  
- Surname  

Ensures all inputs follow strict formatting and security requirements.

---

###  Input Validation Rules

| Field           | Validation Rule                                         | Example (✓)      | Invalid (✗)   |
|-----------------|--------------------------------------------------------|-------------------|---------------|
| **Username**    | Must contain `_` and be ≤ 5 characters                 | `kyl_1`           | `username`    |
| **Password**    | ≥ 8 chars, includes uppercase, lowercase, number, and special char | `Ch&&sec@ke99!`   | `password`    |
| **Phone Number**| Must start with `+27` and be exactly 12 characters     | `+27821234567`    | `27821234567` |

---

##  Messaging System

###  Message Features
Each message includes:
- **Message Number**
- **Message ID** (10-digit random number)
- **Recipient number** (+27 format)
- **Content**
- **Message hash** (unique identifier)

Users can choose to:
- **Send Now**
- **Store for Later** (saved to `messages.json`)
- **Disregard**

---

###  Message Validation
- Recipient number must follow `+27XXXXXXXXX` format.  
- Messages must be **≤ 250 characters**.  
- Messages can be printed using `Message.printMessages()`.

---
## Message Reporting & Search Features
- Displays all senders and recipients of sent messages
- Finds and returns the longest sent message
- Searches for a message by its unique message ID
- Searches for all messages sent to a specific recipient
- Deletes a message based on its hash value
- Generates a full report of all sent messages, including ID, recipient, content, and hash
- Stores all sent messages into a JSON file for persistence

##  Testing

JUnit 5 tests cover all **Login** and **Message** functionalities.

### `LoginTest.java`
- Username validation (valid/invalid)  
- Password complexity validation  
- Phone number format validation  
- Login authentication (success/failure)

### `MessageTest.java`
- Validates message length limits
- Validates recipient phone number format
- Checks message ID generation and hash format
- Tests sending, storing, and disregarding messages
- Verifies JSON file creation and storage of messages
- Tests dynamic array expansion for messages
- Tracks total number of messages
- Confirms all messages can be printed
- Ensures sent messages are correctly stored in memory
- Searches messages by ID and by recipient
- Finds the longest sent message
- Deletes messages by their hash
---

##  Project Structure

```
Prog5121-ST10466183/
├── .idea/
├── .vscode/
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Login.java         # Handles registration and login logic
│   │   │   ├── Main.java          # Entry point of the application
│   │   │   |── Message.java       # Handles message creation, storage, validation
│   │   │      
│   │   └── resources/
│   └── test/java/
│       ├── LoginTest.java         # JUnit tests for Login class
│       └── MessageTest.java       # JUnit tests for Message class
├── target/
├── pom.xml                        # Maven configuration
└── Github link.txt                # Repository URL
```

---

##  Example Run

```# QuickChat Example Session

QuickChat Menu:
1) Register
2) Login
3) Quick Chat
4) Show recently sent messages
5) Quit
Select an option: 1
Enter Username (must have _ and <=5 chars): user_
Enter Password (min 8 chars, capital, number, special): Pass123!
Enter Phone (+27 format): +27834567890
Enter Name: John
Enter Surname: Doe
Registration successful!

QuickChat Menu:
1) Register
2) Login
3) Quick Chat
4) Show recently sent messages
5) Quit
Select an option: 2
Enter Username: user_1
Enter Password: Pass123!
Welcome to QuickChat! John Doe
How many messages would you like to send? 1

QuickChat Menu:
1) Register
2) Login
3) Quick Chat
4) Show recently sent messages
5) Quit
Select an option: 3
Enter recipient (+27 format): +27839876543
Enter message: Hello, are you coming to lunch?
Choose what to do with this message:
1) Send Now
2) Store for Later
3) Disregard
Enter choice: 1
Message sent

[Popup dialog appears:]
QuickChat Message
---------------------------
Message ID: 0123456789
Recipient: +27839876543
Message: Hello, are you coming to lunch?
Hash: 01:1:HelloLunch

You have reached your message limit.

QuickChat Menu:
1) Register
2) Login
3) Quick Chat
4) Show recently sent messages
5) Quit
Select an option: 4

Message Tools:
1) Display senders & recipients
2) Display longest message
3) Search message by ID
4) Search by recipient
5) Delete message by hash
6) Full sent messages report
Choose: 1
Sender: You → Recipient: +27839876543

QuickChat Menu:
1) Register
2) Login
3) Quick Chat
4) Show recently sent messages
5) Quit
Select an option: 5
Goodbye! Total messages sent: 1
```

---

##  Dependencies

| Dependency                              | Version   | Purpose                |
|------------------------------------------|-----------|------------------------|
| `org.junit.jupiter:junit-jupiter`        | 5.10.2    | Unit testing           |
| `com.googlecode.json-simple:json-simple` | 1.1.1     | JSON writing support   |
| `org.json:json`                         | 20250517  | JSON object manipulation|

---

##  Build Configuration

**Java Version:** 21  
**Maven Compiler Plugin:** 3.11.0  

###  Build Command

```bash
mvn clean compile
```

# References

   - Using AI tools can assist with programming tasks 
       (ChatGPT, personal communication, 3 September 2025; GitHub, 2025).
  
      - ChatGPT. (2025) Response to programming query on user registration and login system. 
        Personal communication with Thorn Scheepers, 3 September.
   
    - OpenAI. (2025) ChatGPT [AI language model]. 
        Available at: https://chat.openai.com/ (Accessed: 3 September 2025).
   
  - GitHub. (2025) GitHub Copilot [AI code generation tool]. 
       Available at: https://github.com/features/copilot (Accessed: 3 September 2025).
     
      - GitHub Copilot. (2025) AI code generation tool. 
       Available at: https://github.com/features/copilot (Accessed:  28 August).
- ChatGPT. (2025) Response to programming query on JUnit 5 GitHub Actions workflow. Personal communication with Thorn Scheepers, 12 October.

- GitHub Copilot. (2025) AI code generation tool. Available at: https://github.com/features/copilot (Accessed: 12 October 2025).

- OpenAI. (2025) ChatGPT [AI language model]. Response to query on Java Maven project structure. Personal communication with Thorn Scheepers, (Accessed: 5 October 2025)

- GitHub. (2025) GitHub Copilot [AI code generation tool]. Response to query on automated testing workflow. Available at: https://github.com/features/copilot (Accessed: 12 October 2025).

- ChatGPT. (2025) Assistance with Java JUnit array handling and file storage. Personal communication with Thorn Scheepers, (Accessed: 9 October 2025).

- GitHub Copilot. (2025) AI-assisted programming in Java for student projects. Available at: https://github.com/features/copilot (Accessed: 10 October 2025).


   # Licence
This project is for educational purposes.
