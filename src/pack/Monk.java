package pack;

import java.util.Random;

public class Monk {

    public int fitness = 0;
    public double strength = 0;
    public int x, y;
    public int numberOfStones;
    public int numberOfGenes = x + y + numberOfStones;
    public int[] genes;
    public int counter = 0;
    public boolean blocked = false;

    /**
     * Konštruktor pre Mnícha - Monk, ktorý vytvorí inštanciu mnícha s danými parametrami.
     * @param x Označuje šírku záhrady.
     * @param y Označuje dĺžku záhrady.
     * @param numberOfStones Počet kameňov, ktoré bude vytvorená záhrada obsahovať.
     */
    public Monk(int x, int y, int numberOfStones) {
        this.x = x;
        this.y = y;
        this.numberOfStones = numberOfStones;
    }

    /**
     * Funkcia, v ktorej sa náhodne vygenerujú počiatočné gény mnícha.
     */
    public void initializeGenes() {

        Random ran = new Random();
        numberOfGenes = x + y + numberOfStones;
        genes = new int[numberOfGenes];

        for (int i = 0; i < numberOfGenes; i++) genes[i] = ran.nextInt((x + y) * 2);
    }

    /**
     * Funkcia na výpis génov mnícha.
     */
    public void printGenes() {

        for (int i = 0; i < x + y + numberOfStones; i++) System.out.print(genes[i] + " ");

    }

    /**
     * Funkcia, ktorá kontroluje, či je pole so súradnicmi yMap a xMap v záhrade a či sa na ňom nachádza prekážka.
     * @return Ak je políčko mimo mapy alebo sa na ňom nachádza kameň alebo prehrabaná pôda vráti false, inak vráti true.
     */
    public boolean validField(Garden g, int yMap, int xMap) {

        if (yMap >= y || xMap >= x || yMap < 0 || xMap < 0) return false;
        if (g.gardenMap[yMap][xMap] == 0) return true;
        else return false;
    }

    /**
     * Funkcia, ktorá kontroluje, či je pole so súradnicmi yMap a xMap v záhrade.
     * @return Ak je políčko mimo záhrady vráti true inak false.
     */
    public boolean outOfGarden(Garden g, int yMap, int xMap) {
        if (yMap >= y || xMap >= x || yMap < 0 || xMap < 0) return true;
        return false;
    }

    /**
     * Kontroluje, či sa na políčku s danými súradnicami v záhrade nenachádza kameň alebo prejdená pôda.
     * @return Ak sa nachádza prekážka vráti true inak vráti falsse.
     */
    public boolean blockAhead(Garden g, int yMap, int xMap) {

        if (!outOfGarden(g, yMap, xMap) && (g.gardenMap[yMap][xMap] == -1 || g.gardenMap[yMap][xMap] > 0)) return true;
        else return false;

    }

    /**
     * Ak sa pri hrabaní záhrady smerom hore alebo dole vyskytne ako nasledujúce políčko v danom smere prekážka, musí zmeniť mních smer.
     * Najprv sa pozrie doprava (na políčko s väčšou x-ovou súradnicou) a potom do ľava (na políčko s menšou x-ovou súradnicou) a kde sa nachádza nepohrabané políčko tam bude nasledovať.
     * Ak sa nemôže pohnúť ani jedným smerom nastaví premennej blocked hodnotu true.
     * @return Ak sa môže pohnúť do pravej alebo ľavej strany vráti true, ak sa zablokuje vráti false.
     */
    public boolean changeDirection(Garden g, int yMap, int xMap) {

        if (!blockAhead(g, yMap, ++xMap)) {
            right(g, yMap, xMap);
            return true;
        }

        --xMap;
        if (!blockAhead(g, yMap, --xMap)) {
            left(g, yMap, xMap);
            return true;
        }
        blocked = true;
        return false;
    }
    /**
     * Ak sa pri hrabaní záhrady smerom doprava alebo doľava vyskytne ako nasledujúce políčko v danom smere prekážka, musí zmeniť mních smer.
     * Najprv sa pozrie dole (na políčko s väčšou y-ovou súradnicou) a potom do hore (na políčko s menšou y-ovou súradnicou) a kde sa nachádza nepohrabané políčko tam bude nasledovať.
     * Ak sa nemôže pohnúť ani jedným smerom nastaví premennej blocked hodnotu true.
     * @return Ak sa môže pohnúť hore alebo dole vráti true, ak sa zablokuje vráti false.
     */
    public boolean changeDirectionUpDown(Garden g, int yMap, int xMap) {
        if (!blockAhead(g, ++yMap, xMap)) {
            down(g, yMap, xMap);
            return true;
        }

        --yMap;
        if(!blockAhead(g, --yMap, xMap)) {
            up(g, yMap, xMap);
            return true;
        }
        blocked = true;
        return false;
    }

    /**
     * Mních hraba záhradu do "pravej strny" strany s vyššou hodnotou súradnice x.
     * Obsahuje kontrolu prekážky na ceste a následnou zmenou smeru.
     */
    public void right(Garden g, int yMap, int xMap) {

        while (validField(g, yMap, xMap)) {
            g.gardenMap[yMap][xMap] = counter;
            xMap = ++xMap;
            if (blockAhead(g, yMap, xMap)) {
                changeDirectionUpDown(g, yMap, --xMap);
                break;
            }
        }
    }

