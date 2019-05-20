public class UdpMessage {

    public byte[] encode(){
        byte[] buf = new byte[1+32+4+1+10];

        byte[] opCode = "W".getBytes();

        byte[] fileName = "1.txt".getBytes();
        byte[] location = toBytes(7);
        byte[] data = "hellohello".getBytes();
        byte dataLength = (byte) data.length;

        System.arraycopy(opCode,0,buf,0,1);
        System.arraycopy(fileName,0,buf,1,fileName.length);
        System.arraycopy(location,0,buf,33,location.length);
        buf[37]=dataLength;
        System.arraycopy(data,0,buf,38,data.length);
        for (byte b:buf) {
            System.out.println(b);
        }
        return buf;
    }

    private static byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[3] = (byte) (i >> 24);
        result[2] = (byte) (i >> 16);
        result[1] = (byte) (i >> 8);
        result[0] = (byte) (i /*>> 0*/);

        return result;
    }
}
