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
import java.util.Random;

import edu.gatech.seclass.services.PrintingService;

public class AddingCustomerActivity extends AppCompatActivity {
    LinearLayout addCustomerLayout;
    ScrollView addCustomerScrollView;
    EditText customerFirstName;
    EditText customerLastName;
    EditText customerEmail;
    DBManager myDb;


    int initialVIPstatus = 0;
    double initialCredit = 0;
    double initialCumulativeCost =0;
    String initialCreditReceivedDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_customer);

        addCustomerScrollView = (ScrollView) findViewById(R.id.addCustomerScrollView);
        addCustomerScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
        myDb = new DBManager(this);

        customerFirstName = (EditText) findViewById(R.id.editTextCustomerFirstName);
        customerLastName = (EditText) findViewById(R.id.editTextCustomerName);
        customerEmail = (EditText) findViewById(R.id.editTextCustomerEmail);

    }
    public void onClick_cancel(View view)
    {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


    public void onClick_addCustomer(View view)
    {
        //ADD CUSTOMER
        String firstName = customerFirstName.getText().toString();
        String lastName = customerLastName.getText().toString();
        String email = customerEmail.getText().toString();
        String id = "";
        if((lastName.equals("")) || (email.equals("")) || (firstName.equals(""))){
            showMessage("Not enough Information","Fill in all required information");
            customerLastName.setText("");
            customerEmail.setText("");
            customerFirstName.setText("");
        }
        else {
            // Check if id is not in customer list, give id to customer else compute again for random and unique ID
            boolean valid = true;
            while (valid) {
                id = createID();
                if (validID(id)) {
                    valid = false;
                }
            }
            Date today = getDate();
            String todayString = dateToString(today);
            Customer customer = new Customer(id, firstName, lastName, email,initialVIPstatus,
                    initialCredit,initialCumulativeCost,todayString,initialCreditReceivedDate);
            boolean isInserted = myDb.insertData(customer.getId(), customer.getFirstName(),
                    customer.getLastName(), customer.getEmail(), customer.getVIPstatus(),
                    customer.getCredit(),customer.getCumulativeCost(), customer.getVipLastUpdate(),
                    customer.getCreditReceivedDate());
            if (isInserted == true) {
                //Toast.makeText(AddingCustomerActivity.this, "Customer successfully added", Toast.LENGTH_LONG).show();
                //Print customer card automatically when he or she is added as customer
                boolean cardPrinted = printCustomerCard(customer);
                if (cardPrinted) {
                    Toast.makeText(AddingCustomerActivity.this, "Customer successfully added", Toast.LENGTH_LONG).show();

                    Toast.makeText(AddingCustomerActivity.this, "Customer card successfully printed", Toast.LENGTH_LONG).show();
                    //Toast.makeText(AddingCustomerActivity.this, id, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{
                    showMessage("Printing Card Failed"," Printing card failed! Please try again.");
                    myDb.deleteData(id);
                    Toast.makeText(AddingCustomerActivity.this, "Customer successfully deleted", Toast.LENGTH_LONG).show();
                    customerLastName.setText("");
                    customerEmail.setText("");
                    customerFirstName.setText("");
                }
            }
            else {
                showMessage("System Error"," Customer not added.");
                customerLastName.setText("");
                customerEmail.setText("");
                customerFirstName.setText("");
            }
//            //Toast.makeText(AddingCustomerActivity.this, id, Toast.LENGTH_LONG).show();
//            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(i);
//            finish();
        }
    }


    public String createID(){
        Random random = new Random();
        //Need to change this to 8 digit hex. It is now 4 digit hex number.
        int val1 = random.nextInt((65535 - 4096) + 1) + 4096;
        int val2 = random.nextInt((65535 - 4096) + 1) + 4096;

        String Hex1 = new String();
        Hex1 = Integer.toHexString(val1);
        String Hex2 = new String();
        Hex2 = Integer.toHexString(val2);

        String Hex = Hex1 + Hex2;
        return Hex;
    }
    public boolean validID(String id){
        boolean duplicate = myDb.duplicateID(id);
        //boolean duplicate = false;
        return !duplicate;
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
    public boolean printCustomerCard(Customer customer){
//        boolean printSuccess = false;
//        while(!printSuccess){
//            boolean print = PrintingService.printCard(customer.getFirstName(), customer.getLastName(),customer.getId());
//            if(print) {
//                printSuccess = true;
//            }
//        }
        boolean printSuccess = PrintingService.printCard(customer.getFirstName(), customer.getLastName(),customer.getId());

        return printSuccess;

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
