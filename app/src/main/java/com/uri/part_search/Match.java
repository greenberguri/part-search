package com.uri.part_search;

/**
 * Created by kstanoev on 1/14/2015.
 */
public class Match {
    //private int voltage, current, power, voutMin;
    private String catNo, description, category, voltage, current, power, voutMin;


    public Match(String catNo, String description) {
        this.setCatNo(catNo);
        this.setDescription(description);
        //this.setCategory(category);
        //this.setVoltage(voltage);
        //this.setCurrent(current);
        //this.setPower(power);
        //this.setVoutMin(voutMin);
    }


    public String getCatNo() {
        return catNo;
    }

    public void setCatNo(String catNo) {
        this.catNo = catNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //public String setCategory() {
        //return category;
    //}

    //public void setCategory(String category) {
    //    this.category = category;
    //}

    //public String getVoltage() {
        //return voltage;
    //}

    //public void setVoltage(String voltage) {
        //this.voltage = voltage;
    //}

    //public String getCurrent() {
        //return current;
    //}

    //public void setCurrent(String current) {
        //this.current = current;
    //}

    //public String getPower() {
        //return power;
    //}

    //public void setPower(String power) {
        //this.power = power;
    //}

    //public String getVoutMin() {
        //return voutMin;
    //}

    //public void setVoutMin(String voutMin) {
        //this.voutMin = voutMin;
    //}
}
