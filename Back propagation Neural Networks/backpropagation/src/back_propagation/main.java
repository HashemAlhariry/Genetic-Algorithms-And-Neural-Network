package back_propagation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class main {
    public static Vector<Training> training_examples;
    public static void main(String[] args) {
        training_examples=new Vector<>();
          readfile();
          for(int i=0;i<training_examples.size();i++){
              System.out.println("new training example");
              for(int j=0;j<training_examples.get(i).getX().size();j++){
                  System.out.print(training_examples.get(i).getX().get(j)+",");
              }
              System.out.println();
              for(int j=0;j<training_examples.get(i).getY().size();j++){
                  System.out.print(training_examples.get(i).getY().get(j)+",");
              }
              System.out.println();
          }
    }
    private static void readfile(){
        try{
        File myObj = new File("train.txt");
        Scanner myReader = new Scanner(myObj);
            String data = myReader.nextLine();
            String[]numbers=data.split(" ");
            int inputNodesNumber=Integer.parseInt(numbers[0]);
            int hiddenNodesNumber=Integer.parseInt(numbers[1]);
            int outputNodesNumber=Integer.parseInt(numbers[2]);
            int number_of_training=Integer.parseInt(myReader.nextLine());
            System.out.println(number_of_training);
            for(int i=0;i<number_of_training;i++){
                Training train=new Training();
                data=myReader.nextLine();
                data.replace("  "," ");
                String[]s=data.split(" ");
                Vector<Double>x=new Vector<>();
                Vector<Double>y=new Vector<>();
                for(int j=0;j<s.length;j++) {
                    s[j].replace(" ", "");
                    if (!s[j].isEmpty()) {
                        if (x.size() < 4) {
                            x.add(Double.parseDouble(s[j]));
                        } else {
                            y.add(Double.parseDouble(s[j]));
                        }
                    }
                }
                train.setX(x);
                train.setY(y);
                training_examples.add(train);
            }
        myReader.close();
    }
    catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
    }
}
