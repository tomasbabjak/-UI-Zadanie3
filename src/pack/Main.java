package pack;

import java.io.*;

public class Main {

    private static final int NUMBEROFSTONES = 6;
    private static final int NUMBEROFMONKS = 100;
    private static final int NUMBEROFREPS = 100;
    private static final int X = 12;
    private static final int Y = 10;

    public static void main(String[] args) {

        int sum = 0;
        for (int i = 0; i < NUMBEROFREPS; i++) {
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
                //System.out.println(line);
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
        File file = new File("evo3000.txt");

        if(file.delete())
        {
            System.out.println("File deleted successfully");
        }
        else
        {
            System.out.println("Failed to delete the file");
        }

        System.out.print("Priemerny pocet generácií mnichov je " + sum/NUMBEROFREPS);
    }
}
