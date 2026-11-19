 package poepartthree;

import javax.swing.JOptionPane;

/**
 * QuickChat main class: registration, login and the main menu.
 * @author Student
 */
public class PoepartThree {

    public static void main(String[] args) {
        Login register = new Login();
        Message msg = new Message();

        JOptionPane.showMessageDialog(null, "=====Register=====",
                "Registration", JOptionPane.INFORMATION_MESSAGE);
        String firstName = JOptionPane.showInputDialog(null, "Please enter your first name");
        String lastName = JOptionPane.showInputDialog(null, "Please enter your last name");
        String name = JOptionPane.showInputDialog(null, "Please enter your username");
        String password = JOptionPane.showInputDialog(null, "Please enter your password");
        String cellphone = JOptionPane.showInputDialog(null, "Please enter your cellphone (+27XXXXXXXXX)");

        // User pressed cancel on one of the dialogs
        if (firstName == null || lastName == null || name == null
                || password == null || cellphone == null) {
            return;
        }

        JOptionPane.showMessageDialog(null,
                register.registerUser(firstName, lastName, name, password, cellphone));
        if (!register.registerStatus(name, password, cellphone)) {
            return;
        }

        JOptionPane.showMessageDialog(null, "=====Login=====",
                "Login", JOptionPane.INFORMATION_MESSAGE);

        boolean status = false;
        while (!status) {
            String loginUsername = JOptionPane.showInputDialog(null, "Enter username to login");
            if (loginUsername == null) {
                return;
            }
            String loginPassword = JOptionPane.showInputDialog(null, "Enter password to login");
            if (loginPassword == null) {
                return;
            }
            status = register.loginUser(loginUsername, loginPassword);
            JOptionPane.showMessageDialog(null, register.returnLoginStatus(status));
        }

        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
        msg.loadStoredMessages(); // read the JSON file into the stored arrays

        int option;
        do {
            String menu = "=====Select any of the option below =====\n"
                    + "1.Send message\n"
                    + "2.Message management\n"
                    + "3.Exit\n"
                    + "4.Stored messages\n"
                    + "Select an option";
            String input = JOptionPane.showInputDialog(null,
                    menu, "Main menu", JOptionPane.QUESTION_MESSAGE);
            if (input == null) {
                option = 3;
            } else {
                try {
                    option = Integer.parseInt(input.trim());
                    switch (option) {
                        case 1 -> msg.sendingMessages();
                        case 2 -> msg.messageManagementMenu();
                        case 3 -> JOptionPane.showMessageDialog(null, "Good bye");
                        case 4 -> msg.storedMessagesMenu();
                        default -> JOptionPane.showMessageDialog(null, "Invalid option!!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    option = 0;
                    JOptionPane.showMessageDialog(null,
                            "Please enter a valid number",
                            "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } while (option != 3);
    }
}