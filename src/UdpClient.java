import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class UdpClient {

    private static final int SEND_TO_PORT = 1982;
    private static final int LISTEN_AT_PORT = 1983;
    private DatagramSocket sendToSocket, ListenAtSocket;
    private InetAddress host;

    public UdpClient() throws SocketException, UnknownHostException {
//        ListenAtSocket = new DatagramSocket(LISTEN_AT_PORT);
        sendToSocket = new DatagramSocket();
        host = InetAddress.getByName("localhost");
    }

//    public void receiveData() {
//        byte[] buffer = new byte[60];
//        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
//        try {
//            ListenAtSocket.receive(reply);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        byte[] data = reply.getData();
//
//        Message recvMsg = Message.Decode(data);
//    }

    public void sendData() throws IOException {
        Message sendMsg = new Message();

        byte[] b = sendMsg.Encode();
        ByteBuffer buffer = ByteBuffer.wrap(b);
        String s = new String(buffer.array());
        System.out.println(s);
        DatagramPacket datagramPacket = new DatagramPacket(b, 48, host, SEND_TO_PORT);
        sendToSocket.send(datagramPacket);
    }

}
