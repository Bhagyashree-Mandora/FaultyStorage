import java.util.Arrays;

public class Message {
    public byte[] data;
    public String fileName;
    public int dataLen;
    public int offset;

    public Message() {
    }

    public Message(String fileName, int offset, int dataLen, byte[] data) {
        this.fileName = fileName;
        this.offset = offset;
        this.dataLen = dataLen;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
