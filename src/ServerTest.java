import javax.swing.JFrame;
 
public class ServerTest {
     
    public static void main(String[] args) {
         
        Server serve = new Server();
        serve.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serve.start();
         
    }
     
}