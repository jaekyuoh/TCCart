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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.gatech.seclass.services.QRCodeService;

public class CustomerHistoryActivity extends AppCompatActivity {
    ScrollView customerHistoryScrollView;
    RelativeLayout customerHistoryRelativeLayout;
    DBManager myDb;
    EditText customerId;
    String id;
    TextView historyContext;
    TextView availCredit;
    TextView VIPstat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);
        customerHistoryRelativeLayout = (RelativeLayout) findViewById(R.id.customerHistoryRelativeLayout);
        customerHistoryRelativeLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        customerHistoryScrollView = (ScrollView) findViewById(R.id.customerHistoryScrollView);
        customerHistoryScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
        myDb = new DBManager(this);

        customerId = (EditText)findViewById(R.id.editTextCustomerID);
        historyContext = (TextView) findViewById(R.id.historyContext);
        availCredit = (TextView) findViewById(R.id.availCredit);
        VIPstat = (TextView)findViewById(R.id.VIPstat);
    }

    public void onClick_scanQRcode(View view){
        id = QRCodeService.scanQRCode(); // using QRcode
        customerId.setText(id);
        if(id.equals("ERR")){
            showMessage("Scan Error", "Please scan QR code again.");
            customerId.setText("");
        }
        Toast.makeText(CustomerHistoryActivity.this, "Id from QRcode: " + id, Toast.LENGTH_LONG).show();
    }

    public void onClick_viewHistory(View view)
    {
        id = customerId.getText().toString();
        ArrayList<Purchase> purchaseList = myDb.retrievePurchaseInfo(id);
        Customer targetCustomer = myDb.retrieveUserInfo(id);

        if(purchaseList == null){
            //Show message
            showMessage("No History", "This customer has no transaction history");
            customerId.setText("");
            return;
        }
        else{
            StringBuffer buffer = new StringBuffer();
            for (int i =0; i <purchaseList.size(); i++){
                Purchase purchase = purchaseList.get(i);
                String creditUsed = "No";
                String vip = "No";
                if (purchase.getCreditUsed() == 1){
                        creditUsed = "Yes";
                }
                if (purchase.getVip() == 1){
                    vip = "Yes";
                }
                buffer.append("Date: " + purchase.getDate() + "\n");
                buffer.append("Number of Coffee: " + purchase.getCoffeeCount() + "\n");
                buffer.append("Number of Tea: " + purchase.getTeaCount() + "\n");
                buffer.append("Credit used? : " + creditUsed + "\n");
                buffer.append("Credit Discount Amount: " + purchase.getCreditAmount() + "\n");
                buffer.append("VIP? : " + vip + "\n");
                buffer.append("VIP Discount Amount: " + purchase.getVipAmount() + "\n");
                buffer.append("Cost before Any Discount: " + purchase.getBeforeDiscount() + "\n");
                buffer.append("Total Amount: " + purchase.getTotal() + "\n");
                //buffer.append("Purchase List Size: " + purchaseList.size() + "\n");
                //buffer.append("\n");
                buffer.append("\n");
            }
            historyContext.setText(buffer.toString());
            //Add available credit and VIP status
            availCredit.setText(String.valueOf(targetCustomer.getCredit()));
            if (targetCustomer.getVIPstatus()==1){
                VIPstat.setText("Yes");
            }
            else{
                VIPstat.setText("No");
            }
            //showMessage("Customer History", buffer.toString());
        }


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
