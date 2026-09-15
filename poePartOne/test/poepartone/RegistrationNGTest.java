 /*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
*/
package poepartone;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
*
* @author Student
*/
public class RegistrationNGTest {

public RegistrationNGTest() {
}

@BeforeClass
public static void setUpClass() throws Exception {
}

@AfterClass
public static void tearDownClass() throws Exception {
}

@BeforeMethod
public void setUpMethod() throws Exception {
}

@AfterMethod
public void tearDownMethod() throws Exception {
}

// =========================================================
// checkUserName() Tests
// =========================================================

/**
* Test of checkUserName method, of class Registration.
* Valid case: username contains an underscore and is no more than
* five characters long.
*/
@Test
public void testCheckUserName_Valid() {
System.out.println("checkUserName - valid");
String username = "kyl_1";
Registration instance = new Registration();
boolean expResult = true;
boolean result = instance.checkUserName(username);
assertEquals(result, expResult);
}

/**
* Test of checkUserName method, of class Registration.
* Invalid case: username does not contain an underscore and/or
* exceeds five characters.
*/
@Test
public void testCheckUserName_Invalid() {
System.out.println("checkUserName - invalid");
String username = "kyle!!!!!!!";
Registration instance = new Registration();
boolean expResult = false;
boolean result = instance.checkUserName(username);
assertEquals(result, expResult);
}

// =========================================================
// checkPasswordComplexity() Tests
// =========================================================

/**
* Test of checkPasswordComplexity method, of class Registration.
* Valid case: password meets all complexity requirements.
*/
@Test
public void testCheckPasswordComplexity_Valid() {
System.out.println("checkPasswordComplexity - valid");
String password = "Ch&&sec@ke99!";
Registration instance = new Registration();
boolean expResult = true;
boolean result = instance.checkPasswordComplexity(password);
assertEquals(result, expResult);
}

/**
* Test of checkPasswordComplexity method, of class Registration.
* Invalid case: password does not meet complexity requirements
* (no capital letter, number, or special character).
*/
@Test
public void testCheckPasswordComplexity_Invalid() {
System.out.println("checkPasswordComplexity - invalid");
String password = "password";
Registration instance = new Registration();
boolean expResult = false;
boolean result = instance.checkPasswordComplexity(password);
assertEquals(result, expResult);
}

// =========================================================
// checkCellPhoneNumber() Tests
// =========================================================

/**
* Test of checkCellPhoneNumber method, of class Registration.
* Valid case: cell phone number contains international code and
* is correctly formatted.
*/
@Test
public void testCheckCellPhoneNumber_Valid() {
System.out.println("checkCellPhoneNumber - valid");
String cellphone = "+27838968976";
Registration instance = new Registration();
boolean expResult = true;
boolean result = instance.checkCellPhoneNumber(cellphone);
assertEquals(result, expResult);
}

/**
* Test of checkCellPhoneNumber method, of class Registration.
* Invalid case: cell phone number does not contain an
* international country code.
*/
@Test
public void testCheckCellPhoneNumber_Invalid() {
System.out.println("checkCellPhoneNumber - invalid");
String cellphone = "08966553";
Registration instance = new Registration();
boolean expResult = false;
boolean result = instance.checkCellPhoneNumber(cellphone);
assertEquals(result, expResult);
}

// =========================================================
// registerUser() Tests
// =========================================================

/**
* Test of registerUser method, of class Registration.
* Valid case: all fields (username, password, cell phone) pass
* validation, so all success messages are returned.
*/
@Test
public void testRegisterUser_Valid() {
System.out.println("registerUser - valid");
String firstName = "Kyle";
String lastName = "Smith";
String username = "kyl_1";
String password = "Ch&&sec@ke99!";
String cellphone = "+27838968976";
Registration instance = new Registration();
String expResult = "Username successfully captured."
+ "\nPassword successfully captured."
+ "\nCell phone number successfully added.";
String result = instance.registerUser(firstName, lastName, username, password, cellphone);
assertEquals(result, expResult);
}

/**
* Test of registerUser method, of class Registration.
* Invalid case: username, password and cell phone all fail
* validation, so all the corresponding error messages should be
* returned.
*/
@Test
public void testRegisterUser_Invalid() {
System.out.println("registerUser - invalid");
String firstName = "Kyle";
String lastName = "Smith";
String username = "kyle!!!!!!!";
String password = "password";
String cellphone = "08966553";
Registration instance = new Registration();
String expResult = "Username is not correctly formatted; please ensure that your username "
+ "contains an underscore and is no more than five characters in length."
+ "\nPassword is not correctly formatted; please ensure that the password contains "
+ "at least eight characters, a capital letter, a number, and a special character."
+ "\nCell phone number incorrectly formatted or does not contain international code; "
+ "please enter it in the format +27XXXXXXXXX.";
String result = instance.registerUser(firstName, lastName, username, password, cellphone);
assertEquals(result, expResult);
}

// =========================================================
// registerStatus() Tests
// =========================================================

/**
* Test of registerStatus method, of class Registration.
* Valid case: username, password and cell phone all pass
* validation and registration succeeds.
*/
@Test
public void testRegisterStatus_Valid() {
System.out.println("registerStatus - valid");
String username = "kyl_1";
String password = "Ch&&sec@ke99!";
String cellphone = "+27838968976";
Registration instance = new Registration();
boolean expResult = true;
boolean result = instance.registerStatus(username, password, cellphone);
assertEquals(result, expResult);
}

/**
* Test of registerStatus method, of class Registration.
* Invalid case: username, password and cell phone all fail
* validation and registration should not succeed.
*/
@Test
public void testRegisterStatus_Invalid() {
System.out.println("registerStatus - invalid");
String username = "kyle!!!!!!!";
String password = "password";
String cellphone = "08966553";
Registration instance = new Registration();
boolean expResult = false;
boolean result = instance.registerStatus(username, password, cellphone);
assertEquals(result, expResult);
}

// =========================================================
// loginUser() Tests
// NOTE: loginUser() and returnLoginStatus() live on the Login
// class (Login extends Registration), so a Login instance is
// used for these tests instead of Registration directly.
// =========================================================

/**
* Test of loginUser method, of class Login.
* Valid case: entered username and password match the details
* used at registration, so login succeeds.
*/
@Test
public void testLoginUser_Valid() {
System.out.println("loginUser - valid");
Login instance = new Login();
// Register the user first so there is something to log in against.
instance.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
boolean expResult = true;
boolean result = instance.loginUser("kyl_1", "Ch&&sec@ke99!");
assertEquals(result, expResult);
}

/**
* Test of loginUser method, of class Login.
* Invalid case: entered username and/or password do not match
* the details used at registration, so login fails.
*/
@Test
public void testLoginUser_Invalid() {
System.out.println("loginUser - invalid");
Login instance = new Login();
instance.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
boolean expResult = false;
boolean result = instance.loginUser("kyl_1", "WrongPassword1!");
assertEquals(result, expResult);
}

// =========================================================
// returnLoginStatus() Tests
// =========================================================

/**
* Test of returnLoginStatus method, of class Login.
* Valid case: successful login returns the personalised welcome
* message containing the user's first and last name.
*/
@Test
public void testReturnLoginStatus_Valid() {
System.out.println("returnLoginStatus - valid");
Login instance = new Login();
instance.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
boolean loginStatus = instance.loginUser("kyl_1", "Ch&&sec@ke99!");
String expResult = "Welcome Kyle, Smith it is great to see you again.";
String result = instance.returnLoginStatus(loginStatus);
assertEquals(result, expResult);
}

/**
* Test of returnLoginStatus method, of class Login.
* Invalid case: failed login returns the generic error message.
*/
@Test
public void testReturnLoginStatus_Invalid() {
System.out.println("returnLoginStatus - invalid");
Login instance = new Login();
instance.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
boolean loginStatus = instance.loginUser("kyl_1", "WrongPassword1!");
String expResult = "Username or password incorrect, please try again.";
String result = instance.returnLoginStatus(loginStatus);
assertEquals(result, expResult);
}

}
