package com.company;

public class FuzzySet {
    String name;
    int crispInput;
    int numberOfMemberFunction;
    Membership_Function [] Membership_Functions;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCrispInput() {
        return crispInput;
    }

    public void setCrispInput(int crispInput) {
        this.crispInput = crispInput;
    }

    public int getNumberOfMemberFunction() {
        return numberOfMemberFunction;
    }

    public void setNumberOfMemberFunction(int numberOfMemberFunction) {
        this.numberOfMemberFunction = numberOfMemberFunction;
    }

    public Membership_Function[] getMembership_Functions() {
        return Membership_Functions;
    }

    public void setMembership_Functions(Membership_Function[] membership_Functions) {
        Membership_Functions = membership_Functions;
    }
}
