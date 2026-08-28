/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepartone;

import java.util.Scanner;

/**
 *
 * @author Student
 */
   public class PoePartOne {

public static void main(String[] args) {
Login register = new Login();
Scanner scan = new Scanner(System.in);

System.out.println("\n===== Register =====");

System.out.println("Please enter your first name: ");
String firstName = scan.nextLine();

System.out.println("Please enter your last name: ");
String lastName = scan.nextLine();

boolean registered = false;

// Keep asking for username, password and cell number until all three
// are correct, without restarting registration from the beginning
while (!registered) {
System.out.println("Please enter the username: ");
String name = scan.nextLine();

System.out.println("Enter your password: ");
String password = scan.nextLine();

System.out.println("Enter your cell number (e.g. +27831234567): ");
String cellphone = scan.nextLine();

System.out.println(register.registerUser(firstName, lastName, name, password, cellphone));

registered = register.registerStatus(name, password, cellphone);
}

System.out.println("\n===== Login =====");

boolean loginStatus = false;

while (!loginStatus) {
System.out.println("Enter your username: ");
String loginName = scan.nextLine();

System.out.println("Enter your password: ");
String loginPassword = scan.nextLine();

loginStatus = register.loginUser(loginName, loginPassword);
System.out.println(register.returnLoginStatus(loginStatus));
}

System.out.println("Login complete.");
}
}
