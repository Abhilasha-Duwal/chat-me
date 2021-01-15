package Client;
import javax.swing.JFrame;

public class ClientTest {
    public static void main(String[] args){
        Client suman;
        suman = new Client("127.0.0.1");
        suman.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        suman.startRunning();
    }
}
