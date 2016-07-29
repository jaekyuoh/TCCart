package edu.gatech.seclass.tccart;

import java.util.ArrayList;

/**
 * Created by jaekyuoh on 3/17/16.
 */
public class CustomerList {
    DBManager myDb;
    ArrayList<Customer> customers;
    public CustomerList(ArrayList<Customer> customers){
        this.customers = customers;
    }

    public void addCustomer(Customer customer){

        customers.add(customer);
    }

    public void getUsersFromDb(){

    }
}
