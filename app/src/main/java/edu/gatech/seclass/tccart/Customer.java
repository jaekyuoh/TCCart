package edu.gatech.seclass.tccart;

/**
 * Created by jaekyuoh on 3/16/16.
 */
public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int VIPstatus;
    private double credit;
    private double cumulativeCost;
    private String vipLastUpdate;
    private String creditReceivedDate;

    public Customer(){}

    public Customer(String id,String firstName, String lastName, String email, int VIPstatus,
                    double credit, double cumulativeCost, String vipLastUpdate, String creditReceivedDate){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.VIPstatus = VIPstatus;
        this.credit = credit;
        this.cumulativeCost = cumulativeCost;
        this.vipLastUpdate = vipLastUpdate;
        this.creditReceivedDate = creditReceivedDate;

    }
    public boolean isEmpty(){
        if (this.id == ""){
            return true;
        }
        return false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVIPstatus() {
        return VIPstatus;
    }

    public void setVIPstatus(int VIPstatus) {
        this.VIPstatus = VIPstatus;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getCumulativeCost() {
        return cumulativeCost;
    }

    public void setCumulativeCost(double cumulativeCost) {
        this.cumulativeCost = cumulativeCost;
    }

    public String getVipLastUpdate() {
        return vipLastUpdate;
    }

    public void setVipLastUpdate(String vipLastUpdate) {
        this.vipLastUpdate = vipLastUpdate;
    }

    public String getCreditReceivedDate() {
        return creditReceivedDate;
    }

    public void setCreditReceivedDate(String creditReceivedDate) {
        this.creditReceivedDate = creditReceivedDate;
    }
}
