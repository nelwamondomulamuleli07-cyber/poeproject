 package poepartthree;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG unit tests for the Registration class (Part 1 test data).
 */
public class RegistrationNGTest {

    private Registration reg;

    private static final String USER_BAD = "Username is not correctly formatted; please ensure that your "
            + "username contains an underscore and is no more than five characters in length.";
    private static final String PASS_BAD = "Password is not correctly formatted; please ensure that the "
            + "password contains at least eight characters, a capital letter, a number, and a special character.";
    private static final String CELL_BAD = "Cell phone number incorrectly formatted or does not contain "
            + "international code; please enter it in the format +27XXXXXXXXX.";

    @BeforeMethod
    public void setUp() {
        reg = new Registration();
    }

    // ---------- assertTrue / assertFalse ----------
    @Test
    public void usernameCorrectlyFormatted() {
        assertTrue(reg.checkUserName("kyl_1"));
    }

    @Test
    public void usernameIncorrectlyFormatted() {
        assertFalse(reg.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void passwordMeetsComplexity() {
        assertTrue(reg.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void passwordDoesNotMeetComplexity() {
        assertFalse(reg.checkPasswordComplexity("password"));
    }

    @Test
    public void cellPhoneCorrectlyFormatted() {
        assertTrue(reg.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void cellPhoneIncorrectlyFormatted() {
        assertFalse(reg.checkCellPhoneNumber("08966553"));
    }

    // ---------- assertEquals (registerUser messages) ----------
    @Test
    public void registerUserSuccess() {
        assertEquals(reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976"),
                "Username successfully captured.\nPassword successfully captured."
                + "\nCell phone number successfully added.");
        assertTrue(reg.registerStatus("kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void registerUserUsernameFails() {
        assertEquals(reg.registerUser("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"),
                USER_BAD);
    }

    @Test
    public void registerUserPasswordFails() {
        assertEquals(reg.registerUser("Kyle", "Smith", "kyl_1", "password", "+27838968976"),
                PASS_BAD);
    }

    @Test
    public void registerUserCellFails() {
        assertEquals(reg.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553"),
                CELL_BAD);
        assertFalse(reg.registerStatus("kyl_1", "Ch&&sec@ke99!", "08966553"));
    }

    @Test
    public void registerUserDoesNotStoreDetailsOnFailure() {
        reg.registerUser("Kyle", "Smith", "kyle!!!!!!!", "password", "08966553");
        assertNull(reg.userName);
    }
}