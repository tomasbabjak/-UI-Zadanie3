package pack;

import java.util.Random;

/**
 * Trieda Garden predstavuje záhradku čiže gardenMap ako 2D pole naplnené hodnotami 0 alebo -1 pre pôdu resp. kameň.
 * Ďalej obsahuje premenné x a y , ktoré slúžia na vytvorenie záhradky daných rozmerov.
 */
public class Garden {

    public static final int SOIL = 0;
    public static final int STONE = -1;

    public int [][] gardenMap;
    //public int numberOfStones = 6;
    public int x,y;

    /**
     * Funkcia, ktorá vytvorí záhradu ako dvojrozmerné pole a naplní ju číslami 0 pre pôdu a -1 pre kamene.
     */
    public void createGarden() {

        gardenMap = new int[y][x];

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                gardenMap[i][j] = SOIL;
            }
        }

        insertStone();

    }

    /**
     * Funkcia, ktorá slúži na vkladanie kameňov do záhradky.
     */
    public void insertStone(){

        //Random ran = new Random();
        //gardenMap[ran.nextInt(y)][ran.nextInt(x)] = -1

//        Map 2:
//        gardenMap[1][8] = STONE;
//        gardenMap[2][2] = STONE;
//        gardenMap[3][7] = STONE;
//        gardenMap[4][1] = STONE;

        //Map 1:
        gardenMap[2][1] = STONE;
        gardenMap[4][2] = STONE;
        gardenMap[3][4] = STONE;
        gardenMap[1][5] = STONE;
        gardenMap[6][8] = STONE;
        gardenMap[6][9] = STONE;

    }

    /**
     * Funkcia na výpis záhradky.
     */
    public void printGarden(){

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                System.out.print(gardenMap[i][j] + "  ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

}
