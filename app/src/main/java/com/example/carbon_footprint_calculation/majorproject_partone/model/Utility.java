package com.example.carbon_footprint_calculation.majorproject_partone.model;


public class Utility {
    private long utilityID;
    private String billType;
    private double billAmount;
    private Double totalEmission;
    private int numPeople;
    private Double emissionPerPerson;
    private double emissionUnit;
    private String billStartDate;
    private String billEndDate;
    private long days;

    public Utility(String type, String start, String end, double amount,int people,long days){
        billType=type;
        billStartDate=start;
        billEndDate=end;
        billAmount=amount;
        numPeople=people;

        this.days = days;
        determineUnit();
        totalEmission = amount*emissionUnit;
        emissionPerPerson = (totalEmission/people)/days;
    }

    //utility
    public Utility(long id, String type, String start, String end,double amount, int people,long days) {
        utilityID = id;
        billType = type;
        billStartDate=start;
        billEndDate=end;
        billAmount = amount;
        numPeople = people;

        this.days = days;
        determineUnit();
        totalEmission = amount*emissionUnit;
        emissionPerPerson = (totalEmission/people)/days;
    }

    //determine unit for calculating emission
    private void determineUnit() {
        if(billType.equals("Electricity")){
            emissionUnit = 0.009;
        }
        else{
            emissionUnit = 56.1;
        }
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public Double getTotalEmission(String unit) {
        if (unit.equals("Kg")) {
            return totalEmission;
        }
        else
            return totalEmission/0.05965;//a tree absorbs as much as 48 pounds/21.7724 kg of CO2 a year, 21.7724/365/24
    }

    public void setTotalEmission(Double totalEmission) {
        this.totalEmission = totalEmission;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public Double getEmissionPerPerson(String unit) {
        if (unit.equals("Kg")) {
            return emissionPerPerson;
        }
        else
            return emissionPerPerson/0.05965;//a tree absorbs as much as 48 pounds/21.7724 kg of CO2 a year, 21.7724/365/24
    }

    public void setEmissionPerPerson(Double emissionPerPerson) {
        this.emissionPerPerson = emissionPerPerson;
    }

    public long getUtilityID() { return utilityID; }

    public String getBillStartDate() {
        return billStartDate;
    }

    public void setBillStartDate(String billStartDate) {
        this.billStartDate = billStartDate;
    }

    public String getBillEndDate() {
        return billEndDate;
    }

    public void setBillEndDate(String billEndDate) {
        this.billEndDate = billEndDate;
    }

    public long getDays() {
        return days;
    }
    public void setDays(long days) {
        this.days = days;
    }
}
