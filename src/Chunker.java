import java.io.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class Chunker {

    private UdpClient udpClient;
    private WriteEncoder writeMessage;
    private ReadEncoder readMessage;
    private final int COPIES;

    public Chunker(int copies) throws SocketException, UnknownHostException {
        udpClient = UdpClient.GetInstance();
        writeMessage = new WriteEncoder();
        readMessage = new ReadEncoder();
        this.COPIES = copies;
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

                for (int j = 1; j <= COPIES; j++) {
                    byte[] encodedChunk = writeMessage.encode(j + "_" + fileName, chunk, i);

                    Message decodedResponse;
                    do {
                        byte[] response = udpClient.sendData(encodedChunk);
                        decodedResponse = writeMessage.decode(response);
                    } while (decodedResponse == null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String fileName) throws Exception {
        boolean end = false;
        int offset = 0;
        int len;
        String fileDest = "/home/bhago/Desktop/op" + fileName;
        FileOutputStream fos = new FileOutputStream(fileDest);
//        StringBuilder sb = new StringBuilder();
        byte[] ignore = new byte[0];
        while (!end) {
            Message[] responses = new Message[COPIES];
//            System.out.println("Offset: " + offset);
            for (int j = 1; j <= COPIES; j++) {
                byte[] encodedMsg = readMessage.encode(j + "_" + fileName, ignore, offset);

                Message decodedResponse;
                do {
                    byte[] response = udpClient.sendData(encodedMsg);
                    decodedResponse = readMessage.decode(response);
                    if (decodedResponse != null) {
//                        System.out.println("Chunker " + j + " :" + new String(decodedResponse.data));
                    }
                    responses[j - 1] = decodedResponse;
                } while (decodedResponse == null);
            }

            len = responses[0].dataLen;
            offset = responses[0].offset + len;
            if (len < 10) {
                end = true;
            }

            boolean success = true;
            byte[] chunk = new byte[len];
            int majority = (COPIES + 1) / 2;
            for (int b = 0; b < len; b++) {
                HashMap<Byte, Integer> map = new HashMap<>();
                boolean found = false;
                for (Message msg : responses) {
                    int count = map.getOrDefault(msg.data[b], 0);
                    if (count + 1 >= majority) {
                        chunk[b] = msg.data[b];
                        found = true;
                        break;
                    }
                    map.put(msg.data[b], count + 1);
                }
                if (!found) {
                    offset = responses[0].offset;
                    success = false;
                    break;
                    //throw new Exception("Irrecoverable data");
                }
            }
            if (success) {
                fos.write(chunk);
//                sb.append(new String(chunk));
            }
        }
//        System.out.println(sb.toString());
//        Path opPath = Paths.get("/home/bhago/Desktop/op" + fileName);
        fos.close();
//        Files.write(opPath, sb.toString().getBytes());
    }
}
