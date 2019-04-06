package pack;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;

public class Evolution {

    private static final int EVOLUTIONTYPE = 2;

    public int numberOfMonks;
    double sum = 0;
    int generationCounter = 0;
    int numberOfStones;
    int x,y;

    /**
     * Konštruktor pre Evolúciu. Potrebujeme ho na vytvorenie inštancie evolúcie pri testovaní.
     * @param numberOfMonks Počet mníchov v jednej generácii.
     * @param numberOfStones Počet kameňov, ktoré bude vytvorená záhrada obsahovať.
     * @param x Označuje šírku záhrady.
     * @param y Označuje dĺžku záhrady.
     */
    public Evolution(int numberOfMonks, int numberOfStones, int x, int y) {
        this.numberOfMonks = numberOfMonks;
        this.numberOfStones = numberOfStones;
        this.x = x;
        this.y = y;
    }

    /**
     * Funkcia na inicializáciu pôvodnej generácie mníchov.
     * Pre každého mnícha z populácie vygeneruje náhodné gény a následne mních pohrabe záhradku.
     * Na základe výslednej hodnoty fitness sú mnísy zoradený a je im vypočítaná ich sila.
     * Následne je vytvorená nová generácia mníchov na základe tejto prvotnej.
     * @throws IOException
     */
    public void initialize() throws IOException{

        Monk[] population = new Monk[numberOfMonks];

        for (int i = 0; i < numberOfMonks; i++) {

            population[i] = new Monk(x,y,numberOfStones);
            population[i].initializeGenes();
            population[i].rakeGarden(false);
            population[i].strength = 0;
        }

        Arrays.sort(population, new SortByFitness());
        findStrength(population);
        newPopulation(population);
    }

    /**
     * Nájde silu pre všetkých jedincov populácie usporiadaných podľa ich fitness funkcie.
     * Pri výpočte sily som sa inšpiroval knihou P. Návrata: Umelá inteligencia, s.350.
     * @param population
     */
    public void findStrength(Monk[] population) {

        sum = 0;
        for (int i = 0; i < numberOfMonks; i++) {
            population[i].strength = ((1 - 0.05) * i + 0.05 - numberOfMonks) / (1 - numberOfMonks);
            sum = sum + population[i].strength;
        }
//
//        for (int i = 0; i < numberOfMonks; i++) {
//            System.out.print("\nFitness: " + population[i].fitness + " Strength: " + population[i].strength + " Genes: ");
//            population[i].printGenes();
//        }
//
    }

    /**
     * Funkcia, ktorá vylepšuje starú generáciu a nahrádza ju novou, vyvynutou generáciou mníchov.
     * Robí to podobne ako funkcia initialize, ale namiesto vygenerovania náhodných génov pre jedincov,
     * volá na základe hodnoty v EVOLUTIONTYPE funkcie evolve, ktoré vylepšujú gény jedincov napríklad kríženiami alebo mutáciami.
     * Následne sa vykoná hrabanie záhradky novou generáciou mnácho, vyhodnotí sa ich fitness a sila a zistí sa, či sa v novej populácii nenašiel mních, ktorý pohrabal celú záhradku.
     * Ak sa našiel, ukončí sa vykonávanie procedúry a prípadne sa vyhodnotia populácie a mních, v opačnom prípade sa opäť zavolá procedúra newPopulation(), ale s novou populáciou ako parametrom.
     * @param population
     * @throws IOException
     */
    public void newPopulation(Monk[] population)  throws IOException {

        int sumFitness = 0;
        Monk[] newPopulation = new Monk[numberOfMonks];

        for (int i = 0; i < numberOfMonks; i++) {

            newPopulation[i] = new Monk(x,y,numberOfStones);
            newPopulation[i].numberOfGenes = newPopulation[i].x + newPopulation[i].y + newPopulation[i].numberOfStones;
            newPopulation[i].genes = new int[newPopulation[i].numberOfGenes];
            newPopulation[i].strength = population[i].strength;
            for (int j = 0; j < population[i].numberOfGenes; j++) {
                Random ran = new Random();
                if( population[i].genes[j] == -1) population[i].genes[j] = ran.nextInt((population[i].x + population[i].y) * 2);
            }
        }

        for (int i = 0; i < numberOfMonks; i++) {
            if(EVOLUTIONTYPE == 1) evolve1(newPopulation[i], i, population);
            else if(EVOLUTIONTYPE == 2) evolve2(newPopulation[i], i, population);
            else if(EVOLUTIONTYPE == 3) evolve3(newPopulation[i], i, population);
            else if(EVOLUTIONTYPE == 4) evolve4(newPopulation[i], i, population);
            newPopulation[i].rakeGarden(false);
        }

        findStrength(newPopulation);
        Arrays.sort(newPopulation, new SortByFitness());

        int best = newPopulation[0].fitness;
        int daco = newPopulation[0].x * newPopulation[0].y - newPopulation[0].numberOfStones;

        for (int i = 0; i < numberOfMonks; i++) {
            population[i] = newPopulation[i];
        }

        for (int i = 0; i < numberOfMonks; i++) {
            sumFitness = sumFitness + population[i].fitness;
        }
       // System.out.print(" Average fitness is " + sumFitness/numberOfMonks + " Max fitness is " + population[0].fitness + "\n");
        //average fitness

        //Evaluate()
        if (best == daco) {
            Monk m = new Monk(x,y,numberOfStones);
            m.numberOfGenes = m.x + m.y + m.numberOfStones;
            m.genes = new int[m.numberOfGenes];

            System.out.print("\n");
            for (int i = 0; i < m.numberOfGenes; i++) {
                System.out.print(newPopulation[0].genes[i] + " ");
                m.genes[i] = newPopulation[0].genes[i];
            }

            String str = (Integer.toString(generationCounter) + "\n");
            FileOutputStream outputStream = new FileOutputStream("evo3000.txt", true);
            byte[] strToBytes = str.getBytes();
            outputStream.write(strToBytes);
            outputStream.close();

            System.out.print("\n + Number of generations: " + generationCounter + "\n");
            m.rakeGarden(true);
            return;
        }
        generationCounter++;
        newPopulation(population);

    }