    /**
     * Mních hraba záhradu do "ľavej strny" strany s nižšou hodnotou súradnice x.
     * Obsahuje kontrolu prekážky na ceste a následnou zmenou smeru.
     */
    public void left(Garden g, int yMap, int xMap) {

        while (validField(g, yMap, xMap)) {
            g.gardenMap[yMap][xMap] = counter;
            xMap = --xMap;
            if (blockAhead(g, yMap, xMap)) {
                changeDirectionUpDown(g, yMap, ++xMap);
                break;
            }
        }
    }

    /**
     * Mních hraba záhradu smerom "hore" teda do strany s nižšou hodnotou súradnice y.
     * Obsahuje kontrolu prekážky na ceste a následnou zmenou smeru.
     */
    public void up(Garden g, int yMap, int xMap) {

        while (validField(g, yMap, xMap)) {
            g.gardenMap[yMap][xMap] = counter;
            yMap = --yMap;

            if (blockAhead(g, yMap, xMap)) {
                changeDirection(g, ++yMap, xMap);
                break;
            }
        }
    }

    /**
     * Mních hraba záhradu smerom "dole" teda do strany s vyššou hodnotou súradnice y.
     * Obsahuje kontrolu prekážky na ceste a následnou zmenou smeru.
     */
    public void down(Garden g, int yMap, int xMap) {

        while (validField(g, yMap, xMap)) {
            g.gardenMap[yMap][xMap] = counter;
            yMap = ++yMap;

            if (blockAhead(g, yMap, xMap)) {
                changeDirection(g, --yMap, xMap);
                break;
            }
        }
    }

    /**
     * Hlavná funkcia pre mnícha, teda hrabanie záhradky.
     * Najprv sa vytvorí prázdna záhradka a pre každý mníchov gén sa pokúsi vstúpiť do záhradky a pohrabať ju.
     * Na základe mníchovho génu sa vypočíta vstupná súradnica a určí sa z ktorej strany záhradky vošiel a ktorým smerom ju má hrabať.
     * Na záver sa vypočíta mníchova fitness hodnota na základe počtu pohrabaných políčok.
     * @param write Určuje či sa vypíše na konci záhradka alebo nie. Vypíše sa iba pri poslednom mníchovy, teda pri tom, ktorý ako prvý úspešne pohrabal celú záhradku.
     */
    public void rakeGarden(boolean write) {

        Garden g = new Garden();
        g.x = x;
        g.y = y;
        g.createGarden();

//        for (int i = 0; i < x + y; i++) {
        for (int i = 0; i < numberOfGenes; i++) {
            int in = genes[i];
            int yMap;
            int xMap;
            int validGene = 0;
            counter++;

            if (in < x && in > -1) { //vosiel zhora
                yMap = 0;
                xMap = in;

                while (validField(g, yMap, xMap)) {
                    validGene++;
                    g.gardenMap[yMap][xMap] = counter;
                    yMap = ++yMap;

                    if (blockAhead(g, yMap, xMap)) {
                        changeDirection(g, --yMap, xMap);
                        break;
                    }
                }
            }

            if (in >= x + y && in < 2 * x + y) { //vosiel zdola
                yMap = y - 1;
                xMap = 2 * x + y - in - 1;

                while (validField(g, yMap, xMap)) {
                    g.gardenMap[yMap][xMap] = counter;
                    yMap = --yMap;
                    validGene++;

                    if (blockAhead(g, yMap, xMap)) {
                        changeDirection(g, ++yMap, xMap);
                        break;
                    }
                }
            }

            if (in >= x && in < x + y) { //vosiel sprava
                yMap = in - x;
                xMap = x - 1;

                while (validField(g, yMap, xMap)) {
                    validGene++;
                    g.gardenMap[yMap][xMap] = counter;
                    xMap = --xMap;
                    if (blockAhead(g, yMap, xMap)) {
                        changeDirectionUpDown(g, yMap, ++xMap);
                        break;
                    }
                }
            }

            if (in >= 2 * x + y && in < 2 * x + 2 * y) { //vosiel zlava
                yMap = 2 * x + 2 * y - in - 1;
                xMap = 0;

                while (validField(g, yMap, xMap)) {
                    g.gardenMap[yMap][xMap] = counter;
                    xMap = ++xMap;
                    validGene++;
                    if (blockAhead(g, yMap, xMap)) {
                        changeDirectionUpDown(g, yMap, --xMap);
                        break;
                    }
                }
            }
            if(validGene == 0) genes[i] = -1;
            //System.out.print("Blocked:" + blocked + "\n\n");
            if(blocked){
                break;
            }
        }

        if(write) g.printGarden();

        for(int i = 0; i < y; i++){
            for(int j = 0; j < x; j++){
                if(g.gardenMap[i][j] > 0) fitness++;
            }
        }
        //System.out.print("\nFitness: " + fitness + "\nMax fitness: " + (x*y-numberOfStones));
    }
}
