package Server;
import javax.swing.JFrame;

public class ServerTest{
    public static void main(String[] args){
       Server abc = new Server();
       abc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       abc.startRunning();
    }
}

