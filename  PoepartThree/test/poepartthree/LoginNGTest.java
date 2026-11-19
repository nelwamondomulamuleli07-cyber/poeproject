 package poepartthree;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG unit tests for the Login class.
 */
public class LoginNGTest {

    private Login login;

    @BeforeMethod
    public void setUp() {
        login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    @Test
    public void loginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void loginFailedWrongPassword() {
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
    }

    @Test
    public void loginFailedWrongUsername() {
        assertFalse(login.loginUser("abc_9", "Ch&&sec@ke99!"));
    }

    @Test
    public void loginFailedNullInput() {
        assertFalse(login.loginUser(null, null));
    }

    @Test
    public void loginFailedWhenNobodyRegistered() {
        assertFalse(new Login().loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void returnLoginStatusSuccess() {
        assertEquals(login.returnLoginStatus(true),
                "Welcome Kyle, Smith it is great to see you again.");
    }

    @Test
    public void returnLoginStatusFailure() {
        assertEquals(login.returnLoginStatus(false),
                "Username or password incorrect, please try again.");
    }
}