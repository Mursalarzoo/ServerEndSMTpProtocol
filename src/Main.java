
/*
This program is written by Mursal Hosseini
In this program I am implementing the server end of the SMTP protocol in JAVA

Reference:
Code inspired by: https://stackoverflow.com/questions/66534689/how-do-i-code-my-own-smtp-server-using-java
Code inspired by: https://commandlinefanatic.com/cgi-bin/showarticle.cgi?article=art076
Code inspired by: https://stackoverflow.com/questions/40059025/proper-response-to-smtp-helo
*/

import java.io.IOException;

public class Main {

    public static void main(final String s[]) throws  IOException {        // main method serves as the entry point of the program

        Connection connection = new Connection();     // Instantiate a new Connection object to establish a connection
        connection.setPort(25);                       // Set the connection
        connection.start();

        try {                                        // After starting the connection, the program enters a try-catch block
            Thread.sleep(60 * 60 * 1000);      // Inside the try block, the 'Thread.sleep' method is called to pause the execution of the program for 1 hour (60 minutes * 60 seconds * 1000 milliseconds)
        } catch (final InterruptedException e) {     // If an InterruptedException occurs during the sleep, the catch block is executed
            System.out.println(e);                   // Inside the catch block, the exception 'e' is printed to the console
        }

    }
}


