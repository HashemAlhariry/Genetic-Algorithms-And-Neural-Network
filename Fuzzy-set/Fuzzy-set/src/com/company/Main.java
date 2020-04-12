package com.company;
import java.io.*;
import java.util.Arrays;
import java.util.Vector;

public class Main {

    private static  FuzzySet [] fuzzySets;
    private static String [] outRules;
    private static FuzzySet outputSet;
    private static Vector<Vector<FuzzificationOutput>> fuzzifactionOutput;  //same length of fuzzySet


    public static void main(String[] args) throws IOException
    {
      //  prepare();
      //  fuzzification();
      //  printFuzzyOutput();






    }

    private static void prepare() throws IOException
    {
        File file=new File("E:\\FCI\\FourthYear first term\\Genetics Algorithms\\Assignment\\Assignment 3\\Assignment_3\\inputnew.txt");
        BufferedReader br= null;
        br = new BufferedReader(new FileReader(file));
        String st="";
        st=br.readLine();
        int loopNumber=Integer.parseInt(st);
        fuzzySets = new FuzzySet[loopNumber];  //numberOfObjects;
        Membership_Function [] membership_function;
        for(int i =0;i<loopNumber;i++)
        {
            st=br.readLine();
            String[] splitingName= st.split(" ");
            fuzzySets[i]= new FuzzySet();
            fuzzySets[i].setName(splitingName[0]);
            fuzzySets[i].setCrispInput(Integer.parseInt(splitingName[1]));
            st=br.readLine();
            int numberOfMemberFunction=Integer.parseInt(st);
            membership_function= new Membership_Function[numberOfMemberFunction];
            fuzzySets[i].setNumberOfMemberFunction(numberOfMemberFunction);
            for(int j=0;j<numberOfMemberFunction;j++)
            {
                st=br.readLine();
                splitingName= st.split(" ");
                membership_function[j]=new Membership_Function();
                membership_function[j].setName(splitingName[0]);
                membership_function[j].setType(splitingName[1]);

                st=br.readLine();
                splitingName= st.split(" ");
                int[] points = Arrays.stream(splitingName).mapToInt(Integer::parseInt).toArray();   //numberofPoints
                membership_function[j].setPoints(points);


            }

            fuzzySets[i].setMembership_Functions(membership_function);
        }

        printObject(fuzzySets);
        st=br.readLine();
        outputSet = new FuzzySet();
        outputSet.setName(st);
        st=br.readLine();
        int nomf=Integer.parseInt(st);
        String[] splitingName;
        Membership_Function[] object =new Membership_Function[5] ;
        for( int i=0;i<nomf;i++)
        {
            st=br.readLine();
            splitingName= st.split(" ");
            object[i]=new Membership_Function();
            object[i].setName(splitingName[0]);
            object[i].setType(splitingName[1]);

            st=br.readLine();
            splitingName= st.split(" ");
            int[] points = Arrays.stream(splitingName).mapToInt(Integer::parseInt).toArray();   //numberofPoints
            object[i].setPoints(points);

        }
        outputSet.setMembership_Functions(object);

        int numberOfRules=Integer.parseInt(br.readLine());
        outRules=new String [numberOfRules];
        for(int i=0;i<numberOfRules;i++)
        {
            outRules[i]=br.readLine();
        }
    }

    private static void printObject(FuzzySet[] object)
    {
        for(int i=0;i<object.length;i++)
        {
            System.out.println(object[i].getName()+" "+ object[i].getCrispInput());
            Membership_Function [] objects=object[i].getMembership_Functions();
            for(int j=0;j<object[i].getNumberOfMemberFunction();j++)
            {
                Membership_Function obj = objects[j];
                System.out.println(obj.getName()+" "+ obj.getType());
                System.out.println(Arrays.toString(obj.getPoints()));
            }
        }
    }

    private static void printFuzzyOutput()
    {
        for(int i=0;i<fuzzifactionOutput.size();i++)
        {

            for(int j=0;j<fuzzifactionOutput.get(i).size();j++)
            {
                System.out.println(fuzzifactionOutput.get(i).get(j).getFuzzySetName()+" "+fuzzifactionOutput.get(i).get(j).getMembership_FunctionName()+ " "+fuzzifactionOutput.get(i).get(j).getValue());
            }
            System.out.println(" ");
        }

    }


