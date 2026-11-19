 package poepartthree;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 * Handles sending, storing, disregarding, searching and reporting on messages.
 * @author Student
 */
public class Message {

    // Sent messages (parallel lists - same index = same message)
    ArrayList<String> sentMessages = new ArrayList<>();
    ArrayList<String> messageIdsList = new ArrayList<>();
    HashSet<String> messageIds = new HashSet<>();
    ArrayList<String> msgHash = new ArrayList<>();
    ArrayList<String> recipientNumber = new ArrayList<>();
    ArrayList<String> messageStatus = new ArrayList<>();
    ArrayList<String> senders = new ArrayList<>();

    // Disregarded messages
    ArrayList<String> disregardedMessages = new ArrayList<>();

    // Stored messages (parallel lists - loaded from / saved to the JSON file)
    ArrayList<String> storedMessages = new ArrayList<>();
    ArrayList<String> storedIds = new ArrayList<>();
    ArrayList<String> storedHashes = new ArrayList<>();
    ArrayList<String> storedRecipients = new ArrayList<>();
    ArrayList<String> storedSenders = new ArrayList<>();

    Random random = new Random();

    int numMessageSent = 0;
    int messageCounter = 0;                 // message number used in the hash (starts at 0)
    String jsonFile = "storedMessages.json";

    private static final String CELL_OK = "Cell phone number successfully captured.";
    private static final String CELL_BAD = "Cell phone number is incorrectly formatted or does "
            + "not contain an international code. Please correct the number and try again.";

    // ------------------------------------------------------------------
    // SENDING
    // ------------------------------------------------------------------
    public void sendingMessages() {

        int numberOfMessages = 0;

        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "How many messages do you want to send?",
                    "Send Message",
                    JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                return;
            }

