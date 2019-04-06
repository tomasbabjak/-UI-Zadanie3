package pack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static final int NUMBEROFSTONES = 4;
    private static final int NUMBEROFMONKS = 100;
    private static final int X = 10;
    private static final int Y = 6;

    public static void main(String[] args) {

        int sum = 0;
        for (int i = 0; i < 100; i++) {
            Evolution e = new Evolution(NUMBEROFMONKS, NUMBEROFSTONES, X, Y);
            try {
                e.initialize();
            } catch (IOException ex) {
            }
        }

        FileInputStream fis = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream("evo3000.txt");
            reader = new BufferedReader(new InputStreamReader(fis));

            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                sum = sum + Integer.parseInt(line);
                line = reader.readLine();
            }

        } catch (IOException ex1) {
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex3) {
            }
        }
        System.out.print("Priemerny pocet mnichov je " + sum/100);
    }
}
