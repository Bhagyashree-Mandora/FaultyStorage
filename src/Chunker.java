import java.io.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Chunker {

    private UdpClient udpClient;
    private WriteMessage writeMessage;
    private ReadMessage readMessage;

    public Chunker() throws SocketException, UnknownHostException {
        udpClient = new UdpClient();
        writeMessage = new WriteMessage();
        readMessage = new ReadMessage();
    }

    public void writeFile(String fileName) throws Exception {
        Path location = Paths.get(fileName);
        try {
            byte[] data = Files.readAllBytes(location);
            for (int i = 0; i < data.length; i = i + 10) {
                int end = i + 10;
                if (i + 10 > data.length) {
                    end = data.length;
                }
                byte[] chunk = Arrays.copyOfRange(data, i, end);
                byte[] encodedChunk = writeMessage.encode("2.txt", chunk, i);

                String[] decodedResponse;
                do {
                    byte[] response = udpClient.sendData(encodedChunk);
                    decodedResponse = writeMessage.decode(response);
                } while (decodedResponse.length == 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String fileName) throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean end = false;
        int offset = 0;
        byte[] ignore = new byte[0];
        while (!end) {
            byte[] encodedMsg = readMessage.encode(fileName, ignore, offset);

            String[] decodedResponse;
            do {
                byte[] response = udpClient.sendData(encodedMsg);
                decodedResponse = readMessage.decode(response);
            } while (decodedResponse.length == 0);

            //{filename, String.valueOf(location), String.valueOf(dataLen), payloadData};
            int off = Integer.parseInt(decodedResponse[1]);
            int len = Integer.parseInt(decodedResponse[2]);
//            sb.append(decodedResponse[3].toCharArray(), off, len);
            sb.append(decodedResponse[3]);
            if (len < 10) {
                end = true;
            }
            offset += len;
//            System.out.println("offset: " + offset + " end: " + end);
        }
    }
}
