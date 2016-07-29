package edu.gatech.seclass.tccart;

/**
 * Created by jaekyuoh on 3/21/16.
 */
public class Purchase {
    int purchaseId;
    String customerId;
    String date;
    int coffeeCount;
    int teaCount;
    double beforeDiscount;
    double total;
    int creditUsed;
    double creditAmount;
    int vip;
    double vipAmount;

    public Purchase() {
    }

    public Purchase(int purchaseId, String customerId, String date, int coffeeCount, int teaCount,
                    double beforeDiscount, double total, int creditUsed, double creditAmount,
                    int vip, double vipAmount) {
        this.purchaseId = purchaseId;
        this.customerId = customerId;
        this.date = date;
        this.coffeeCount = coffeeCount;
        this.teaCount = teaCount;
        this.beforeDiscount = beforeDiscount;
        this.total = total;
        this.creditUsed = creditUsed;
        this.creditAmount = creditAmount;
        this.vip = vip;
        this.vipAmount = vipAmount;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCoffeeCount() {
        return coffeeCount;
    }

    public void setCoffeeCount(int coffeeCount) {
        this.coffeeCount = coffeeCount;
    }

    public int getTeaCount() {
        return teaCount;
    }

    public void setTeaCount(int teaCount) {
        this.teaCount = teaCount;
    }

    public double getBeforeDiscount() {
        return beforeDiscount;
    }

    public void setBeforeDiscount(double beforeDiscount) {
        this.beforeDiscount = beforeDiscount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCreditUsed() {
        return creditUsed;
    }

    public void setCreditUsed(int creditUsed) {
        this.creditUsed = creditUsed;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public double getVipAmount() {
        return vipAmount;
    }

    public void setVipAmount(double vipAmount) {
        this.vipAmount = vipAmount;
    }
}