    public void evolve1(Monk m, int index, Monk[] population) {

        if (index < numberOfMonks / 10) elitism(m, index, population); //0..9
        if (index < numberOfMonks / 5 && index >= numberOfMonks / 10) crossoverRoulette(m, population); //10..19
        if (index < numberOfMonks / 3 && index >= numberOfMonks / 5) crossoverTournament(m, population);//20..32
        if (index < numberOfMonks / 2 && index >= numberOfMonks / 3) mutationComplementTournament(m, population);//33..49
        if (index < numberOfMonks / 1.5 && index >= numberOfMonks / 2) mutationComplementRoulette(m, population);//50..66
        if (index < numberOfMonks && index >= numberOfMonks / 1.5) randomGenes(m);//66..100
    }

    public void evolve2(Monk m, int index, Monk[] population) {

        if (index < numberOfMonks / 10) elitism(m, index, population); //0..9
        if (index < numberOfMonks / 3 && index >= numberOfMonks / 10) crossoverRoulette(m, population); //10..19
        if (index < numberOfMonks / 1.5 && index >= numberOfMonks / 3) mutationComplementRoulette(m, population);//50..66
        if (index < numberOfMonks && index >= numberOfMonks / 1.5) randomGenes(m);//66..100
    }

    public void evolve3(Monk m, int index, Monk[] population) {

        if (index < numberOfMonks / 10) elitism(m, index, population); //0..9
        if (index < numberOfMonks / 3 && index >= numberOfMonks / 10) crossoverTournament(m, population);//20..32
        if (index < numberOfMonks / 1.5 && index >= numberOfMonks / 3) mutationComplementTournament(m, population);//33..49
        if (index < numberOfMonks && index >= numberOfMonks / 1.5) randomGenes(m);//66..100
    }

    public void evolve4(Monk m, int index, Monk[] population) {
        randomGenes(m);
    }

    /**
     * Najlepší jedinci majú právo zotrvať v generácii, tento spôsob sa nazýva elitizmus (Návrat P.: Umelá inteligencia s.349).
     * @param m
     * @param index
     * @param population
     */
    public void elitism(Monk m, int index, Monk[] population) {
        for (int i = 0; i < m.numberOfGenes; i++) {
            m.genes[i] = population[index].genes[i];
        }
    }

    /**
     * Spôsob kríženia, pri ktorom sa pomocou rulety vyberú dvaja rodičia a vypočíta sa limit pre kríženie.
     * Na základe tohto limitu sa vyberú gény pre nového jedinca od prvého rodiča(od začiatku chromozómu po daný limit) a od druhého rodiča(od limitu po koniec chromozómu).
     * Inšpirácie ku kríženiu: Návrat P. - Umelá inteligencia s.348
     * @param m
     * @param population
     */
    public void crossoverRoulette(Monk m, Monk[] population) {
        int genesChangeLimit = (int) (Math.random() * m.numberOfGenes);

        int parent1 = roulette(population);
        int parent2 = roulette(population);

        for (int i = 0; i < genesChangeLimit; i++) {
            m.genes[i] = population[parent1].genes[i];
        }
        for (int i = genesChangeLimit; i < m.numberOfGenes; i++) {
            m.genes[i] = population[parent2].genes[i];
        }
    }

