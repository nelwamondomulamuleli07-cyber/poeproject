/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepartone;

/**
 *
 * @author Student
 */
 public class Login extends Registration {

public boolean loginUser(String username, String password) {
return username.equals(userName) && password.equals(passWord);
}

public String returnLoginStatus(boolean loginStatus) {
if (loginStatus) {
return "Welcome " + firstName + ", " + lastName
+ " it is great to see you again.";
} else {
return "Username or password incorrect, please try again.";
}
}
}
