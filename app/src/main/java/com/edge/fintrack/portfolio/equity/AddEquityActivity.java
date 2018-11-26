package com.edge.fintrack.portfolio.equity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;

import java.util.Calendar;
import java.util.HashMap;

public class AddEquityActivity extends AppCompatActivity implements View.OnClickListener {
    DatePickerDialog datePickerDialog;
    // Session Manager Class
    SessionManager session;
    private EditText et_invdate;
    private AutoCompleteTextView act_company;
    private TextView tv_accountHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equity);
        act_company = (AutoCompleteTextView) findViewById(R.id.act_company);
        et_invdate = (EditText) findViewById(R.id.et_invdate);
        et_invdate.setOnClickListener(this);
        et_invdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                et_invdate.requestFocus();
                return true;
            }
        });
        //state_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, statearrayList);
        act_company.setThreshold(1);//will start working from first character
        // act_state.setAdapter(state_adapter);//setting the adapter data into the AutoCompleteTextView
        // et_state.setTextColor(Color.RED);
        act_company.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Toast.makeText(TermInsuranceActivity.this, state_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                //State_Communication = state_id.get(parent.getItemAtPosition(position).toString());
            }
        });

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        tv_accountHolder = (TextView) findViewById(R.id.tv_accountHolder);
        tv_accountHolder.setText(user.get(SessionManager.KEY_NAME));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_invdate:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddEquityActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                et_invdate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                et_invdate.setTextColor(getResources().getColor(R.color.colorBlack));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
