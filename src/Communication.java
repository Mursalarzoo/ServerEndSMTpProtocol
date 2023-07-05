import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Communication {
    private final static boolean DEBUG = true;        // A boolean flag indicating whether debug output should be enabled or not

    // Method responsible for handling the SMTP connection
    static public void handle(final Socket pSocket, final BufferedReader pBR, final BufferedWriter pBW) throws IOException {
        send("220 ruc.dk SMTP", pBW);       // Send the initial SMTP message to the client

        String helo = read(pBR);        // Read the client's HELO command

        if (helo == null || helo.length() == 0) {       // If the HELO command is invalid or empty, send an appropriate response and terminate to connection
            send("221 ruc.dk Service closing transmission channel", pBW);
            return;
        }

        //Extract the command and domain from the HELO command
        String[] heloParts = helo.split(" ");
        String heloCommand = heloParts[0].toUpperCase();
        String heloDomain = heloParts[1];

        //Validate the Helo command and check if the domain is properly formatted
        switch (heloCommand) {
            case "HELO":
                if (!heloDomain.contains(".")) {
                    send("501 Syntax error in parameters or arguments", pBW);
                    return;
                }
                break;
            default:
                send("500 Syntax error, command unrecognized", pBW);
                return;
        }

        send("250 OK", pBW);        // Sending positive response "250 OK" indicating successful handling of the HELO command.

        String sender = read(pBR);           // Read the sender's MAIL FROM command


        if (sender == null || sender.length() == 0) {
            send("221 ruc.dk Service closing transmission channel", pBW);
            return;
        }

        String[] senderParts = sender.split(":");
        String senderCommand = senderParts[0].toUpperCase();
        String senderAddress = senderParts[1].toLowerCase();

        //Validate the MAIL FROM command and check is the sender address is valid
        switch (senderCommand) {
            case "MAIL FROM":
                if (!senderAddress.contains("hans@ruc.dk")) {
                    send("550 No such a user here", pBW);
                    return;
                }
                break;
            default:
                send("500 Syntax error, command unrecognized", pBW);
                return;
        }

        send("250 OK", pBW);

        String recipient = read(pBR);
        if (recipient == null || recipient.length() == 0) {
            send("221 ruc.dk Service closing transmission channel", pBW);
            return;
        }

        String[] recipientParts = recipient.split(":");
        String recipientCommand = recipientParts[0].toUpperCase();
        String recipientAddress = recipientParts[1].toLowerCase();

        switch (recipientCommand) {
            case "RCPT TO":
                if (!recipientAddress.contains("mursal@ruc.dk")) {
                    send("550 No such a user here", pBW);
                    return;
                }
                break;
            default:
                send("500 Syntax error, command unrecognized", pBW);
                return;
        }

        send("250 OK", pBW);

        String data = read(pBR);
        if (data == null || data.length() == 0) {
            send("221 ruc.dk Service closing transmission channel", pBW);
            return;
        }

        String[] dataParts = data.split(" ");
        String dataCommand = dataParts[0].toUpperCase();


        if (!dataCommand.equals("DATA")) {
            send("500 Syntax error, command unrecognized", pBW);
            return;
        }

        send("354 Start <CRLF>.<CRLF>", pBW);

        // Do-while loop to handle the email message
        do {
            System.out.println("I am testing now");
            System.out.println("See you soon");
            System.out.println("Ending the test now");
            System.out.println(".");
        } while(dataCommand.equals("."));

        send("250 OK", pBW);

        String quit = read(pBR);
        if (quit == null || quit.length() == 0) {
            send("221 ruc.dk Service closing transmission channel", pBW);
            return;
        }

        String[] quitParts = quit.split(" ");
        String quitCommand = quitParts[0].toUpperCase();


        if (!quitCommand.equals("QUIT")) {
            send("500 Syntax error, command unrecognized", pBW);
            return;
        }

        send("221 OK", pBW);
    }



    static private void send(final String pMessage, final BufferedWriter pBW) {
        try {
            pBW.write(pMessage + "\n");
            pBW.flush();
            if (DEBUG) System.out.println("SENT:\t" + pMessage);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }




    static private String read(final BufferedReader pBR) throws IOException {
        try {
            final String reply = pBR.readLine();
            if (DEBUG) System.out.println("RECV:\t" + reply);
            return reply;

        } catch (final SocketTimeoutException e) {
            System.err.println("SERVER TIMEOUT");
        }
        return null;
    }
}

