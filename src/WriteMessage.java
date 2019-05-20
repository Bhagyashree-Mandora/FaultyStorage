import java.nio.ByteBuffer;
import java.util.Arrays;

public class WriteMessage extends Message {

    private static final String OPCODE = "W";
    private static final int ENCODE_SIZE = 48;

    public String[] decode(byte[] data) throws Exception {
        if (data.length == RESPONSE_SIZE && (char) data[0] == 'A') {
            if (data[RESPONSE_SIZE - 1] == 1) {
//                char opcode = (char) data[0];
//                String filename = new String(Arrays.copyOfRange(data, 1,33)).trim();
//                byte[] loc = Arrays.copyOfRange(data, 33,37);
//                int location = getIntFromByteArray(loc);
//                int dataLen = data[37];
//                String payloadData = new String(Arrays.copyOfRange(data, 38,48)).trim();
//                System.out.println(opcode + " " + filename + " " + location + " " + dataLen + " " + payloadData);
                return new String[]{"write successful"};
            }
        } else {
            throw new Exception("Wrong UDP response size");
        }
        return new String[0];
    }

    public byte[] encode(String filename, byte[] data, int offset) {
        ByteBuffer buffer = ByteBuffer.allocate(ENCODE_SIZE);
        buffer.put(OPCODE.getBytes()[0]);

        byte[] nullPadding = new byte[32 - filename.length()];
        buffer.put(filename.getBytes());
        buffer.put(nullPadding);

        //file offset in little-endian: 4 bytes
        buffer.put((byte) (offset));
        buffer.put((byte) (offset >> 8));
        buffer.put((byte) (offset >> 16));
        buffer.put((byte) (offset >> 24));

        int dataLen = data.length;
        buffer.put((byte) dataLen);

        nullPadding = new byte[10 - dataLen];
        buffer.put(data);
        buffer.put(nullPadding);

        return buffer.array();
    }
}
