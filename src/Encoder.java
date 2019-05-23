public abstract class Encoder {
    static final int RESPONSE_SIZE = 49;

    public abstract Message decode(byte[] data) throws Exception;

    public abstract byte[] encode(String filename, byte[] data, int offset);

    int getIntFromByteArray(byte[] bytes) {
        return bytes[3] << 24 | (bytes[2] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8 | (bytes[0] & 0xFF);
    }
}
