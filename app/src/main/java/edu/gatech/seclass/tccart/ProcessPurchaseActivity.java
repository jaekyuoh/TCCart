package edu.gatech.seclass.tccart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.gatech.seclass.services.CreditCardService;
import edu.gatech.seclass.services.EmailService;
import edu.gatech.seclass.services.PaymentService;
import edu.gatech.seclass.services.QRCodeService;

public class ProcessPurchaseActivity extends AppCompatActivity {
    LinearLayout processPurchaseLayout;
    ScrollView processPurchaseScrollView;
    EditText editTextPurchaseID;
    EditText numberOfTea;
    EditText numberOfCoffee;
    Coffee coffee;
    Tea tea;
    Date today;
    DBManager myDb;
    String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        processPurchaseScrollView = (ScrollView) findViewById(R.id.processPurchaseScrollView);
        processPurchaseScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        myDb = new DBManager(this);

        numberOfCoffee = (EditText) findViewById(R.id.editTextNumberOfCoffee);
        numberOfTea = (EditText) findViewById(R.id.editTextNumberOfTea);
        editTextPurchaseID = (EditText) findViewById(R.id.editTextPurchaseID);

        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        today = stringToDate(dateStr);
        String dateStr2 = dateToString(today);
        //Coffee coffee = new Coffee();
        //Tea tea = new Tea();
        //Toast.makeText(ProcessPurchaseActivity.this, "Item name: " + coffee.getName()+ "\n"+ " Item Price: " + coffee.getPrice(), Toast.LENGTH_LONG).show();
//        Toast.makeText(ProcessPurchaseActivity.this, "String : " + dateStr, Toast.LENGTH_LONG).show();
//        Toast.makeText(ProcessPurchaseActivity.this, "Date : " + today, Toast.LENGTH_LONG).show();
//        Toast.makeText(ProcessPurchaseActivity.this, "String : " + dateStr2, Toast.LENGTH_LONG).show();


    }

    public void onClick_scanQRcode(View view){
        customerId = QRCodeService.scanQRCode(); // using QRcode
        editTextPurchaseID.setText(customerId);
        if(customerId.equals("ERR")){
            showMessage("Scan Error","Please scan QR code again.");
            editTextPurchaseID.setText("");
        }
        Toast.makeText(ProcessPurchaseActivity.this, "Id from QRcode: " + customerId, Toast.LENGTH_LONG).show();
    }

    public void onClick_purchaseItems(View view){
        customerId = editTextPurchaseID.getText().toString();
        String numCoffeeStr = numberOfCoffee.getText().toString();
        String numTeaStr = numberOfTea.getText().toString();
        int numCoffee;
        int numTea;
        double totalAmount = 0;
        double beforeCreditAmount = 0;
        double vipDiscount = 0;
        Coffee coffee = new Coffee();
        Tea tea = new Tea();

        int creditUsed = 0;
        double creditAmount = 0;
        int vipStatus = 0;
        //PURCHASE
        if((customerId.equals("")) || ((numCoffeeStr.equals(""))&&(numTeaStr.equals(""))) ){
            if(customerId.equals("")){
                showMessage("Not enough Information","Specify customer");
                editTextPurchaseID.setText("");
                numberOfCoffee.setText("");
                numberOfTea.setText("");

            }
            else if (((numCoffeeStr.equals(""))&&(numTeaStr.equals("")))){
                showMessage("Not enough Information","Specify number of items customer is buying");
                editTextPurchaseID.setText("");
                numberOfCoffee.setText("");
                numberOfTea.setText("");

            }

            else {
                showMessage("Not enough Information", "Fill in all required information");
                editTextPurchaseID.setText("");
                numberOfCoffee.setText("");
                numberOfTea.setText("");

            }
        }
        else {
            // Check if customer is in DB
            boolean duplicate = myDb.duplicateID(customerId);
            if (duplicate){
                //Fix empty quantity bug...Make empty string = 0
                if(numCoffeeStr != null && !numCoffeeStr.isEmpty()) {
                    numCoffee = Integer.parseInt(numCoffeeStr);
                }
                else {
                    numCoffee = 0;
                }
                if(numTeaStr != null && !numTeaStr.isEmpty()) {
                    numTea = Integer.parseInt(numTeaStr);
                }
                else{
                    numTea = 0;
                }
                if((numCoffee == 0)&&(numTea == 0)){
                    showMessage("No Item Selected", "Customer is buying Nothing");
                    editTextPurchaseID.setText("");
                    numberOfCoffee.setText("");
                    numberOfTea.setText("");
                }
                else {
                    //customer exist
                    Customer customer = myDb.retrieveUserInfo(customerId);
                    beforeCreditAmount = (numCoffee * coffee.getPrice()) + (numTea * tea.getPrice());
                    // check whether customer is VIP or not
                    vipStatus = customer.getVIPstatus();
                    vipDiscount = beforeCreditAmount * customer.getVIPstatus() * 0.1;
                    //check whether user has any credit, and if he has it then minus remaining credit amount from total cost
                    creditAmount = customer.getCredit();
                    double usedCredit = 0;
                    //Need to check whether credit is bigger than beforeCreditAmount - vipDiscount, if it is then update reduced amount of credit
                    double remainingCredit;
                    if ((beforeCreditAmount - vipDiscount) < creditAmount) {
                        remainingCredit = creditAmount - (beforeCreditAmount - vipDiscount);
                        usedCredit = (beforeCreditAmount - vipDiscount);
                        totalAmount = 0;
                    } else {
                        totalAmount = beforeCreditAmount - vipDiscount - customer.getCredit();
                        remainingCredit = 0;
                        usedCredit = creditAmount;
                    }
                    double cumulativeCost = 0;
                    double prevCumulativeCost = 0;
                    //Set credit to 0 when credit is used
//                    if (customer.getCredit() != 0) {
//                        boolean userUpdated = myDb.updateData(customer.getId(), customer.getFirstName(),
//                                customer.getLastName(), customer.getEmail(), customer.getVIPstatus(),
//                                remainingCredit, customer.getCumulativeCost(), customer.getVipLastUpdate(), "");
//                        creditUsed = 1;
//                        Toast.makeText(ProcessPurchaseActivity.this, "Remaining credit " + remainingCredit, Toast.LENGTH_LONG).show();
//
//                    }


                    //Get Dates
                    Date date = getDate();
                    String dateStr = dateToString(date);
                    if (paymentProcess(totalAmount)) {
                        //Set credit to remaining credit when credit is used
                        if (customer.getCredit() != 0) {
                            boolean userUpdated = myDb.updateData(customer.getId(), customer.getFirstName(),
                                    customer.getLastName(), customer.getEmail(), customer.getVIPstatus(),
                                    remainingCredit, customer.getCumulativeCost(), customer.getVipLastUpdate(), "");
                            creditUsed = 1;
                            Toast.makeText(ProcessPurchaseActivity.this, "Remaining credit " + remainingCredit, Toast.LENGTH_LONG).show();

                        }

                        boolean isInserted = myDb.insertDataPurchase(customerId, dateStr, numCoffee,
                                numTea, beforeCreditAmount, totalAmount, creditUsed, usedCredit, vipStatus, vipDiscount);
                        if (isInserted == true) {
                            prevCumulativeCost = customer.getCumulativeCost();
                            cumulativeCost = customer.getCumulativeCost() + totalAmount;
                            if (creditUsed == 1) {
                                //When credit is used
                                boolean cumulativeUpdated;
                                if (remainingCredit == 0) {
                                    cumulativeUpdated = myDb.updateData(customer.getId(),
                                            customer.getFirstName(), customer.getLastName(),
                                            customer.getEmail(), customer.getVIPstatus(), remainingCredit,
                                            cumulativeCost, customer.getVipLastUpdate(), "");
                                } else {
                                    cumulativeUpdated = myDb.updateData(customer.getId(),
                                            customer.getFirstName(), customer.getLastName(),
                                            customer.getEmail(), customer.getVIPstatus(), remainingCredit,
                                            cumulativeCost, customer.getVipLastUpdate(),
                                            customer.getCreditReceivedDate());
                                }
                                //boolean cumulativeUpdated = myDb.updateData(customer.getId(), customer.getFirstName(), customer.getLastName(),
                                //        customer.getEmail(), customer.getVIPstatus(), remainingCredit, cumulativeCost,customer.getVipLastUpdate());
                                if (cumulativeUpdated) {
                                    Toast.makeText(ProcessPurchaseActivity.this, "Cumulative cost " + cumulativeCost + " has been successfully updated", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ProcessPurchaseActivity.this, "Cumulative cost NOT successfully updated", Toast.LENGTH_LONG).show();
                                }
                            } else if (creditUsed == 0) {
                                //When credit is not used
                                boolean cumulativeUpdated = myDb.updateData(customer.getId(),
                                        customer.getFirstName(), customer.getLastName(),
                                        customer.getEmail(), customer.getVIPstatus(),
                                        remainingCredit, cumulativeCost, customer.getVipLastUpdate(),
                                        customer.getCreditReceivedDate());
                                if (cumulativeUpdated) {
                                    Toast.makeText(ProcessPurchaseActivity.this, "Cumulative cost " + cumulativeCost + " has been successfully updated", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ProcessPurchaseActivity.this, "Cumulative cost NOT successfully updated", Toast.LENGTH_LONG).show();
                                }
                            }

                            Toast.makeText(ProcessPurchaseActivity.this, "Purchase successfully made", Toast.LENGTH_LONG).show();
                            //Send Email for RECEIPT
                            String recieptSummary = "You have bought " + numCoffee + " Coffee, " +
                                    numTea + " Tea for " + totalAmount + "Dollars" + " on " + dateStr;
                            boolean receiptEmailSent = EmailService.sendEMail(customer.getEmail(), "Receipt", recieptSummary);
                            if (receiptEmailSent) {
                                Toast.makeText(ProcessPurchaseActivity.this, "Receipt email sent!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ProcessPurchaseActivity.this, "Receipt email FAILED", Toast.LENGTH_LONG).show();

                            }

                            //Send Email to VIP
                            if ((cumulativeCost >= 300) && (prevCumulativeCost <300)) {
                                Toast.makeText(ProcessPurchaseActivity.this, "SHOULD BE PRINTED IF VIP STATUS", Toast.LENGTH_LONG).show();
                                String vipEmail = customer.getFirstName() + ", you will be our VIP starting next year!!";
                                boolean vipEmailSent = EmailService.sendEMail(customer.getEmail(), "You are VIP!!", vipEmail);
                                if (vipEmailSent) {
                                    Toast.makeText(ProcessPurchaseActivity.this, "VIP email sent!", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(ProcessPurchaseActivity.this, "VIP email FAILED!", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (totalAmount >= 30) {
                                //Give Credit if customer spends more than 30 dollars on purchase
                                Toast.makeText(ProcessPurchaseActivity.this, "SHOULD GET  CREDIT !!!!!!! ", Toast.LENGTH_LONG).show();

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                                String creditIssuedDate = dateFormat.format(new Date());

                                boolean userUpdated = myDb.updateData(customer.getId(),
                                        customer.getFirstName(), customer.getLastName(),
                                        customer.getEmail(), customer.getVIPstatus(), 3, cumulativeCost,
                                        customer.getVipLastUpdate(), creditIssuedDate);
                                if (userUpdated) {
                                    //Send Email to VIP
                                    String creditEmail = customer.getFirstName() + ", you got 3 dollars of credit! It, however, expires after 30 days. ";
                                    boolean creditEmailSent = EmailService.sendEMail(customer.getEmail(), "Credit Received!", creditEmail);
                                    if (creditEmailSent) {
                                        Toast.makeText(ProcessPurchaseActivity.this, "Credit email sent!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(ProcessPurchaseActivity.this, creditEmail + "", Toast.LENGTH_LONG).show();
                                    }
                                    Toast.makeText(ProcessPurchaseActivity.this, "Customer successfully received Credit", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ProcessPurchaseActivity.this, "Customer not successfully received Credit", Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                        else
                            Toast.makeText(ProcessPurchaseActivity.this, "Purchase not successfully made", Toast.LENGTH_LONG).show();
//                        if (totalAmount >= 30) {
//                            //Give Credit if customer spends more than 30 dollars on purchase
//                            Toast.makeText(ProcessPurchaseActivity.this, "SHOULD GET  CREDIT !!!!!!! ", Toast.LENGTH_LONG).show();
//
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
//                            String creditIssuedDate = dateFormat.format(new Date());
//
//                            boolean userUpdated = myDb.updateData(customer.getId(),
//                                    customer.getFirstName(), customer.getLastName(),
//                                    customer.getEmail(), customer.getVIPstatus(), 3, cumulativeCost,
//                                    customer.getVipLastUpdate(), creditIssuedDate);
//                            if (userUpdated) {
//                                //Send Email to VIP
//                                String creditEmail = customer.getFirstName() + ", you got 3 dollars of credit! It, however, expires after 30 days. ";
//                                boolean creditEmailSent = EmailService.sendEMail(customer.getEmail(), "Credit Received!", creditEmail);
//                                if (creditEmailSent) {
//                                    Toast.makeText(ProcessPurchaseActivity.this, "Credit email sent!", Toast.LENGTH_LONG).show();
//                                } else {
//                                    Toast.makeText(ProcessPurchaseActivity.this, creditEmail + "", Toast.LENGTH_LONG).show();
//                                }
//                                Toast.makeText(ProcessPurchaseActivity.this, "Customer successfully received Credit", Toast.LENGTH_LONG).show();
//                            } else {
//                                Toast.makeText(ProcessPurchaseActivity.this, "Customer not successfully received Credit", Toast.LENGTH_LONG).show();
//                            }
//                        }
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        showMessage("Payment Error", "Payment was not successful. Please slide card again.");
                        //editTextPurchaseID.setText("");
                        //numberOfCoffee.setText("");
                        //numberOfTea.setText("");
                    }
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);
//                    finish();
                }
            }
            else{
                //given customer not exist in the system
                showMessage("Customer Not Found", "Customer is not in the system");
                editTextPurchaseID.setText("");
                numberOfCoffee.setText("");
                numberOfTea.setText("");
            }


        }

        // Store id, userID,Date, numCoffee, numTea, beforeCreditApplied totalAmount, in PURCHASE_TABLE
    }

    public void onClick_cancel(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public Date getDate(){
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        Date date = stringToDate(dateStr);
        return date;
    }

    public String dateToString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public Date stringToDate(String str){
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        //String strDate = "2014-01-29 13:30";
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public boolean paymentProcess(double amount){
        boolean success = false;
        String creditCardInfo = CreditCardService.readCreditCard();
        SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
        String[] splits = creditCardInfo.split("#");
        String firstName = splits[0];
        String lastName = splits[1];
        String ccNumber = splits[2];
        Date expirationDate = null;
        try {
            expirationDate = format.parse(splits[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String securityCode = splits[4];
        Toast.makeText(ProcessPurchaseActivity.this, "Expiration Date: " + expirationDate, Toast.LENGTH_LONG).show();

        success = PaymentService.processPayment(firstName,lastName,ccNumber,expirationDate,securityCode,amount);

        return success;
    }


    /**
     * Hides virtual keyboard
     *
     */
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