            try {
                numberOfMessages = Integer.parseInt(input.trim());
                if (numberOfMessages < 1) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a number greater than 0",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid number",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        for (int i = 1; i <= numberOfMessages; i++) {

            String recipient;
            while (true) {
                recipient = JOptionPane.showInputDialog(null,
                        "Message " + i + " of " + numberOfMessages
                        + "\n\nEnter recipient cell number:",
                        "Recipient", JOptionPane.QUESTION_MESSAGE);

                if (recipient == null) {
                    return;
                }

                String recipientResult = checkRecipientCell(recipient.trim());

                if (recipientResult.equals(CELL_OK)) {
                    recipient = recipient.trim();
                    break;
                }
                JOptionPane.showMessageDialog(null, recipientResult, "Invalid number",
                        JOptionPane.ERROR_MESSAGE);
            }

            String messageText;
            while (true) {
                messageText = JOptionPane.showInputDialog(null,
                        "Enter your message (1 to 250 characters long):",
                        "Message", JOptionPane.QUESTION_MESSAGE);

                if (messageText == null) {
                    return;
                }

                if (messageText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a message of 1 to 250 characters.",
                            "Invalid message", JOptionPane.ERROR_MESSAGE);
                } else if (messageText.length() > 250) {
                    JOptionPane.showMessageDialog(null, checkMessageLength(messageText),
                            "Invalid message", JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            }

            String sender = JOptionPane.showInputDialog(null,
                    "Enter sender name", "Sender",
                    JOptionPane.QUESTION_MESSAGE);

            if (sender == null || sender.trim().isEmpty()) {
                sender = "unknown";
            }
            sender = sender.trim();

            int choice = chooseSendOption();
            String response = SentMessage(choice);
            String flag = null;

            if (choice == 0) {
                flag = "Sent";
            } else if (choice == 2) {
                flag = "Stored";
            } else {
                // Disregard: "Press 0 to delete the message"
                String del = JOptionPane.showInputDialog(null, response,
                        "Disregard Message", JOptionPane.QUESTION_MESSAGE);
                if (del != null && del.trim().equals("0")) {
                    flag = "Disregard";
                    JOptionPane.showMessageDialog(null, "Message deleted",
                            "Deleted", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Message was not deleted and was not sent.");
                }
            }

            if (flag != null) {
                addMessage(recipient, messageText, sender, flag);
            }

            if (choice == 0) {
                int last = sentMessages.size() - 1;
                // Order required: Message ID, Message Hash, Recipient, Message
                JOptionPane.showMessageDialog(null,
                        response
                        + "\n\nMessage ID: " + messageIdsList.get(last)
                        + "\nMessage Hash: " + msgHash.get(last)
                        + "\nRecipient: " + recipientNumber.get(last)
                        + "\nMessage: " + sentMessages.get(last),
                        "Sent", JOptionPane.INFORMATION_MESSAGE);
            } else if (choice == 2) {
                JOptionPane.showMessageDialog(null, response);
            }
        }

        if (!sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, printMessages(),
                    "Sent messages", JOptionPane.INFORMATION_MESSAGE);
        }

        JOptionPane.showMessageDialog(null,
                "Total messages sent: " + returnTotalMessages(),
                "Total", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Creates a message (ID + hash) and puts it in the correct arrays.
     * flag: "Sent", "Stored" or "Disregard". No dialogs, so it can be unit tested.
     * Returns the generated message hash.
     */
    public String addMessage(String recipient, String messageText, String sender, String flag) {
        String id = generateMessageID();
        String hash = createMessageHash(id, messageCounter, messageText);
        messageCounter++;
        messageIds.add(id);

        if (flag.equalsIgnoreCase("Sent")) {
            sentMessages.add(messageText);
            messageIdsList.add(id);
            msgHash.add(hash);
            recipientNumber.add(recipient);
            senders.add(sender);
            messageStatus.add("Sent");
            numMessageSent++;
        } else if (flag.equalsIgnoreCase("Stored")) {
            storedMessages.add(messageText);
            storedIds.add(id);
            storedHashes.add(hash);
            storedRecipients.add(recipient);
            storedSenders.add(sender);
            storeMessage();
        } else {
            disregardedMessages.add(messageText);
            messageIds.remove(id);
        }
        return hash;
    }

    // Shows Send / Disregard / Store buttons and returns 0, 1 or 2
    public int chooseSendOption() {
        Object[] options = {"Send", "Disregard", "Store"};
        int choice = JOptionPane.showOptionDialog(null,
                "Choose what you want to do with this message!",
                "Message Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return choice < 0 ? 1 : choice;   // closing the dialog = disregard
    }

    // Returns the required response for each option (0 = send, 1 = disregard, 2 = store)
    public String SentMessage(int choice) {
        return switch (choice) {
            case 0 -> "Message successfully sent.";
            case 1 -> "Press 0 to delete the message.";
            case 2 -> "Message successfully stored.";
            default -> "Invalid option.";
        };
    }

    public int returnTotalMessages() {
        return numMessageSent;
    }

    // ------------------------------------------------------------------
    // VALIDATION AND HASH
    // ------------------------------------------------------------------
    public boolean checkMessageID(String messageID) {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkMessageLength(String messageText) {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (messageText.length() - 250)
                + "; please reduce the size.";
    }

    public String checkRecipientCell(String cellphone) {
        if (cellphone.matches("^\\+27[0-9]{9}$") || cellphone.matches("^0[0-9]{9}$")) {
            return CELL_OK;
        } else {
            return CELL_BAD;
        }
    }

    // First two ID digits : message number : first word + last word (letters/digits only), upper case
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String firstTwoNumbers = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String hash = firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    public String generateMessageID() {
        String messageID;
        do {
            long number = 1000000000L + random.nextLong(9000000000L);
            messageID = String.valueOf(number);
        } while (messageIds.contains(messageID));
        return messageID;
    }

    // ------------------------------------------------------------------
    // JSON STORAGE
    // ------------------------------------------------------------------
    // Reference: [add the source you used for writing/reading JSON here]
    public String storeMessage() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append("{\"messageID\":\"").append(escape(storedIds.get(i)))
                    .append("\",\"messageHash\":\"").append(escape(storedHashes.get(i)))
                    .append("\",\"recipient\":\"").append(escape(storedRecipients.get(i)))
                    .append("\",\"sender\":\"").append(escape(storedSenders.get(i)))
                    .append("\",\"message\":\"").append(escape(storedMessages.get(i)))
                    .append("\"}").append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        sb.append("]");
        try {
            Files.writeString(Paths.get(jsonFile), sb.toString(), StandardCharsets.UTF_8);
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Could not write JSON file: " + e.getMessage();
        }
    }

    // Reads the JSON file into the stored arrays; returns how many messages were loaded
    public int loadStoredMessages() {
        storedMessages.clear();
        storedIds.clear();
        storedHashes.clear();
        storedRecipients.clear();
        storedSenders.clear();

        Path path = Paths.get(jsonFile);
        if (!Files.exists(path)) {
            return 0;
        }
        String str = "\"((?:[^\"\\\\]|\\\\.)*)\"";
        Pattern pattern = Pattern.compile("\"messageID\":" + str + ",\"messageHash\":" + str
                + ",\"recipient\":" + str + ",\"sender\":" + str + ",\"message\":" + str);
        try {
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                Matcher m = pattern.matcher(line);
                if (m.find()) {
                    storedIds.add(unescape(m.group(1)));
                    storedHashes.add(unescape(m.group(2)));
                    storedRecipients.add(unescape(m.group(3)));
                    storedSenders.add(unescape(m.group(4)));
                    storedMessages.add(unescape(m.group(5)));
                    messageIds.add(unescape(m.group(1)));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not read JSON file: " + e.getMessage());
        }
        return storedMessages.size();
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    private String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                if (n == 'n') {
                    sb.append('\n');
                } else if (n == 'r') {
                    sb.append('\r');
                } else {
                    sb.append(n);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // PRINT / SEARCH / DELETE / REPORT (all return text; menus show it in dialogs)
    // ------------------------------------------------------------------
    public String printMessages() {
        StringBuilder allMessages = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            allMessages.append("\nMessage Number: ").append(i + 1);
            allMessages.append("\nMessage ID: ").append(messageIdsList.get(i));
            allMessages.append("\nMessage Hash: ").append(msgHash.get(i));
            allMessages.append("\nRecipient: ").append(recipientNumber.get(i));
            allMessages.append("\nMessage: ").append(sentMessages.get(i));
            allMessages.append("\nStatus: ").append(messageStatus.get(i)).append("\n");
        }
        return allMessages.toString();
    }

    private String longestOf(List<String> list) {
        String longest = "";
        for (String m : list) {
            if (m.length() > longest.length()) {
                longest = m;
            }
        }
        return longest;
    }

    // Longest message across sent and stored messages ("" if there are none)
    public String findLongestMessage() {
        List<String> all = new ArrayList<>(sentMessages);
        all.addAll(storedMessages);
        return longestOf(all);
    }

    public String findLongestStoredMessage() {
        return longestOf(storedMessages);
    }

    public String showDisregardedMessages() {
        if (disregardedMessages.isEmpty()) {
            return "No deleted message yet.";
        }
        StringBuilder sb = new StringBuilder("========DELETED MESSAGES========\n");
        for (int i = 0; i < disregardedMessages.size(); i++) {
            sb.append(i + 1).append(". ").append(disregardedMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String showAllStoredMessages() {
        if (storedMessages.isEmpty()) {
            return "No stored messages to display";
        }
        StringBuilder sb = new StringBuilder("========All stored messages========\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append(i + 1).append(". ").append(storedMessages.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            return "No stored messages to display";
        }
        StringBuilder sb = new StringBuilder("========Stored messages: sender and recipient========\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append(i + 1).append(". Sender: ").append(storedSenders.get(i))
                    .append(" | Recipient: ").append(storedRecipients.get(i)).append("\n");
        }
        return sb.toString();
    }

    public String searchMessageByID(String messageId) {
        int index = messageIdsList.indexOf(messageId);
        if (index != -1) {
            return "Recipient: " + recipientNumber.get(index)
                    + "\nMessage: " + sentMessages.get(index);
        }
        index = storedIds.indexOf(messageId);
        if (index != -1) {
            return "Recipient: " + storedRecipients.get(index)
                    + "\nMessage: " + storedMessages.get(index);
        }
        return "No message exists with this Message ID: " + messageId;
    }

    // All sent and stored messages for one recipient, one per line
    public String searchMessageByRecipient(String recipient) {
        List<String> found = new ArrayList<>();
        for (int i = 0; i < sentMessages.size(); i++) {
            if (recipientNumber.get(i).equals(recipient)) {
                found.add(sentMessages.get(i));
            }
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedRecipients.get(i).equals(recipient)) {
                found.add(storedMessages.get(i));
            }
        }
        if (found.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }
        return String.join("\n", found);
    }

    public String searchMessageByHashID(String hashId) {
        if (hashId == null || hashId.trim().isEmpty()) {
            return "Hash ID cannot be empty";
        }
        hashId = hashId.trim();
        int index = msgHash.indexOf(hashId);
        if (index != -1) {
            return "========Message Details========"
                    + "\nHash ID: " + msgHash.get(index)
                    + "\nMessage ID: " + messageIdsList.get(index)
                    + "\nFrom: " + senders.get(index)
                    + "\nTo: " + recipientNumber.get(index)
                    + "\nMessage: " + sentMessages.get(index)
                    + "\nLength: " + sentMessages.get(index).length() + " characters long";
        }
        index = storedHashes.indexOf(hashId);
        if (index != -1) {
            return "========Message Details========"
                    + "\nHash ID: " + storedHashes.get(index)
                    + "\nMessage ID: " + storedIds.get(index)
                    + "\nFrom: " + storedSenders.get(index)
                    + "\nTo: " + storedRecipients.get(index)
                    + "\nMessage: " + storedMessages.get(index)
                    + "\nLength: " + storedMessages.get(index).length() + " characters long";
        }
        return "No message exists with this Hash ID: " + hashId;
    }

    // Deletes a sent or stored message by hash and records it as disregarded
    public String deleteMessageByHash(String hash) {
        if (hash == null) {
            return "Message not found";
        }
        hash = hash.trim();
        int index = msgHash.indexOf(hash);
        if (index != -1) {
            String text = sentMessages.get(index);
            disregardedMessages.add(text);
            messageIds.remove(messageIdsList.get(index));
            sentMessages.remove(index);
            senders.remove(index);
            recipientNumber.remove(index);
            messageIdsList.remove(index);
            msgHash.remove(index);
            messageStatus.remove(index);
            return "Message: \"" + text + "\" successfully deleted.";
        }
        index = storedHashes.indexOf(hash);
        if (index != -1) {
            String text = storedMessages.get(index);
            disregardedMessages.add(text);
            messageIds.remove(storedIds.get(index));
            storedMessages.remove(index);
            storedIds.remove(index);
            storedHashes.remove(index);
            storedRecipients.remove(index);
            storedSenders.remove(index);
            storeMessage();   // keep the JSON file up to date
            return "Message: \"" + text + "\" successfully deleted.";
        }
        return "Message not found";
    }

    // Report of all sent messages
    public String displayFullReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages to display.";
        }
        StringBuilder sb = new StringBuilder("========Full Message Report========\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            sb.append("\n").append(i + 1).append(".")
                    .append(" Message Hash: ").append(msgHash.get(i))
                    .append("\n    Message ID: ").append(messageIdsList.get(i))
                    .append("\n    Sender: ").append(senders.get(i))
                    .append("\n    Recipient: ").append(recipientNumber.get(i))
                    .append("\n    Message: ").append(sentMessages.get(i)).append("\n");
        }
        sb.append("\nTotal sent messages: ").append(sentMessages.size());
        sb.append("\nTotal disregarded messages: ").append(disregardedMessages.size());
        sb.append("\nTotal stored messages: ").append(storedMessages.size());
        return sb.toString();
    }

    // Report of all stored messages (full details)
    public String displayStoredReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages to display";
        }
        StringBuilder sb = new StringBuilder("========Stored Messages Report========\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            sb.append("\n").append(i + 1).append(".")
                    .append(" Message ID: ").append(storedIds.get(i))
                    .append("\n    Message Hash: ").append(storedHashes.get(i))
                    .append("\n    Sender: ").append(storedSenders.get(i))
                    .append("\n    Recipient: ").append(storedRecipients.get(i))
                    .append("\n    Message: ").append(storedMessages.get(i)).append("\n");
        }
        sb.append("\nTotal stored messages: ").append(storedMessages.size());
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // MENUS
    // ------------------------------------------------------------------
    private void show(String text) {
        JOptionPane.showMessageDialog(null, text, "QuickChat", JOptionPane.INFORMATION_MESSAGE);
    }

    private String ask(String text) {
        return JOptionPane.showInputDialog(null, text, "QuickChat", JOptionPane.QUESTION_MESSAGE);
    }

    // Main menu option 2
    public void messageManagementMenu() {
        while (true) {
            String option = ask("1. Show disregarded messages\n"
                    + "2. Show all stored messages\n"
                    + "3. Delete a message by Hash ID\n"
                    + "4. Display full report\n"
                    + "5. Search for a message by Hash ID\n"
                    + "6. Display the longest message\n"
                    + "7. Back to menu\n\nChoose an option (1-7):");
            if (option == null) {
                return;
            }
            switch (option.trim()) {
                case "1" -> show(showDisregardedMessages());
                case "2" -> show(showAllStoredMessages());
                case "3" -> {
                    String hash = ask("Enter Hash ID to delete:");
                    if (hash != null) {
                        show(deleteMessageByHash(hash));
                    }
                }
                case "4" -> show(displayFullReport());
                case "5" -> {
                    String hash = ask("Enter the Hash ID to search:");
                    if (hash != null) {
                        show(searchMessageByHashID(hash));
                    }
                }
                case "6" -> {
                    String longest = findLongestMessage();
                    show(longest.isEmpty() ? "No messages to analyze."
                            : "====== LONGEST MESSAGE ======\nLength: " + longest.length()
                            + " characters\nMessage: " + longest);
                }
                case "7" -> {
                    return;
                }
                default -> show("Invalid choice.");
            }
        }
    }

    // Main menu option 4
    public void storedMessagesMenu() {
        while (true) {
            String option = ask("=====STORED MESSAGES=====\n"
                    + "1. Display sender and recipient of all stored messages\n"
                    + "2. Display the longest stored message\n"
                    + "3. Search for a message ID\n"
                    + "4. Search messages for a recipient\n"
                    + "5. Delete a message using the message hash\n"
                    + "6. Display report of all stored messages\n"
                    + "7. Back to menu\n\nChoose an option (1-7):");
            if (option == null) {
                return;
            }
            switch (option.trim()) {
                case "1" -> show(displayStoredSendersAndRecipients());
                case "2" -> {
                    String longest = findLongestStoredMessage();
                    show(longest.isEmpty() ? "No stored messages to analyze."
                            : "====== LONGEST STORED MESSAGE ======\nLength: " + longest.length()
                            + " characters\nMessage: " + longest);
                }
                case "3" -> {
                    String id = ask("Enter the Message ID to search:");
                    if (id != null) {
                        show(searchMessageByID(id.trim()));
                    }
                }
                case "4" -> {
                    String recipient = ask("Enter the recipient number:");
                    if (recipient != null) {
                        show(searchMessageByRecipient(recipient.trim()));
                    }
                }
                case "5" -> {
                    String hash = ask("Enter the Message Hash to delete:");
                    if (hash != null) {
                        show(deleteMessageByHash(hash));
                    }
                }
                case "6" -> show(displayStoredReport());
                case "7" -> {
                    return;
                }
                default -> show("Invalid choice.");
            }
        }
    }
}