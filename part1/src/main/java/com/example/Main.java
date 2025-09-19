package com.example;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login authService = new Login();

        // Registration process
        System.out.println("-- Registration --");
        System.out.print("Enter a Username(Must have a _ and five characters long): ");
        String username = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Surname: ");
        String surname = scanner.nextLine();
        System.out.print("Password(Must be 8 characters ): ");
        String password = scanner.nextLine();
        System.out.print("Phone Number(Must include international code +27): ");
        String phoneNumber = scanner.nextLine();
        
        // Create user object
        Login.User user = authService.new User(username, password, phoneNumber, name, surname);
        boolean valid = true;
        
        // Validate inputs
        if (authService.checkUserName(user.getUsername())) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println(" Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.");
            valid = false;
        }

        if (authService.checkPasswordComplexity(user.getPassword())) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println(" Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number, and a special character.");
            valid = false;
        }

        if (authService.checkCellPhoneNumber(user.getPhoneNumber())) {
            System.out.println("Phone number successfully added.");
        } else {
            System.out.println(" Cell phone number incorrectly formatted or does not contain international code.");
            valid = false;
        }

        if (!valid) {
            System.out.println(" Registration failed. Please try again.");
            scanner.close();
            return;
        }

        // Save user
        authService.registerUser(user);

        System.out.println("\n Registration complete. Now please log in.\n");

        // Login process
        System.out.print("Enter username to login: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine();

        if (authService.returnLoginStatus(loginUsername, loginPassword)) {
            System.out.println(" Welcome " + user.getName() + " " + user.getSurname() + ", it is great to see you again.");
        } else {
            System.out.println(" Username or password incorrect, please try again.");
        }

        scanner.close();
    }

}
