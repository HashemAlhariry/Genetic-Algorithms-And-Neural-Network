package knapsack;

public class Test_Case {
    int number_of_items;
    int size_of_knapsack;
    Items [] items;

    public Test_Case(int number_of_items, int size_of_knapsack) {
        this.number_of_items = number_of_items;
        this.size_of_knapsack = size_of_knapsack;
        items=new Items[number_of_items];
    }

    public Test_Case(int number_of_items, int size_of_knapsack, Items[] items) {
        this.number_of_items = number_of_items;
        this.size_of_knapsack = size_of_knapsack;
        this.items = items;
    }

    public int getNumber_of_items() {
        return number_of_items;
    }

    public void setNumber_of_items(int number_of_items) {
        this.number_of_items = number_of_items;
    }

    public int getSize_of_knapsack() {
        return size_of_knapsack;
    }

    public void setSize_of_knapsack(int size_of_knapsack) {
        this.size_of_knapsack = size_of_knapsack;
    }

    public Items[] getItems() {
        return items;
    }

    public void setItems(Items[] items) {
        this.items = items;
    }
}
