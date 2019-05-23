import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Checker extends Thread {
    private String fileName;
    private UdpClient udpClient;
    private WriteEncoder writeMessage;
    private ReadEncoder readMessage;
    private final int COPIES;

    public Checker(String fileName, int copies) throws SocketException, UnknownHostException {
        udpClient = UdpClient.GetInstance();
        writeMessage = new WriteEncoder();
        readMessage = new ReadEncoder();
        this.fileName = fileName;
        this.COPIES = copies;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
            check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check() throws Exception {
        int offset = 0;
        int len;
        byte[] ignore = new byte[0];
        while (true) {
            Message[] responses = new Message[COPIES];
            for (int j = 1; j <= COPIES; j++) {
                byte[] encodedMsg = readMessage.encode(j + "_" + fileName, ignore, offset);

                Message decodedResponse;
                do {
                    byte[] response = udpClient.sendData(encodedMsg);
                    decodedResponse = readMessage.decode(response);
                    responses[j - 1] = decodedResponse;
                } while (decodedResponse == null);
            }

            boolean readIncorrect = false;
            for(int i=0; i<COPIES-1; i++){
                if (responses[i].dataLen != responses[i+1].dataLen) {
                    readIncorrect = true;
                    break;
                }
            }

            if(readIncorrect){
                continue;
            }
            len = responses[0].dataLen;
            offset = responses[0].offset + len;
            if (len < 10) {
                // Start checking the file again from start
                offset = 0;
//                Thread.sleep(2000);
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
                    for(Message m: responses) {
                        System.out.println("Response: " + new String(m.data));
                    }
                    System.out.println("Irrecoverable data");
                    // offset = responses[0].offset;
                    success = false;
                    break;
//                    throw new Exception("Irrecoverable data");
                }
            }

            if (success) {
                rectify(chunk, responses[0].offset);
            }
        }
    }

    private void rectify(byte[]data, int offset) throws Exception {
        for(int j=1; j<=COPIES; j++){
            byte[] encodedChunk = writeMessage.encode(j+ "_" + fileName, data, offset);

            Message decodedResponse;
            do {
                byte[] response = udpClient.sendData(encodedChunk);
                decodedResponse = writeMessage.decode(response);
            } while (decodedResponse == null);
        }
    }

}
