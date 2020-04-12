package knapsack;

public class Items
{
    private int weight;
    private int benefit;

    public Items(int weight, int benifit)
    {
        this.weight = weight;
        this.benefit = benifit;
    }

    public Items() {
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setBenefit(int benefit) {
        this.benefit = benefit;
    }

    public int getWeight() {
        return weight;
    }

    public int getBenefit() {
        return benefit;
    }
}
