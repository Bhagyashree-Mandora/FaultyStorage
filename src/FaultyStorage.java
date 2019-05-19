import java.io.IOException;

public class FaultyStorage {
    public static void main(String[] args) {
        try {
            UdpClient udpClient = new UdpClient();
            udpClient.sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        udpClient.receiveData();
    }
}
