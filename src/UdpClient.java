import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;

public class UdpClient {

    private static final int SEND_TO_PORT = 1982;
    private static final int LISTEN_AT_PORT = 1983;
    private DatagramSocket sendToSocket, ListenAtSocket;
    private InetAddress host;

    public UdpClient() throws SocketException, UnknownHostException {
        ListenAtSocket = new DatagramSocket(LISTEN_AT_PORT);
        sendToSocket = new DatagramSocket();
        host = InetAddress.getByName("localhost");
    }

    public byte[] sendData(byte[] data) throws IOException {
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, host, SEND_TO_PORT);
        sendToSocket.send(datagramPacket);

        byte[] buffer = new byte[49];
        DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
        try {
            ListenAtSocket.receive(reply);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reply.getData();
    }
}
