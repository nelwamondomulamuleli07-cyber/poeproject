 package poepartthree;

/**
 * Handles user login and login messaging.
 * @author Student
 */
public class Login extends Registration {

    // Verifies the login details match the details stored at registration
    public boolean loginUser(String name, String password) {
        return name != null && password != null
                && name.equals(userName) && password.equals(passWord);
    }

    // Returns the required message for a successful or failed login
    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}