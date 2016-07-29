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
import android.widget.ScrollView;
import android.widget.Toast;

import edu.gatech.seclass.services.QRCodeService;

public class EditCustomerActivity extends AppCompatActivity {
    ScrollView editCustomerScrollView;
    EditText customerId;
    EditText editFirstName;
    EditText editName;
    EditText editEmail;
    CustomerList customerList;
    DBManager myDb;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        editCustomerScrollView = (ScrollView) findViewById(R.id.editCustomerScrollView);
        editCustomerScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });


        myDb = new DBManager(this);
        customerId = (EditText) findViewById(R.id.editTextCustomerID);
        editFirstName = (EditText) findViewById(R.id.editTextEditFirstName);
        editName = (EditText) findViewById(R.id.editTextEditName);
        editEmail = (EditText) findViewById(R.id.editTextEditEmail);
    }

    public void onClick_scanQRcode(View view){
        id = QRCodeService.scanQRCode(); // using QRcode
        customerId.setText(id);
        if(id.equals("ERR")){
            showMessage("Scan Error","Please scan QR code again.");
            customerId.setText("");
        }
        Toast.makeText(EditCustomerActivity.this, "Id from QRcode: " + id, Toast.LENGTH_LONG).show();
    }

    public void onClick_editCustomer(View view){
        //EDIT CUSTOMER
        String id = customerId.getText().toString(); // using raw ID
        //String id = QRCodeService.scanQRCode(); // using QRcode
        //Toast.makeText(EditCustomerActivity.this, "Id from QRcode: " + id, Toast.LENGTH_LONG).show();

        String firstName = editFirstName.getText().toString();
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        Customer customer = myDb.retrieveUserInfo(id);
        if((firstName.equals(""))||(name.equals("")) || (email.equals("")) || (id.equals(""))){
            showMessage("Not enough Information","Fill in all required information");
            customerId.setText("");
            editFirstName.setText("");
            editName.setText("");
            editEmail.setText("");
        }
        else if (customer.isEmpty()){
            String ms = "There is no matching customer";
            showMessage("Not valid user", ms);
            customerId.setText("");
            editFirstName.setText("");
            editName.setText("");
            editEmail.setText("");
        }
        else{
            myDb.updateData(customer.getId(), firstName,name, email,customer.getVIPstatus(),
                    customer.getCredit(),customer.getCumulativeCost(),customer.getVipLastUpdate(),
                    customer.getCreditReceivedDate());
            Toast.makeText(EditCustomerActivity.this, "Information has been successfully updated!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
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
