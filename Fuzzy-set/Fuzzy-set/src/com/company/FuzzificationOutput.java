package com.company;

public class FuzzificationOutput
{
    String fuzzySetName;
    String membership_FunctionName;
    double value;

    public String getFuzzySetName() {
        return fuzzySetName;
    }

    public void setFuzzySetName(String fuzzySetName) {
        this.fuzzySetName = fuzzySetName;
    }

    public String getMembership_FunctionName() {
        return membership_FunctionName;
    }

    public void setMembership_FunctionName(String membership_FunctionName) {
        this.membership_FunctionName = membership_FunctionName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public FuzzificationOutput(String fuzzySetName, String membership_FunctionName, double value) {
        this.fuzzySetName = fuzzySetName;
        this.membership_FunctionName = membership_FunctionName;
        this.value = value;
    }
}
