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
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        // The user gets three attempts to log in; after that the program exits
        while (!loginStatus && attempts < MAX_ATTEMPTS) {
            System.out.println("Enter your username: ");
            String loginName = scan.nextLine();

            System.out.println("Enter your password: ");
            String loginPassword = scan.nextLine();

            loginStatus = register.loginUser(loginName, loginPassword);
            System.out.println(register.returnLoginStatus(loginStatus));
            attempts++;
        }

        if (!loginStatus) {
            System.out.println("Too many failed login attempts. Exiting the application.");
            return;
        }

        System.out.println("Login complete.");

        // Messages can only be sent once the user has logged in successfully
        System.out.println("\nWelcome to QuickChat.");

        Message msg = new Message();

        int option;

        do {
            System.out.println("\n===== Select any of the option below =====");
            System.out.println("1. Send message");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");

            option = scan.nextInt();

            switch (option) {

                case 1 -> msg.sendingMessages();
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> System.out.println("Thank you for using QuickChat");
                default -> System.out.println("Invalid option!!");

            }

        } while (option != 3);

    }

}