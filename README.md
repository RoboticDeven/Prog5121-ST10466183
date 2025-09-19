# Prog5121 POE-ST10466183
Thorn Scheepers  
Java chatapp

# User Registration and Login System  

 

---

## Features  

- **User Registration:** Captures username, password, phone number, name, and surname.  
- **Input Validation:** Enforces formatting rules for all user inputs.  
- **User Authentication:** Allows users to log in with their registered credentials.  
- **Welcome Message:** Displays a personalized greeting after successful login.  

---

## Validation Rules  

### Username  
- Must contain an underscore (`_`)  
- Must be **no more than 5 characters**  
- Example: `kyl_1` ✓, `username` ✗  

### Password  
- Must be **at least 8 characters long**  
- Must contain at least **one uppercase letter**  
- Must contain at least **one lowercase letter**  
- Must contain at least **one number**  
- Must contain at least **one special character**  
- Example: `Ch&&sec@ke99!` ✓, `password` ✗  

### Phone Number  
- Must start with `+27` (South African format)  
- Must be exactly **12 characters** (`+27` followed by 9 digits)  
- Example: `+27821234567` ✓, `27821234567` ✗  

---

## Project Structure  


Prog5121-ST10466183/
├── .idea/                    # IntelliJ IDEA project settings
├── .vscode/                  # VS Code project settings
├── src/  
│   ├── main/  
│   │   ├── java/  
│   │   │   └── com/example/  
│   │   │       ├── Login.java     # Main login logic
│   │   │       └── Main.java  # Application entry point
│   │   └── resources/             # Application resources
│   └── test/  
│       └── java/  
│           └── LoginTest.java      # Unit tests for login functionality  
├── target/                  # Build output directory  
├── Github link.txt          # Contains repository URL
└── pom.xml                  # Maven project configuration

# Example Run
-- Registration --  
Enter a Username(Must have a _ and five characters long): kyl_1
Username successfully captured.  
Enter Name: John  
Enter Surname: Doe  
Password(Must be 8 characters ): Password1!  
Password successfully captured.  
Phone Number(Must include international code +27): +27821234567  
Phone number successfully added.  

Registration complete. Now please log in.  
Enter username to login: kyl_1  
Enter password: Password1!  
Welcome John Doe, it is great to see you again.  

# Testing

The project includes JUnit 5 tests covering:
Username validation (valid and invalid cases)
Password complexity validation
Phone number validation
User login (success and failure scenarios)

# Dependencies
JUnit 5.10.2 – Unit testing framework

# References

   - Using AI tools can assist with programming tasks 
       (ChatGPT, personal communication, 3 September 2025; GitHub, 2025).
  
      - ChatGPT. (2025) Response to programming query on user registration and login system. 
        Personal communication with Thorn Scheepers, 3 September.
   
    - OpenAI. (2025) ChatGPT [AI language model]. 
        Available at: https://chat.openai.com/ (Accessed: 3 September 2025).
   
  - GitHub. (2025) GitHub Copilot [AI code generation tool]. 
       Available at: https://github.com/features/copilot (Accessed: 3 September 2025).
     
      - GitHub Copilot. (2024) AI code generation tool. 
       Available at: https://github.com/features/copilot (Accessed: 7 June 2024).

   # Licence
This project is for educational purposes.
