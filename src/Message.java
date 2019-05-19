import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Message {

    public void Decode(byte[] data) {
        if(data != null && data.length == 49){
            ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            String s = new String(buffer.array());
            System.out.println(s);
            if("1".equals(s.substring(48))){
               String opcode = s.substring(0,1);    //convert hex to ascii
               String filename = s.substring(1,33).trim();
               int location = Integer.parseInt(s.substring(33,37)); //msb to lsb req?
               int dataLen = Integer.parseInt(s.substring(37,38));  //convert hex to int
               String payloadData = s.substring(38,48);
                System.out.println(opcode + " " + filename + " " + location + " " + dataLen + " " + payloadData);
            } else {
                //retry
            }
        }
    }

    public byte[] Encode() {
        ByteBuffer buffer = ByteBuffer.allocate(count()).order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) 0x57);    //for 'W'
        String filename = "1.txt";
        buffer.put(String.format("%-" + 32 + "s", filename).getBytes());     //padded filename till 32 bytes

//        buffer.putInt(255);
        int offset = 20;
//        buffer.put(Integer.toHexString(offset).getBytes());
        buffer.put((byte)(offset >> 24));
        buffer.put((byte)(offset >> 16));
        buffer.put((byte)(offset >> 8));
        buffer.put((byte)(offset));
//        buffer.put((byte)0);   //offset
//        buffer.put((byte)0);   //offset
//        buffer.put((byte)0);   //offset
//        buffer.put((byte)0);   //offset
        String data = "hellohello";
        buffer.put(Integer.toHexString(data.length()).getBytes());  //data length
        buffer.put(data.getBytes());    //data
        System.out.println(new String(buffer.array()).substring(1,33));
//        System.out.println(Integer.parseInt(new String(buffer.array()).substring(33,37)));
        System.out.format("0x%x ", buffer.get(33));
        System.out.format("0x%x ", buffer.get(34));
        System.out.format("0x%x ", buffer.get(35));
        System.out.format("0x%x ", buffer.get(36));
        System.out.println(new String(buffer.array()).substring(33,37));
        System.out.println(new String(buffer.array()).substring(37,38));
        System.out.println(new String(buffer.array()).substring(38,48));
        System.out.println(buffer.array().length);
        return buffer.array();
    }

    private int count() {
        return 48;
    }

}
