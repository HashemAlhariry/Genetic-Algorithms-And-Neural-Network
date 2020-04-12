package back_propagation;

import java.util.Vector;

public class Training {
    Vector<Double>x;
    Vector<Double>y;

    public Training(Vector<Double> x, Vector<Double> y) {
        this.x = x;
        this.y = y;
    }

    public Training() {
        x=new Vector<>();
        y=new Vector<>();
    }

    public Vector<Double> getX() {
        return x;
    }

    public void setX(Vector<Double> x) {
        this.x = x;
    }

    public Vector<Double> getY() {
        return y;
    }

    public void setY(Vector<Double> y) {
        this.y = y;
    }
}
