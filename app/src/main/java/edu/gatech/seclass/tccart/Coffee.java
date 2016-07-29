package edu.gatech.seclass.tccart;

/**
 * Created by jaekyuoh on 3/19/16.
 */
public class Coffee extends Item {
    private String name;
    private double price;

    public Coffee (){
        this.name = "Coffee";
        this.price = 2.50;
    }
    public Coffee(String name, double price){
        this.name = name;
        this.price = 2.50;
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
