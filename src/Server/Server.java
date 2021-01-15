package Server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Server extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    
    //Constructor
    public Server(){
    super("Abhilasha Messenger");    
    userText = new JTextField();
    userText.setEditable(false);
    userText.addActionListener(
             new ActionListener(){
                 public void actionPerformed(ActionEvent event){
                        sendMessage(event.getActionCommand());
                        userText.setText("");
                 }
             }
    );
    add(userText ,BorderLayout.SOUTH);
    chatWindow = new JTextArea();
    add(new JScrollPane(chatWindow));
    setSize(300,200);
    setVisible(true);
            
    }
    //setup and run the server
    public void startRunning(){
        try{
            server = new ServerSocket(6789 , 100);
              while(true){
                  try{
                      waitForConnection();
                      setupStreams();
                      whileChatting();
                  }catch(EOFException eofException){
                     showMessage("\n Sever ended the connection! ");
                  }finally{
                   closeCrap();
                  }
              }
            }catch(IOException ioException){
                   ioException.printStackTrace();
                   }
    }
//wait for the connection, then display the connection information
private void waitForConnection() throws IOException{
     showMessage("Waiting for for someone to connect....\n ");
     connection = server.accept();
     showMessage("Now connected to " + connection.getInetAddress().getHostName());
}
//get streams to send and recieve data
private void setupStreams() throws IOException{
     output = new ObjectOutputStream(connection.getOutputStream());
     output.flush();
     input = new ObjectInputStream(connection.getInputStream());
     showMessage("\n Streams are now setup! \n");
}
//during the chat Conversation
private void whileChatting() throws IOException{
   String message = "You are now connected! ";
   sendMessage(message);
   ableToType(true);
   do{
      try{
          message = (String) input.readObject();
          showMessage("\n" +message);
      }catch(ClassNotFoundException classNotFoundException){
          showMessage("Attachment Unavailable");
      }
   }while(!message.equals("CLIENT - END"));
}
//close streams and sockets after you are done  chatting
private void closeCrap(){
    showMessage("\n closing connection....\n");
    ableToType(false);
    try{
        output.close();
        input.close();
        connection.close();
    }catch(IOException ioException){
         ioException.printStackTrace();
    }
}
//send a message to client
private void sendMessage(String message){
      try{
          output.writeObject("Abhilasha - " + message);
          output.flush();
          showMessage("\n Abhilasha - " + message);
      }catch(IOException ioexception){
        chatWindow.append("\n Erroe:Something went wrong");
      }
}
//Updates chatWindow
private void showMessage(final String text){
     SwingUtilities.invokeLater(
         new Runnable(){
             public void run(){
                  chatWindow.append(text);
             }
         }
     );
}
//let the user type into their box
private void ableToType(final Boolean tof){
     SwingUtilities.invokeLater(
          new Runnable(){
              public void run(){
                   userText.setEditable(tof);
              }
          }
     );
}
}