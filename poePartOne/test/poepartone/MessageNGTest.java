 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepartone;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Unit tests for the Message class (Part 2).
 *
 * @author Student
 */
public class MessageNGTest {

    Message msg = new Message();

    // Creates a Message that reads its "user input" from the given text
    private Message messageWithInput(String text) {
        System.setIn(new ByteArrayInputStream(text.getBytes()));
        return new Message();
    }

    @Test
    public void messageLengthSuccess() {
        // TestNG order is assertEquals(actual, expected)
        assertEquals(msg.checkMessageLength("Hi Mike, can you join us for dinner tonight?"),
                "Message ready to send.");
    }

    @Test
    public void messageLengthFailure() {
        String longMessage = "a".repeat(260);
        assertEquals(msg.checkMessageLength(longMessage),
                "Message exceeds 250 characters by 10; please reduce the size.");
    }

    @Test
    public void recipientCorrectlyFormatted() {
        assertEquals(msg.checkRecipientCell("+27718693002"),
                "Cell phone number successfully captured.");
    }

    @Test
    public void recipientIncorrectlyFormatted() {
        assertEquals(msg.checkRecipientCell("08966553"),
                "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.");
    }

    @Test
    public void messageHashCorrect() {
        assertEquals(msg.createMessageHash("0012345678", 0,
                "Hi Mike, can you join us for dinner tonight?"),
                "00:0:HITONIGHT");
    }

    @Test
    public void messageHashesInLoop() {
        String[] ids = {"0012345678", "0898765432"};
        String[] texts = {"Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"};
        String[] expected = {"00:0:HITONIGHT", "08:1:HIPAYMENT"};

        for (int i = 0; i < texts.length; i++) {
            assertEquals(msg.createMessageHash(ids[i], i, texts[i]), expected[i]);
        }
    }

    @Test
    public void messageIdCreated() {
        String id = msg.generateMessageID();
        System.out.println("Message ID generated: " + id);
        assertTrue(msg.checkMessageID(id));
        assertEquals(id.length(), 10);
    }

    @Test
    public void messageIdTooLong() {
        assertFalse(msg.checkMessageID("12345678901"));
    }

    @Test
    public void sentMessageSend() {
        assertEquals(messageWithInput("1\n").SentMessage(),
                "Message successfully sent.");
    }

    @Test
    public void sentMessageDisregard() {
        assertEquals(messageWithInput("2\n0\n").SentMessage(),
                "Press 0 to delete the message.");
    }

    @Test
    public void sentMessageStore() {
        assertEquals(messageWithInput("3\n").SentMessage(),
                "Message successfully stored.");
    }

    @Test
    public void totalMessagesStartsAtZero() {
        assertEquals(msg.returnTotalMessages(), 0);
    }

    // Test data: message 1 is sent, message 2 is disregarded
    @Test
    public void totalMessagesAfterSendAndDisregard() {
        Message m = messageWithInput("2\n"
                + "+27718693002\nHi Mike, can you join us for dinner tonight?\n1\n"
                + "0857597588\nHi Keegan, did you receive the payment?\n2\n0\n");
        m.sendingMessages();

        assertEquals(m.returnTotalMessages(), 1);
        assertTrue(m.msgHash.get(0).endsWith(":1:HITONIGHT"));
    }

    // A stored message must be written to messages.json
    @Test
    public void storedMessageWrittenToJson() throws IOException {
        Message m = new Message();
        m.messageIds.add("0012345678");
        m.msgHash.add("00:1:HITONIGHT");
        m.recipientNumber.add("+27718693002");
        m.messages.add("Hi Mike, can you join us for dinner tonight?");
        m.messageStatus.add("Stored");

        m.storeMessage();

        String json = Files.readString(Paths.get("messages.json"));
        assertTrue(json.contains("\"messageHash\": \"00:1:HITONIGHT\""));
        assertTrue(json.contains("\"recipient\": \"+27718693002\""));
    }
}