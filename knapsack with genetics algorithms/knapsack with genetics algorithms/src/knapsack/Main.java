package knapsack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main
{
    static int number_of_population=1000;
    static int iterations;
    static double pm=0.2;
    static double pc=0.9;
    static ArrayList<String>generationn;
    public static void main(String[] args) throws FileNotFoundException {
       genetics_algorithm_knapsack();
    }
    private static void genetics_algorithm_knapsack() throws FileNotFoundException {

        //read input
        Test_Case [] testcases;
        testcases=readInputFile();
        if(testcases==null)
            return;
        //loop on test cases
       for(int i=0;i<testcases.length;i++)
       {
            int max_fit=0;
            generationn=new ArrayList<>();
            //init population
            generate_population(testcases[i].getNumber_of_items());
            String max_chromosome=generationn.get(0);
            iterations=10000;
             do {
                 int total_sum_of_fitnesses=0;
                //calculate fitness
                int[] fitness_array = new int[generationn.size()];
                for (int j = 0; j < generationn.size(); j++){
                    fitness_array[j] = fitness_function(generationn.get(j), testcases[i].getItems());
                    total_sum_of_fitnesses+=fitness_array[j];
                    if(fitness_array[j]>=max_fit && checkweightofchromosome(generationn.get(j),testcases[i].getSize_of_knapsack(),testcases[i].getItems()))
                    {
                        max_fit=fitness_array[j];
                        max_chromosome=generationn.get(j);
                    }
                }
                //select
                select(testcases[i].getItems(),total_sum_of_fitnesses,fitness_array);
                int random_index= (int) ((generationn.size()-1)*Math.random());
                generationn.set(random_index,max_chromosome);
                //check termination condition
                iterations--;
             }
            while(iterations!=0);//repeat
           System.out.println("result of case number "+(i+1)+" is "+max_fit);
           //System.out.println("maximum chromosome is "+max_chromosome);
           System.out.println("------------------------------------------------------------------------");
        }
    }
    private static String [] removeString(String[]generation,String chromosome){
        String[]newgeneration=new String[generation.length-1];
        for(int i=0,j=0;i<generation.length && j<newgeneration.length;i++,j++){
            if(!generation[i].equals(chromosome)){
                newgeneration[j]=generation[i];
            }
            else{
                j--;
            }
        }
        return newgeneration;
    }
    private static void generate_population(int number_of_items)
    {
        generationn=new ArrayList<>(number_of_population);
        char one='1';
        char zero='0';
        for(int i=0;i<number_of_population;i++) {
             String chromosome="";
            for(int j=0;j<number_of_items;j++){
                double random_number=Math.random();

                if(random_number>=0.5)
                {
                    chromosome+=one;
                }
                else
                {
                    chromosome+=zero;
                }


            }
            generationn.add(chromosome);
        }

    }
     //tournament selection function makes tournamnet between each
    //2 chromosomes and the one with the highest fitness wins
    private static ArrayList<String> tournament_Selection(int[]fitnesses,ArrayList<String>generation){
        ArrayList<String>selected=new ArrayList<>();
        for(int i=0;i<generation.size()-1;i+=2){
               if(fitnesses[i]>=fitnesses[i+1])
                   selected.add(generation.get(i));
               else{
                   selected.add(generation.get(i+1));
               }
        }



        return selected;
    }
    /** fitness function return the summation of items accepted in the knapsack **/
    private static int fitness_function(String chromosome ,Items[] items)
    {
        int fitness=0;
        for(int i=0;i<items.length;i++)
        {
            if(chromosome.charAt(i)=='1')
            {
                fitness+=items[i].getBenefit();
            }

        }
        return fitness;
    }


    /** evaluate the fitness function if its feasible for the bag **/
    private static boolean checkweightofchromosome(String chromosome,int weightOfTheBag,Items[] items)
    {
        int weight=0;
        for(int i=0;i<items.length;i++){
            if(chromosome.charAt(i)=='1')
                weight+=items[i].getWeight();
        }
        return (weightOfTheBag>=weight);
    }
    /** Single CrossOver method after x bits **/
    private static String [] singlePointCrossOver(String firstOne,String secondOne,double pc)
    {
        int afterHowManyBits=(int)((firstOne.length()-2)*Math.random()+1);
        double random_number=Math.random();

        String[] newOffsprings= new String[2];

        int lengthofstring=firstOne.length();
        if(random_number<=pc) {
            newOffsprings[0] = firstOne.substring(0, afterHowManyBits) + secondOne.substring(afterHowManyBits, lengthofstring);
            newOffsprings[1] = secondOne.substring(0, afterHowManyBits) + firstOne.substring(afterHowManyBits, lengthofstring);
        }
        else{
            newOffsprings[0]=firstOne;
            newOffsprings[1]=secondOne;
        }
        return newOffsprings;
    }

    /** Mutation method**/
    private static String mutation(String chromosome,double pm){
        double random_number=Math.random();
        int random_index;
        if(random_number<=pm){
            random_index= (int)((chromosome.length()-1)*Math.random());
            chromosome=flipbit(chromosome,random_index);
        }
        return chromosome;
    }
    private static String flipbit(String chromosome,int index){
        String new_chromosome="";
        String first_substring=chromosome.substring(0,index);
        String second_substring=chromosome.substring(index+1,chromosome.length());
        if(chromosome.charAt(index)=='1'){
            new_chromosome=first_substring+'0'+second_substring;
        }
        else{
            new_chromosome=first_substring+'1'+second_substring;
        }
        return new_chromosome;

    }


    /** Return String from random number according to the Roulette wheel fitness For Each chromosome array corresponding to the
     * generation array so if the the fitness For Each chromosome[x] so the returned value generation[x]
     * @param items array of objects each with weight and benefit
     * @param total_sum is the total sum of fitnesses of generation
     * @param fitnessForEachchromosome int array of all fitnesses of generation
     * @return the chosen String based on roulette Wheel Selection
     */

    private static String rouletteWheelSelection(Items[] items,int total_sum,int []fitnessForEachchromosome)
    {
        int counter=0;
        double random_number=total_sum*Math.random();

        for(int i=0;i<generationn.size();i++)
        {
            if(random_number<=(fitnessForEachchromosome[i]+counter))
                return generationn.get(i);
            else
                counter+=fitnessForEachchromosome[i];

        }
        return "";
    }

    // read input from file
    // tested
    private static Test_Case[] readInputFile() throws FileNotFoundException {
        File file = new File("input_example.txt");
        int number_of_testcases = 0;
        int number_of_items = 0;
        int size_of_knapsack = 0;
        Test_Case[] testcases;
        Items[] items;
        if (file.exists()) {
            Scanner reader = new Scanner(file);
            number_of_testcases = Integer.parseInt(reader.nextLine());
            reader.nextLine();
            testcases = new Test_Case[number_of_testcases];
            for (int i = 0; i < number_of_testcases; i++) {
                number_of_items = Integer.parseInt(reader.nextLine());
                size_of_knapsack = Integer.parseInt(reader.nextLine());
                items = new Items[number_of_items];
                for (int j = 0; j < number_of_items; j++) {
                    items[j] = new Items();
                    items[j].setWeight(Integer.parseInt(reader.next()));
                    items[j].setBenefit(Integer.parseInt(reader.next()));
                }
                testcases[i] = new Test_Case(number_of_items, size_of_knapsack, items);
                if (reader.hasNext()) {
                    reader.nextLine();
                    reader.nextLine();
                }
            }
            return testcases;
        } else {
            System.out.println("file doesn't exist");
        }
        return null;
    }

    private static void select(Items[]items,int total_sum,int[]fitnesses){
        ArrayList<String>newgeneration=new ArrayList<>();
        for(int i=0;i<generationn.size()/2;i++){
                String select=rouletteWheelSelection(items,total_sum,fitnesses);
                newgeneration.add(select);
        }
        for(int i=0;i<newgeneration.size()/2;i+=2){
            String offsprings[];
            offsprings=singlePointCrossOver(newgeneration.get(i),newgeneration.get(i+1),pc);
            offsprings[0]=mutation(offsprings[0],pm);
            offsprings[1]=mutation(offsprings[1],pm);
            newgeneration.addAll(Arrays.asList(offsprings));
        }
       generationn=newgeneration;
    }

}
