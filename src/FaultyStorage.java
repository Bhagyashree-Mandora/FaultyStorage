import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Thread.sleep;

public class FaultyStorage {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter test case number: ");
        int testCase = Integer.parseInt(br.readLine());
        String fileName = "SmalTextFile.txt";
        long time = 30000;
        int copies = 5;

        switch (testCase){
            case 1 :
                fileName = "SmalTextFile.txt";
                copies = 3;
                break;

            case 2 :
                fileName = "LargeTextFile.txt";
                copies = 5;
                break;

            case 3:
                fileName = "SmallBinary.jpg";
                copies = 5;
                break;

            case 4:
                fileName = "LargeBinary.wav";
                copies = 5;
                break;

            case 5:
                fileName = "SmallBinary.jpg";
                time = 180000;
                copies = 5;
                break;

            case 6:
                fileName = "SmalTextFile.txt";
                time = 15000;
                copies = 13;
                break;
        }

        try {
            Chunker chunker = new Chunker(copies);
            Checker checker = new Checker(fileName, copies);
            checker.start();

            System.out.println("Starting to write file " + fileName);
            chunker.writeFile(fileName);

            System.out.println("Write completed. Waiting for " + time/1000 + " seconds");
            sleep(time);

            System.out.println("Starting to read file " + fileName);
            chunker.readFile(fileName);
            System.out.println("Read completed. Please check the output!");

            checker.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
