 package poepartthree;

import static org.testng.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG unit tests for the Message class (Part 2 and Part 3 test data).
 * Note: TestNG's assertEquals takes (actual, expected).
 */
public class MessageNGTest {

    private Message msg;

    @BeforeMethod
    public void setUp() throws IOException {
        msg = new Message();
        // temporary JSON file so the tests never touch the real storedMessages.json
        msg.jsonFile = Files.createTempDirectory("quickchat").resolve("storedMessages.json").toString();
    }

    // Part 3 test data, messages 1-5
    private void loadTestData() {
        msg.addMessage("+27834557896", "Did you get the cake?", "Developer", "Sent");
        msg.addMessage("+27838884567", "Where are you? You are late! I have asked you to be on time.", "Developer", "Stored");
        msg.addMessage("+27834484567", "Yohoooo, I am at your gate.", "Developer", "Disregard");
        msg.addMessage("0838884567", "It is dinner time !", "Developer", "Sent");
        msg.addMessage("+27838884567", "Ok, I am leaving without you.", "Developer", "Stored");
    }

    // ---------------- Part 2 ----------------
    @Test
    public void messageLengthSuccess() {
        assertEquals(msg.checkMessageLength("Hi Mike, can you join us for dinner tonight?"),
                "Message ready to send.");
    }

    @Test
    public void messageLengthFailure() {
        assertEquals(msg.checkMessageLength("a".repeat(260)),
                "Message exceeds 250 characters by 10; please reduce the size.");
    }

    @Test
    public void recipientCorrectlyFormatted() {
        assertEquals(msg.checkRecipientCell("+27718693002"),
                "Cell phone number successfully captured.");
    }

    @Test
    public void recipientIncorrectlyFormatted() {
        assertEquals(msg.checkRecipientCell("08575975889"),
                "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.");
    }

    @Test
    public void messageHashIsCorrect() {
        assertEquals(msg.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight?"),
                "00:0:HITONIGHT");
    }

    @Test
    public void remainingMessageHashesInLoop() {
        String[] messages = {"Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};
        for (int i = 0; i < messages.length; i++) {
            assertEquals(msg.createMessageHash("0012345678", i, messages[i]), expected[i]);
        }
    }

    @Test
    public void messageIdIsCreated() {
        String id = msg.generateMessageID();
        assertEquals(id.length(), 10);
        assertTrue(msg.checkMessageID(id));
        assertFalse(msg.checkMessageID("12345678901"));
    }

    @Test
    public void sentMessageOptions() {
        assertEquals(msg.SentMessage(0), "Message successfully sent.");
        assertEquals(msg.SentMessage(1), "Press 0 to delete the message.");
        assertEquals(msg.SentMessage(2), "Message successfully stored.");
    }

    @Test
    public void totalMessagesSent() {
        msg.addMessage("+27718693002", "Hi Mike, can you join us for dinner tonight?", "Dev", "Sent");
        msg.addMessage("08575975889", "Hi Keegan, did you receive the payment?", "Dev", "Disregard");
        assertEquals(msg.returnTotalMessages(), 1);
    }

    // ---------------- Part 3 ----------------
    @Test
    public void sentMessagesArrayCorrectlyPopulated() {
        loadTestData();
        assertEquals(msg.sentMessages, List.of("Did you get the cake?", "It is dinner time !"));
    }

    @Test
    public void disregardedAndStoredArraysPopulated() {
        loadTestData();
        assertEquals(msg.disregardedMessages, List.of("Yohoooo, I am at your gate."));
        assertEquals(msg.storedMessages.size(), 2);
        assertEquals(msg.messageCounter, 5);
    }

    @Test
    public void displayLongestMessage() {
        loadTestData();
        assertEquals(msg.findLongestMessage(),
                "Where are you? You are late! I have asked you to be on time.");
    }

    @Test
    public void searchForMessageID() {
        loadTestData();
        String result = msg.searchMessageByID(msg.messageIdsList.get(1)); // message 4
        assertTrue(result.contains("0838884567"));
        assertTrue(result.contains("It is dinner time !"));
    }

    @Test
    public void searchMessagesForRecipient() {
        loadTestData();
        assertEquals(msg.searchMessageByRecipient("+27838884567"),
                "Where are you? You are late! I have asked you to be on time.\nOk, I am leaving without you.");
    }

    @Test
    public void deleteMessageUsingHash() {
        loadTestData();
        String hash = msg.storedHashes.get(0); // message 2
        assertEquals(msg.deleteMessageByHash(hash),
                "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.");
        assertEquals(msg.storedMessages.size(), 1);
        assertEquals(msg.deleteMessageByHash("XX:9:NOTHING"), "Message not found");
    }

    @Test
    public void displayReport() {
        loadTestData();
        String report = msg.displayFullReport();
        assertTrue(report.contains(msg.msgHash.get(0)));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
    }

    @Test
    public void jsonStoreAndReadBack() {
        loadTestData();
        Message other = new Message();
        other.jsonFile = msg.jsonFile;
        assertEquals(other.loadStoredMessages(), 2);
        assertEquals(other.storedMessages, msg.storedMessages);
        assertEquals(other.storedHashes, msg.storedHashes);
    }
}