    private static double getEquationOfLine(double x1,double y1,double x2,double y2,double crispPoint)
    {
        double slope = (y2 - y1)/( x2 - x1);
        double B = y1 - (slope * x1);
        double yOut=(slope*crispPoint)+B;


        return yOut;
    }
    private static void fuzzification()
    {
        fuzzifactionOutput= new Vector<>();
        for (int i=0;i<fuzzySets.length;i++)
        {

            String fuzzySetName= fuzzySets[i].getName();

            Vector<FuzzificationOutput>FuzzyOutput= new Vector<>();
            int crispInput = fuzzySets[i].getCrispInput();
            Membership_Function [] membership_functions=fuzzySets[i].getMembership_Functions();

            for(int j=0;j<membership_functions.length;j++)
            {
                String nameOfMembership= membership_functions[j].getName();
                int index=-1;
                int []points=membership_functions[j].getPoints();
                for(int k=0;k<points.length-1;k++)
                {
                    if(crispInput>=points[k]&& crispInput<=points[k+1])
                    {
                        index=k;
                        break;
                    }
                }
                if(index==-1)
                {

                    FuzzyOutput.add(new FuzzificationOutput(fuzzySetName,nameOfMembership,0.0));
                }
                else
                {
                    int x1=points[index],x2=points[index+1];
                    int y1=0,y2=0;
                    if(membership_functions[j].getType().equals("triangle"))
                    {

                        if(index==0) {
                            y1 = 0;
                            y2 = 1;
                        }
                        else
                        {
                            y1 = 1;
                            y2 = 0;
                        }
                    }
                    else{
                        if(index==0)
                        {
                            y1 = 0;
                            y2 = 1;
                        }
                        else if (index==1)
                        {
                            y1 = 1;
                            y2 = 1;
                        }
                        else
                        {
                            y1 = 1;
                            y2 = 0;
                        }
                    }

                    FuzzyOutput.add( new FuzzificationOutput(fuzzySetName,nameOfMembership,getEquationOfLine(x1,y1,x2,y2,crispInput)));
                }

            }
            fuzzifactionOutput.add(FuzzyOutput);  //add answer each of MembershipFunction

        }

    }

    private static void splitString (String rule)
    {

        String [] arrOfRules = rule.split("then");

       // int numberOfRules = Integer.parseInt(String.valueOf(arrOfRules[0].charAt(0)));
       // System.out.println(numberOfRules);

        boolean checkIfAnd=arrOfRules[0].contains("AND");
        Vector<Double> numbersForAnd;
        Vector<Double> numbersForOR;

        if(checkIfAnd)
        {
            String [] arrayOfAnd= arrOfRules[0].split("AND");
            numbersForAnd=new Vector<Double>();
            for(int i=0;i<arrayOfAnd.length;i++)
            {
                if(arrayOfAnd[i].contains("OR"))
                {
                    String []arrayOfOr =arrayOfAnd[i].split("OR");
                    numbersForOR=new Vector<>();
                        for(int j=0;j<arrayOfOr.length;j++){
                        numbersForOR.add(calculateString(arrayOfOr[j]));
                    }
                    numbersForOR.sort(Double::compareTo);
                    numbersForAnd.add(numbersForOR.get(numbersForOR.size()-1));
                }
                else
                {

                    numbersForAnd.add(calculateString(arrayOfAnd[i]));
                }

            }
            numbersForAnd.sort(Double::compareTo);
            Double ruleanswer=numbersForAnd.get(0);


        }





    }
    private static double calculateString(String rule)
    {
        String [] split = rule.split("=");
        for(int i=0;i<fuzzifactionOutput.size();i++)
        {
            for(int j=0;j<fuzzifactionOutput.get(i).size();j++)
            {
                if(split[0].equals(fuzzifactionOutput.get(i).get(j).getFuzzySetName()) && split[1].equals(fuzzifactionOutput.get(i).get(j).getMembership_FunctionName()))
                {
                    return fuzzifactionOutput.get(i).get(j).getValue();

                }
            }
        }
          return 123456789;

    }




}


