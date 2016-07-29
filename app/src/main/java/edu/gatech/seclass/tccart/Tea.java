package edu.gatech.seclass.tccart;

/**
 * Created by jaekyuoh on 3/19/16.
 */
public class Tea extends Item {
    private String name;
    private double price;

    public Tea (){
        this.name = "Tea";
        this.price = 2.0;
    }
    public Tea(String name, double price){
        this.name = "Tea";
        this.price = 2.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
