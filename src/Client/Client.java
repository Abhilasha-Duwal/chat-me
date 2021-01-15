package Client;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
    
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    
   //Constructor
    public Client(String host){
        super("Messenger");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                   sendMessage(event.getActionCommand());
                   userText.setText(" ");
                }
            }
        );
        add(userText, BorderLayout.SOUTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow) , BorderLayout.CENTER);
        setSize(300,200);
        setVisible(true);
    }
    //connect to the server
    public void startRunning(){
       try{
            connectToServer();
            setupStreams();
            whileChatting();
       }catch(EOFException eofException){
          showMessage("\n client terminated connection");
       }catch(IOException ioException){
          ioException.printStackTrace();
       }finally{
            closeCrap();
       }
    }
    //connect to the server
    private void connectToServer() throws IOException{
     showMessage("attempting connection.....");
     connection  = new Socket(InetAddress.getByName(serverIP),6789);
     showMessage("Connected to:" + connection.getInetAddress().getHostName());
    }
    //setup Streams to send and recieve messages
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n your Streams are setup \n");
    }
    //while chatting with server
    private void whileChatting() throws IOException{
       ableToType(true);
       do{
          try{
              message = (String) input.readObject();
              showMessage("\n" + message);
       }catch(ClassNotFoundException classnotfoundException){
           showMessage("\n Unknown Object type");
       }
       }while(!message.equals("Abhilasha - END"));
    }
    //close the streams and sockets
    private void closeCrap(){
       showMessage("\n closing crap down"); 
       ableToType(false);
       try{
           output.close();
           input.close();
           connection.close();
       }catch(IOException ioException){
          ioException.printStackTrace();
       }
    }
    //send message to server
    private void sendMessage(String message){
         try{
             output.writeObject("CLIENT -" + message);
             output.flush();
             showMessage("\nCLIENT -" + message);
         }catch(IOException ioException){
             chatWindow.append("\n something went wrong send message! ");
         }
    }
    //chsnge/update chatWindow
    private void showMessage(final String m){
       SwingUtilities.invokeLater(
          new Runnable(){
              public void run(){
                  chatWindow.append(m);
              }
          }
       );
    }
    //give user permission to type message into the text box
    private void ableToType(final boolean tof){
       SwingUtilities.invokeLater(
          new Runnable(){
              public void run(){
                  userText.setEditable(tof);
                        }
                   }
       );
}
}

