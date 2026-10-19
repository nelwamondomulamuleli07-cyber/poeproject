 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepartone;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Handles creating, sending, storing and displaying messages (Part 2).
 *
 * @author Student
 */
public class Message {

    // Parallel lists: the same index in every list belongs to the same message
    ArrayList<String> messageIds = new ArrayList<>();
    ArrayList<String> recipientNumber = new ArrayList<>();
    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> msgHash = new ArrayList<>();
    ArrayList<String> messageStatus = new ArrayList<>();

    Scanner input = new Scanner(System.in);
    Random random = new Random();

    // Number of messages sent so far; it increases as more messages are sent
    int numMessageSent = 0;

    // Outcome of the last SentMessage() choice: Sent, Stored, Disregarded or Deleted
    String currentStatus = "";

    // Lets the user enter the chosen number of messages, one at a time
    public void sendingMessages() {

        System.out.println("How many messages do you want to send?");
        int numberOfMessages = input.nextInt();

        input.nextLine();

        // The loop counter (i) is the message number used in the message hash
        for (int i = 1; i <= numberOfMessages; i++) {

            System.out.println("MESSAGE: " + i);

            System.out.println("Enter recipient  cell number!!");
            String recipient = input.nextLine();

            String recipientResult = checkRecipientCell(recipient);

            if (!recipientResult.equals("Cell phone number successfully captured.")) {
                System.out.println(recipientResult);
                i = i - 1;
                continue;
            }

            System.out.println("Enter your message!!");
            String messageText = input.nextLine();

            if (messageText.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
                i = i - 1;
                continue;
            }

            if (messageText.trim().isEmpty()) {
                System.out.println("Your message length should be between 1 to 250");
                i = i - 1;
                continue;
            }

            System.out.println("Message sent");

            // Everything is valid, so the message details can now be recorded
            String messageID = generateMessageID();
            System.out.println("Message ID generated: " + messageID);

            recipientNumber.add(recipient);
            messages.add(messageText);
            msgHash.add(createMessageHash(messageID, i, messageText));

            // The user chooses to send, disregard or store the message
            SentMessage();

            if (currentStatus.equals("Deleted")) {
                // Remove every detail of this message so the lists stay aligned
                int lastIndex = messages.size() - 1;
                messageIds.remove(lastIndex);
                recipientNumber.remove(lastIndex);
                messages.remove(lastIndex);
                msgHash.remove(lastIndex);
                continue;
            }

            messageStatus.add(currentStatus);

            if (currentStatus.equals("Sent")) {
                numMessageSent++;
            }

            if (currentStatus.equals("Stored")) {
                storeMessage();
            }

        }

        printMessages();

        System.out.println("Total messages sent:" + returnTotalMessages());

    }

    // Returns the total number of messages sent
    public int returnTotalMessages() {

        return numMessageSent;
    }

    // Ensures the message ID is not more than ten characters
    public boolean checkMessageID(String messageID) {
        return messageID.length() <= 10;
    }

    // Checks the message is not longer than 250 characters
    public String checkMessageLength(String messageText) {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int extra = messageText.length() - 250;
        return "Message exceeds 250 characters by " + extra
                + "; please reduce the size.";
    }

    // Creates the hash: first two ID digits : message number : first and last word
    public String createMessageHash(String messageID,
            int messageNumber,
            String messageText) {

        String firstTwoNumbers
                = messageID.substring(0, 2);
        String trimmedMessage = messageText.trim();
        String[] words = trimmedMessage.split("\\s+");
        // Punctuation is removed so "tonight?" becomes "tonight"
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        String hash = firstTwoNumbers
                + ":"
                + messageNumber
                + ":"
                + firstWord
                + lastWord;
        return hash.toUpperCase();
    }

    // Generates a random, unique ten-digit message ID
    public String generateMessageID() {

        String messageID;

        do {
            long number = 1000000000L + random.nextLong(9000000000L);

            messageID = String.valueOf(number);

        } while (messageIds.contains(messageID) || !checkMessageID(messageID));

        messageIds.add(messageID);

        return messageID;
    }

    // Checks the recipient number is a valid South African number
    public String checkRecipientCell(String cellphone) {
        if (cellphone.matches("^\\+27[0-9]{9}$")
                || cellphone.matches("^0[0-9]{9}$")) {

            return "Cell phone number successfully captured.";

        } else {

            return "Cell phone number is incorrectly formatted or does not contain "
                    + "an international code. Please correct the number and try again.";
        }
    }

    // Lets the user choose to send, disregard or store the message
    public String SentMessage() {

        System.out.println("\nChoose what you what do with this message");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");

        System.out.println("Enter your choice");
        int choice = input.nextInt();

        // Clears the leftover new line so the next nextLine() works
        input.nextLine();

        switch (choice) {

            case 1 -> {
                System.out.println("Message successfully sent.");
                currentStatus = "Sent";
                return "Message successfully sent.";
            }

            case 2 -> {
                System.out.println("Press 0 to delete the message.");
                int deleteChoice = input.nextInt();

                input.nextLine();

                if (deleteChoice == 0) {
                    System.out.println("Message deleted");
                    currentStatus = "Deleted";
                } else {
                    System.out.println("Invalid option!");
                    currentStatus = "Disregarded";
                }
                return "Press 0 to delete the message.";
            }

            case 3 -> {
                System.out.println("Message successfully stored.");
                currentStatus = "Stored";
                return "Message successfully stored.";
            }

            default -> {
                System.out.println("Invalid option");
                currentStatus = "Disregarded";
                return "Invalid option";
            }
        }

    }

    // Returns (and displays) all the messages sent while the program is running
    public String printMessages() {
        StringBuilder allMessages = new StringBuilder();

        for (int i = 0; i < messages.size(); i++) {
            if (messageStatus.get(i).equals("Sent")) {
                allMessages.append("\nMessage ID: ").append(messageIds.get(i));
                allMessages.append("\nMessage Hash: ").append(msgHash.get(i));
                allMessages.append("\nRecipient: ").append(recipientNumber.get(i));
                allMessages.append("\nMessage: ").append(messages.get(i));
                allMessages.append("\n");
            }
        }

        System.out.println("\n=====All Messages=====");
        System.out.println(allMessages);

        return allMessages.toString();
    }

    // Stores every message flagged "Stored" in a JSON file (messages.json).
    // References:
    // JSON format: JSON.org (n.d.) Introducing JSON. Available at:
    //   https://www.json.org/json-en.html
    // Writing to a file: Oracle (n.d.) FileWriter (Java SE API). Available at:
    //   https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/io/FileWriter.html
    public void storeMessage() {
        StringBuilder json = new StringBuilder("[\n");
        boolean first = true;

        for (int i = 0; i < messages.size(); i++) {
            if (messageStatus.get(i).equals("Stored")) {
                if (!first) {
                    json.append(",\n");
                }
                first = false;
                json.append("  {\n")
                        .append("    \"messageID\": \"").append(jsonEscape(messageIds.get(i))).append("\",\n")
                        .append("    \"messageHash\": \"").append(jsonEscape(msgHash.get(i))).append("\",\n")
                        .append("    \"recipient\": \"").append(jsonEscape(recipientNumber.get(i))).append("\",\n")
                        .append("    \"message\": \"").append(jsonEscape(messages.get(i))).append("\"\n")
                        .append("  }");
            }
        }
        json.append("\n]");

        try (FileWriter writer = new FileWriter("messages.json")) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Could not store the message: " + e.getMessage());
        }
    }

    // Escapes characters that would break a JSON string
    private String jsonEscape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

}//end of class