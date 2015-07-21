import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Server extends JFrame {
     
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private ServerSocket server;
    private Socket sock;
     
    public Server() {
         
        super("Instant Messenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                });
         
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);
         
    }
     
    //set up server
    public void start() {
         
        try {
             
            server = new ServerSocket(8888, 100);
            while (true) {
                 
                try {
                     
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                     
                } catch (EOFException eofe) {
                    showMessage("\n Server ended the connection.");         
                } finally {
                    close();
                }
            }
             
        } catch (IOException ioe) {
             
            ioe.printStackTrace();
             
        }
         
    }
     
    //wait for connection, and display connection info
    private void waitForConnection() throws IOException {
         
        showMessage("Waiting for a connection...\n");
        sock = server.accept();
        showMessage(" Now connected to " +sock.getInetAddress().getHostName());
    }
     
    //send and receive data
    private void setupStreams() throws IOException {
         
        oos = new ObjectOutputStream(sock.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(sock.getInputStream());
        showMessage("\n Streams are setup. \n");
         
        }
     
    //during the conversation
    private void whileChatting() throws IOException {
         
        String message = " You are connected. ";
        sendMessage(message);
        ableToType(true);
        do {
             
            try {
                 
                message = (String) ois.readObject();
                showMessage("\n" + message);
                 
            } catch (ClassNotFoundException cnfe) {
                showMessage("\n Message read error. ");
            }
        } while(!message.equals("CLIENT - END"));
         
    }
     
    //close program
    private void close() {
         
        showMessage("\n Closing connections... \n");
        ableToType(false);
        try {
             
            oos.close();
            ois.close();
            sock.close();
             
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
     
    //send message
    private void sendMessage(String message) {
         
        try {
             
            oos.writeObject("Server - " + message);
            oos.flush();
            showMessage("\nServer - " + message);
             
        } catch (IOException ioe) {
            chatWindow.append("\n Error: Message send failed");
        }
         
    }
     
    //displays message
    private void showMessage(final String txt) {
         
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    chatWindow.append(txt);
                }
            }
        );      
    }
     
    //sets textfield so user can type
    private void ableToType(final boolean bool) {
         
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        userText.setEditable(bool);
                    }
                }
            );  
    }
     
}