    /**
     * Spôsob kríženia, pri ktorom sa pomocou turnaja vyberú dvaja rodičia a vypočíta sa limit pre kríženie.
     * Na základe tohto limitu sa vyberú gény pre nového jedinca od prvého rodiča(od začiatku chromozómu po daný limit) a od druhého rodiča(od limitu po koniec chromozómu).
     * Inšpirácie ku kríženiu: Návrat P. - Umelá inteligencia s.348
     * @param m
     * @param population
     */
    public void crossoverTournament(Monk m, Monk[] population) {
        int genesChangeLimit = (int) (Math.random() * m.numberOfGenes);

        int parent1 = tournament(population);
        int parent2 = tournament(population);

        for (int i = 0; i < genesChangeLimit; i++) {
            m.genes[i] = population[parent1].genes[i];
        }
        for (int i = genesChangeLimit; i < m.numberOfGenes; i++) {
            m.genes[i] = population[parent2].genes[i];
        }
    }

    /**
     * Spôsob mutácie, pri ktorej sa pomocou turnaja vyberie rodič a gény nového jedinca budú komplementárne ku génom rodiča.
     * Inšpirácie k mutácii: Návrat P. - Umelá inteligencia s.348
     * @param m
     * @param population
     */
    public void mutationComplementTournament(Monk m, Monk[] population) {

        int parent1 = tournament(population);
        for (int i = 0; i < m.numberOfGenes; i++) {
            m.genes[i] = abs(m.numberOfGenes - population[parent1].genes[i]);
        }
    }

    /**
     * Spôsob mutácie, pri ktorej sa pomocou rulety vyberie rodič a gény nového jedinca budú komplementárne ku génom rodiča.
     * Inšpirácie k mutácii: Návrat P. - Umelá inteligencia s.348
     * @param m
     * @param population
     */
    public void mutationComplementRoulette(Monk m, Monk[] population) {

        int parent1 = roulette(population);
        for (int i = 0; i < m.numberOfGenes; i++) {
            m.genes[i] = abs(m.numberOfGenes - population[parent1].genes[i]);
        }
    }

    /**
     * Jedincovi sa nastavia úplne nové náhodné gény.
     * @param m
     */
    public void randomGenes(Monk m) {

        Random ran = new Random();
        for (int i = 0; i < m.numberOfGenes; i++) m.genes[i] = ran.nextInt((m.x + m.y) * 2);
    }

    /**
     * Výber jedinca na kríženie alebo mutáciu pomocou rulety.
     * Najprv sa vypočíta súčet hodnôt sily všetkých jedincov a táto hodnota sa vynásobí náhodnám číslom od 0 po 1.
     * Následne sa postupuje od najslabšieho jedinca a od tejto hodnoty sa odčítava hodnota jeho sily.
     * Keď už je hodnota záporná vyberie sa jedinec, pri ktorom sa stala zápornou.
     * @param population
     * @return Vracia poradové číslo jedinca v populácii.
     */
    public int roulette(Monk[] population) {

        int returnVal = 0;
        double sumSum;
        sumSum = Math.random() * sum; //sumu vsetkych sil vynasobim nahodnym cislom (0,1>

        //postupne odcitavam zo spodu sily az kym nepridem k hladanej a teda aj k danemu genu
        for (int i = numberOfMonks - 1; i >= 0; i--) {
            if (sumSum <= 0) {
                returnVal = i;
                break;
            }
            sumSum = sumSum - population[i].strength;
        }
        return returnVal;
    }

    /**
     * Výber jedinca na kríženie alebou mutáciu pomocou turnaja medzi dvoma náhodne zvolenými jedincami.
     * Vybraný je ten, ktorý má vyššiu hodnotu sily.
     * @param population
     * @return Vracia poradové číslo jedinca v populácii.
     */
    public int tournament(Monk[] population) {

        int number1 = (int) (numberOfMonks * Math.random());
        int number2 = (int) (numberOfMonks * Math.random());

        if (population[number1].strength > population[number2].strength) return number1;
        else return number2;
    }

}
