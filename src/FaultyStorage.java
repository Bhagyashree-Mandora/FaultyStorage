import java.io.IOException;

public class FaultyStorage {
    public static void main(String[] args) throws Exception {
        try {
            Chunker chunker = new Chunker();
            chunker.writeFile("/home/bhago/workspace/IdeaProjects/FaultyStorage/src/test.txt");
//            chunker.readFile("2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
