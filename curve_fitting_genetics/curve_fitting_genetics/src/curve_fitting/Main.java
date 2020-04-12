package curve_fitting;


import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main
{
    static ArrayList<Case> cases;
    static ArrayList<double[]> generation;
    private static double pm=0.001;
    private static double pc=0.8;
    private static int numberOfChromosomes=50;
    private static int numberofiterations=1000;
    static double[]fitnessarray;
    static String output="";

    public static void main(String[] args)
    {
         cases=readInputFromFile();  //Read from FILE.

        // loop for the rest testcases
        for(int casee=0;casee<cases.size();casee++){


            createGeneration(numberOfChromosomes, cases.get(casee).getDegree() + 1);  //Generations created with numberOfChromosomes and degree from FILE
            double[] x = new double[cases.get(casee).getNumber_of_points()];
            double[] y = new double[cases.get(casee).getNumber_of_points()];
            x=cases.get(casee).getX();
            y=cases.get(casee).getY();
            fitnessarray = new double[numberOfChromosomes];

            //saving the best chromosome
            double[] bestchromo = null;
            double bestfitness = Double.MAX_VALUE;
            for (int iter = 0; iter < numberofiterations; iter++) {
                for (int i = 0; i < generation.size(); i++) {
                    fitnessarray[i] = fitnessFunction(generation.get(i), x, y);
                }
                double[] bestchromoingenration = bestChromosome(generation, x, y);
                double fitness = fitnessFunction(bestchromoingenration, x, y);
                if (fitness < bestfitness) {
                    bestchromo = bestchromoingenration;
                    bestfitness = fitnessFunction(bestchromoingenration, x, y);
                }
                //select
                select3(x, y, iter);
                //select2(x,y,iter,bestchromo,bestfitness);
                for (int i = 0; i < generation.size(); i++) {
                    double calculatedMeanSquareError = fitnessFunction(generation.get(i), x, y);
                    //System.out.println("Fitness of " + i + " Chromosome after CO = " + calculatedMeanSquareError + ","+"chromo:"+generation.get(i));
                }
            }
            System.out.println("coefficients are:");
            for (int i = 0; i < bestchromo.length; i++) {
                System.out.println(bestchromo[i]);
            }
            System.out.println("best fitness is " + bestfitness);
            output+="case number "+(casee+1)+'\n';
            for(int i=0;i<bestchromo.length;i++){
                output+=Double.toString(bestchromo[i]);
                if(i!=bestchromo.length-1){
                    output+=",";
                }
            }
            output+='\n';
            output+="best fitness is:"+bestfitness;
            output+="------------------------------------------------------------------------------------------------"+'\n';
        }
        writeResultToFile(output);

    }
    public static void select3(double []x,double []y,int iter)
    {
        ArrayList<double []> tempForNewGeneration=new ArrayList<>(numberOfChromosomes);
        double [] fitnessForChromosomes = new double[numberOfChromosomes];
        for (int i = 0; i < generation.size(); i++)
        {
            double calculatedMeanSquareError = fitnessFunction(generation.get(i), x, y);
            fitnessForChromosomes[i]=calculatedMeanSquareError;
            //System.out.print("Fitness of " + i + " Chromosome after CO & Mutation= " + calculatedMeanSquareError + ","+"chromo= "+generation.get(i));
        }
        for(int i=0;i<(generation.size()-1);i+=2)
        {
            double temp [] = generation.get(i);
            if(fitnessarray[i]<fitnessarray[i+1])
            {
                tempForNewGeneration.add(generation.get(i));
            }
            else
            {
                tempForNewGeneration.add(generation.get(i+1));
            }
        }
        for (int i = 0; i < (tempForNewGeneration.size()/2); i += 2) {
            ArrayList<double[]> newOffspring = singlePointCrossOver(tempForNewGeneration.get(i), tempForNewGeneration.get(i + 1), pc);
            if(Math.random()<=0.5) {
                tempForNewGeneration.add(newOffspring.get(0));
                tempForNewGeneration.add(newOffspring.get(1));
            }
            else
            {
                tempForNewGeneration.add(newOffspring.get(1));
                tempForNewGeneration.add(newOffspring.get(0));
            }
        }
        for (int i = 0; i < tempForNewGeneration.size(); i++)
        {
            double rand = Math.random();
            //mutate
            double temp_chromosome [];
            temp_chromosome = tempForNewGeneration.get(i);
            int generateRandIndex = (int) Math.random() * (temp_chromosome.length - 1);
            temp_chromosome[generateRandIndex] = nonUniformMutation(temp_chromosome[generateRandIndex],iter+1, numberofiterations, 2);
            tempForNewGeneration.set(i, temp_chromosome);
        }
        generation=tempForNewGeneration;
    }

    private static void select2(double []x,double[]y,int iter,double[]bestchromo,double bestfitness)
    {
        ArrayList<double []> tempForNewGeneration=new ArrayList<>();
        double [] fitnessForChromosomes = new double[numberOfChromosomes];
        for (int i = 0; i < generation.size(); i++)
        {
            double calculatedMeanSquareError = fitnessFunction(generation.get(i), x, y);
            fitnessForChromosomes[i]=calculatedMeanSquareError;
            //System.out.print("Fitness of " + i + " Chromosome after CO & Mutation= " + calculatedMeanSquareError + ","+"chromo= "+generation.get(i));
        }
        for(int i=0;i<generation.size()/2;i++)
        {
            tempForNewGeneration.add(rouletteWheel(generation,fitnessForChromosomes));
        }
        for (int i = 0; i < (tempForNewGeneration.size()/2); i += 2) {
            ArrayList<double[]> newOffspring = singlePointCrossOver(generation.get(i), generation.get(i + 1), pc);
            tempForNewGeneration.add(newOffspring.get(0));
            tempForNewGeneration.add(newOffspring.get(1));
        }
        double [] temp_chromosome;
        for (int i = 0; i < tempForNewGeneration.size(); i++) {
            double rand = Math.random();
                //mutate
                temp_chromosome = tempForNewGeneration.get(i);
                int generateRandIndex = (int) Math.random() * (temp_chromosome.length - 1);
                temp_chromosome[generateRandIndex] = nonUniformMutation(temp_chromosome[generateRandIndex],iter+1, numberofiterations, 5);
                tempForNewGeneration.set(i, temp_chromosome);
        }
        double worstfitness=0;
        double[]worstchromo=tempForNewGeneration.get(0);
        for(int i=0;i<tempForNewGeneration.size();i++){
            double fit=fitnessFunction(tempForNewGeneration.get(i),x,y);
            if(fit>worstfitness){
                worstchromo=tempForNewGeneration.get(i);
                worstfitness=fit;
            }
        }
        if(bestfitness<worstfitness){
            tempForNewGeneration.remove(worstchromo);
            tempForNewGeneration.add(bestchromo);
        }
        generation=tempForNewGeneration;
    }

    private static void select1(double []x,double[]y,int iter){
        /*** Single Point CrossOver for the generation , First with Second , Third with Fourth ***/

        for (int i = 0; i < (generation.size()/2); i += 2) {
            ArrayList<double[]> newOffspring = singlePointCrossOver(generation.get(i), generation.get(i + 1), pc);
            generation.set(i, newOffspring.get(0));
            generation.set((i + 1), newOffspring.get(1));
        }
        /*** MUTATION HERE ***/

        double[] minChromosome;
        double [] temp_chromosome;
        for (int i = 0; i < generation.size(); i++) {
                //mutate
                temp_chromosome = generation.get(i);
                int generateRandIndex = (int) Math.random() * (temp_chromosome.length - 1);
                temp_chromosome[generateRandIndex] = nonUniformMutation(temp_chromosome[generateRandIndex],iter+1, numberofiterations, 5);
                generation.set(i, temp_chromosome);
        }
        minChromosome = bestChromosome(generation, x, y);

        ArrayList<double []> tempForNewGeneration=new ArrayList<>();
        tempForNewGeneration.add(minChromosome);


        double [] fitnessForChromosomes = new double[numberOfChromosomes] ;
        for (int i = 0; i < numberOfChromosomes; i++)
        {
            double calculatedMeanSquareError = fitnessFunction(generation.get(i), x, y);
            fitnessForChromosomes[i]=calculatedMeanSquareError;
            //System.out.print("Fitness of " + i + " Chromosome after CO & Mutation= " + calculatedMeanSquareError + ","+"chromo= "+generation.get(i));
        }
        for(int i=1;i<generation.size();i++)
        {

            tempForNewGeneration.add(rouletteWheel(generation,fitnessForChromosomes));

        }
        generation.clear();
        generation=tempForNewGeneration;
    }
    private static double [] bestChromosome (ArrayList<double []> gen,double []x,double []y)
    {
        int index=0;
        double maximum = Double.MAX_VALUE;
        for(int i =0 ;i<generation.size();i++)
        {
            double [] temp = generation.get(i);
            if(fitnessFunction(temp,x,y)<maximum)
            {
                maximum=fitnessFunction(temp,x,y);
                index=i;
            }
        }

        return generation.get(index);
    }
    private static double nonUniformMutation(double x,double currentGenration,double maximumNumber,int dependecyFactor)
    {
        double rand=Math.random();
        double deltLeft= x-(-10);
        double deltRight= 10- x;
        double delta ;

        if(rand<=0.5)
        {
            double temp=Math.pow((1-(currentGenration/maximumNumber)),dependecyFactor);
            rand=Math.random();
             delta = (deltLeft  * (1-(Math.pow(rand,temp))));
             return x -(Math.random() * delta);
        }
        else
        {
            double temp=Math.pow((1-(currentGenration/maximumNumber)),dependecyFactor);
            rand=Math.random();
             delta =  (deltRight*(1-(Math.pow(rand,temp))));
            return x +( Math.random() * delta);
        }


    }

    /*** INITIAL GENERATION CREATED ***/
    private static void createGeneration(int numberOfChromosomes,int numberOfCoeff)
    {
        generation=new ArrayList<>();

        for(int i=0;i<numberOfChromosomes;i++)
        {
            double [] chromosome= new double[numberOfCoeff];

            for(int j=0;j<numberOfCoeff;j++)
            {
                double start = -10;
                double end = 10;
                double random = new Random().nextDouble();
                chromosome[j] = start + (random * (end - start));
            }
            generation.add(i,chromosome);
        }
    }

    /*** MEAN square error for chromosome on array of given points ***/

    private static double fitnessFunction(double[] chromosome,double [] x,double [] y)
    {
        double [] yCalculated= new double[y.length];
        double n=1.0/(y.length);
        double meanSquareError=0;
        for(int j =0;j<x.length;j++)
        {
            yCalculated[j]=0;
            for (int i = 0; i < chromosome.length; i++)
            {
                yCalculated[j] += chromosome[i] * (Math.pow(x[j], i));
            }
            double temp=yCalculated[j]-y[j];
            meanSquareError+=Math.pow(temp,2);

        }
        meanSquareError*=n;

        return meanSquareError;
    }


    private static double [] rouletteWheel(ArrayList<double[]>generation ,double[] fitness)
    {
            double []fitness_new= new double[fitness.length];
            int total=0;
            for(int i=0;i<fitness.length;i++)
            {
                fitness_new[i]=10000-fitness[i];
                total+=fitness_new[i];
            }

            int  counter=0;
            double random_number=(total)*(Math.random());

            for(int i=0;i<fitness_new.length;i++)
            {
                if(random_number<=(fitness_new[i]+counter))
                    return generation.get(i);
                else
                    counter+=fitness_new[i];

            }

            return generation.get(0);

    }


    /** SinglePointCrossOver method after x bits **/
    private static ArrayList<double[]> singlePointCrossOver(double [] firstOne,double [] secondOne,double pc)
    {

        int afterHowManyBits = (int) ((firstOne.length - 2) * Math.random() + 1);
        double random_number = Math.random();

        ArrayList<double[]> newOffsprings = new ArrayList<>();

        int lengthofchromosome = firstOne.length;
        if (random_number <= pc) {
            double[] first = new double[lengthofchromosome];
            double[] second = new double[lengthofchromosome];

            for (int i = 0; i < lengthofchromosome; i++) {
                if (i < afterHowManyBits) {
                    first[i] = firstOne[i];
                } else {
                    second[i] = firstOne[i];
                }

            }
            for (int i = 0; i < lengthofchromosome; i++) {
                if (i < afterHowManyBits) {
                    second[i] = secondOne[i];
                } else {
                    first[i] = secondOne[i];
                }

            }
            newOffsprings=new ArrayList<>();
            newOffsprings.add(first);
            newOffsprings.add(second);
        }
        else
        {
            newOffsprings=new ArrayList<>();
            newOffsprings.add(firstOne);
            newOffsprings.add(secondOne);


        }
        return  newOffsprings;
    }


     /**reads input file tested**/
    private static ArrayList<Case> readInputFromFile() {
        File file = new File("input-2.txt");
        ArrayList<Case> cases = new ArrayList<>();
        int number_of_cases = 0;
        int number_of_points = 0;
        int degree = 0;
        if (file.exists()) {
            Scanner reader = null;
            try {
                reader = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            number_of_cases = Integer.parseInt(reader.nextLine());
            for (int i = 0; i < number_of_cases; i++) {
                Case c = new Case();
                number_of_points = Integer.parseInt(reader.next());
                c.setNumber_of_points(number_of_points);
                degree = reader.nextInt();
                c.setDegree(degree);
                double[]x=new double[number_of_points];
                double[]y=new double[number_of_points];
                for (int j = 0; j < number_of_points; j++) {
                    x[j] = Double.parseDouble(reader.next());
                    y[j] = Double.parseDouble(reader.nextLine());
                }
                c.setX(x);
                c.setY(y);
                cases.add(c);
            }
            return cases;
        }
        System.out.println("file doesn't exist");
        return null;
    }

    /**write the final coefficients to file**//**not completed**/
    private static void writeResultToFile(String out){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("output"));
       writer.write(output);
       writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return;


    }



}
