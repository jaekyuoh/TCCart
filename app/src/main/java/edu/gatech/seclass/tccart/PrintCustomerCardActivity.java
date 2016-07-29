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

import edu.gatech.seclass.services.PrintingService;

public class PrintCustomerCardActivity extends AppCompatActivity {
    LinearLayout printCustomerCardLayout;
    ScrollView printCustomerCardScrollView;
    DBManager myDb;
    EditText customerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_customer_card);

        printCustomerCardScrollView = (ScrollView) findViewById(R.id.printCustomerCardScrollView);
        printCustomerCardScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
        myDb = new DBManager(this);
        customerId = (EditText)findViewById(R.id.editTextPrintID);


    }

    public void onClick_printCustomerCard(View view){
        String id = customerId.getText().toString();
        Customer customer = myDb.retrieveUserInfo(id);
        if((id.equals(""))){
            showMessage("Not enough Information","Fill in all required information");
            customerId.setText("");
        }
        else if (customer.isEmpty()){
            String ms = "There is no matching customer";
            showMessage("Not valid user", ms);
            customerId.setText("");
        }
        else{
            boolean print = PrintingService.printCard(customer.getId(),customer.getFirstName(),customer.getLastName());
            if(print) {
                Toast.makeText(PrintCustomerCardActivity.this, "Customer Card has been successfully printed for " + customer.getFirstName() + " " +customer.getLastName(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
            else{
                String ms = "Sorry, Printing Service was not successful. Please try it again.";
                showMessage("Print Failed", ms);
                customerId.setText("");
            }

        }


    }
    public void onClick_cancel(View view)
    {
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
