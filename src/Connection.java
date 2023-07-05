import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
    Communication communication = new Communication();          // Create an instance of the Communication class to handle communication tasks
    private ServerSocket ssocket = null;        // Declare a private ServerSocket variable


    public void setPort(final int port) throws IOException {
        try {
            ssocket = new ServerSocket(port);


        } catch (final java.net.BindException e) {
            new java.net.BindException("When mountain port " + port + ": " + e.getMessage()).printStackTrace();
        }
        System.out.println("Created server socket on port " + port);
    }



    public void start() {
        if (ssocket == null) return;        //If the server socket is not set, return and do nothing
        final Thread t = new Thread(() -> handleServerSocket(), "handleServerSocket(" + ssocket.getLocalPort() + ")");     // Create a new thread and assign it to the handleServerSocket() method, with a name including the local port number
        t.start();              // Start the thread, which will execute the handleServerSocket() method
    }



    public void handleServerSocket() {
        final String name = "handleServerSocket(" + ssocket.getLocalPort() + ")";       // Create a name string for identifying the handleServerSocket thread, including the local port number
        while (true) {
            System.out.println(name + "\tListening for connection...");                 // Print a message indicating that the thread is listening for connections

            // Accept a new socket connection and create input and output streams for communication
            try (final Socket socket = ssocket.accept();
                 final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "8859_1"));                       //input
                 final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "8859_1"))) {      //output

                System.out.println(name + "\tGot new Socket.");
                communication.handle(socket, in, out);              // Call the handle() method of the Communication instance to handle the socket communication

                System.out.println(name + "\tClosing Socket.");     // Print a message indicating that the socket is being closed

            } catch (final IOException e) {                         // If the IOException occurs during socket handling, print the stack trace with a custom message
                System.err.println("In " + name + ":");
                e.printStackTrace();
            }
            System.out.println(name + "\tComm Done.");              // Print a message indicating that the socket communication is done.
        }
    }
}



