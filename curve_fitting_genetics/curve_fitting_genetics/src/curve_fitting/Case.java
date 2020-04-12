package curve_fitting;

import java.util.ArrayList;

public class Case {
    private int degree;
    private int number_of_points;
    private double[]x;
    private double[]y;

    public Case() {
        this.degree=0;
        this.number_of_points=0;
        this.x=new double[number_of_points];
        this.y=new double[number_of_points];
    }

    public Case(int degree, int number_of_points ,double[]x,double[]y) {
        this.degree = degree;
        this.number_of_points = number_of_points;
        this.x=x;
        this.y=y;
    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public Case(int degree, int number_of_points) {
        this.degree = degree;
        this.number_of_points = number_of_points;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getNumber_of_points() {
        return number_of_points;
    }

    public void setNumber_of_points(int number_of_points) {
        this.number_of_points = number_of_points;
    }
}
