package edu.gatech.seclass.tccart;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DBManager myDb;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DBManager(this);
        //                          THIS IS FOR ONLY TESTING
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String today = dateFormat.format(new Date());
        boolean dummyInserted1 = myDb.insertData("7c86ffee", "JQ",
                "OH", "JQemail@mail.com", 0,
                0,0, today, "");
        boolean dummyInserted2 = myDb.insertData("b59441af", "DS",
                "KIM", "DSemail@mail.com", 0,
                0,0, today, "");
        boolean dummyInserted3 = myDb.insertData("cd0f0e05", "SAM",
                "LEE", "SAMemail@mail.com", 0,
                0,0, today, "");

        //myDb.deleteData("cd0f0e05");
        //myDb.deleteDataPurchase("1");
        checkCredit(); // checks credit and if it has been given to customer over 30 days, then that credit will be expired.
        setVIP();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkCredit(); // checks credit and if it has been given to customer over 30 days, then that credit will be expired.
        setVIP();
    }

    private void setVIP() {
        //if date is next year
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        date = stringToDate(dateStr);
        String todayYearStr = dateStr.substring(0,4);
        int todayYear = Integer.parseInt(todayYearStr);

        Cursor cursor = myDb.getAllData();
        if(cursor.getCount() == 0){
            //Show message
//            showMessage("Error", "Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            buffer.append("ID: " + cursor.getString(0) + "\n");
            buffer.append("Cumulative Cost: " + cursor.getDouble(6) + "\n");
            buffer.append("VIP Last Updated: " + cursor.getString(7) + "\n");
            String vipLastUpdated = cursor.getString(7);
            if(!vipLastUpdated.equals("")){
                String vipLastUpdatedYearStr = vipLastUpdated.substring(0,4);
                int vipLastUpdatedYear = Integer.parseInt(vipLastUpdatedYearStr);
                if(vipLastUpdatedYear < todayYear){
                    //if user's cumulative cost > 300
                    if(cursor.getDouble(6) >= 300) {
                        //update customer as vip
                        myDb.updateData(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                                cursor.getString(3),1,cursor.getDouble(5),
                                0,dateStr,cursor.getString(8));
                    }
                    else{
                        //update customer as NOT vip
                        myDb.updateData(cursor.getString(0),cursor.getString(1),cursor.getString(2),
                                cursor.getString(3),0,cursor.getInt(5),
                                0,dateStr,cursor.getString(8));
                    }
                }
            }

        }
        //Show all data
//        showMessage("Data", buffer.toString());
    }
    private void checkCredit(){
        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        String dateStr = dateFormat.format(new Date());
        Date current = stringToDate(dateStr);

        Cursor cursor = myDb.getAllData();
        if(cursor.getCount() == 0){
            //Show message
//            showMessage("Error", "Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            buffer.append("ID: " + cursor.getString(0) + "\n");
            buffer.append("Credit Received Date: " + cursor.getString(8) + "\n");
            String creditReceived = cursor.getString(8);

            //Date creditReceivedDate = stringToDate(creditReceived);
            Calendar c2 = Calendar.getInstance();
            try {
                c2.setTime(dateFormat.parse(creditReceived));
                c2.add(Calendar.DATE, 30);
                SimpleDateFormat dateFormat2 = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                Date resultdate = new Date(c2.getTimeInMillis());
                String creditReceivedDate = dateFormat2.format(resultdate);

                if(current.after(resultdate)){
                    //Credit is expired
                    myDb.updateData(cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), cursor.getInt(4),
                            0, cursor.getDouble(6),cursor.getString(7),"");
                }

            }catch (ParseException e){
                e.printStackTrace();
            }
        }




    }




    public void onClick_toAddCustomer(View view)
    {
        Intent i = new Intent(getApplicationContext(), AddingCustomerActivity.class);
        startActivity(i);
        finish();
    }

    public void onClick_toEditCustomer(View view)
    {
        Intent i = new Intent(getApplicationContext(), EditCustomerActivity.class);
        startActivity(i);
        finish();
    }

    public void onClick_toProcessPurchase(View view)
    {
        Intent i = new Intent(getApplicationContext(), ProcessPurchaseActivity.class);
        startActivity(i);
        finish();
    }

    public void onClick_toPrintCustomerCard(View view)
    {
        Intent i = new Intent(getApplicationContext(), PrintCustomerCardActivity.class);
        startActivity(i);
        finish();
    }

    public void onClick_toCustomerHistory(View view)
    {
        Intent i = new Intent(getApplicationContext(), CustomerHistoryActivity.class);
        startActivity(i);
        finish();
    }


    public void onClick_showUserDB(View view)
    {
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0){
            //Show message
            showMessage("Error", "Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("First Name: " + res.getString(1) + "\n");
            buffer.append("Last Name: " + res.getString(2) + "\n");
            buffer.append("Email: " + res.getString(3) + "\n");
            buffer.append("VIPstatus: " + res.getInt(4) + "\n");
            buffer.append("Credit: " + res.getDouble(5) + "\n");
            buffer.append("Cumulative Cost: " + res.getDouble(6) + "\n");
            buffer.append("VIP Last Updated Date: " + res.getString(7) + "\n");
            buffer.append("Credit Received Date: " + res.getString(8) + "\n");
            buffer.append("\n");
            buffer.append("\n");

        }
        //Show all data
        showMessage("Data", buffer.toString());
    }

    public void onClick_showPurchaseDB(View view)
    {
        Cursor res = myDb.getAllDataPurchase();
        if(res.getCount() == 0){
            //Show message
            showMessage("Error", "Nothing found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("ID: " + res.getInt(0) + "\n");
            buffer.append("Customer ID: " + res.getString(1) + "\n");
            buffer.append("Date: " + res.getString(2) + "\n");
            buffer.append("Number of Coffee: " + res.getInt(3) + "\n");
            buffer.append("Number of Tea: " + res.getInt(4) + "\n");
            buffer.append("Cost before Credit: " + res.getDouble(5) + "\n");
            buffer.append("Total Amount: " + res.getDouble(6) + "\n");
            buffer.append("Credit used? : " + res.getInt(7) + "\n");
            buffer.append("Credit Amount: " + res.getDouble(8) + "\n");
            buffer.append("VIP? : " + res.getInt(9) + "\n");
            buffer.append("VIP Amount: " + res.getDouble(10) + "\n");
            buffer.append("\n");
            buffer.append("\n");
        }
        //Show all data
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
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

}
