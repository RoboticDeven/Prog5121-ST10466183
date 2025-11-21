package com.example;

public class Login{
    private User registeredUser; // Stores the single registered user

    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    //Password  check: at least 8 characters, a capital letter, a number, and a special character
    public boolean checkPasswordComplexity(String password) {
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
        return password.length() >= 8 && hasUppercase && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String phoneNumber) {
        // Matches +27 followed by 9 digits (South African format, e.g. +27821234567)
        return phoneNumber.matches("\\+27\\d{9}");
    }
    // Save the registered user
    public void registerUser(User user) {
        this.registeredUser = user;
    }
    // Validate login credentials
    public boolean returnLoginStatus(String username, String password) {
        if (registeredUser == null) return false;
        return registeredUser.getUsername().equals(username) &&
               registeredUser.getPassword().equals(password);
    }
// User class to hold user details
public class User {
    private String username;
    private String password;
    private String phoneNumber;
    private String name;
    private String surname;

    public User(String username, String password, String phoneNumber, String name, String surname) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
}

}
