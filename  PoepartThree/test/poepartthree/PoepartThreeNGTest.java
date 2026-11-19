 package poepartthree;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import org.testng.annotations.Test;

/**
 * TestNG tests for the flow used by PoepartThree.main().
 * main() itself only opens JOptionPane dialogs, which cannot run in an automated
 * test, so these tests check the same register -> login -> send flow without dialogs.
 */
public class PoepartThreeNGTest {

    @Test
    public void registerLoginAndSendFlow() throws IOException {
        Login register = new Login();
        Message msg = new Message();
        msg.jsonFile = Files.createTempDirectory("quickchat").resolve("storedMessages.json").toString();

        // register
        register.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(register.registerStatus("kyl_1", "Ch&&sec@ke99!", "+27838968976"));

        // login
        boolean status = register.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(status);
        assertEquals(register.returnLoginStatus(status),
                "Welcome Kyle, Smith it is great to see you again.");

        // send one message, store one (as the main menu options 1 and 4 would)
        msg.addMessage("+27718693002", "Hi Mike, can you join us for dinner tonight?", "Kyle", "Sent");
        msg.addMessage("+27838884567", "Ok, I am leaving without you.", "Kyle", "Stored");
        assertEquals(msg.returnTotalMessages(), 1);
        assertEquals(msg.storedMessages.size(), 1);
    }

    @Test
    public void loginBlocksMessagingWhenDetailsWrong() {
        Login register = new Login();
        register.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(register.loginUser("kyl_1", "wrong"));
        assertEquals(register.returnLoginStatus(false),
                "Username or password incorrect, please try again.");
    }

    @Test
    public void mainMethodExists() throws NoSuchMethodException {
        assertNotNull(PoepartThree.class.getMethod("main", String[].class));
    }
}