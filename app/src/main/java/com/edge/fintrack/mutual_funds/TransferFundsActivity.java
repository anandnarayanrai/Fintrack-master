package com.edge.fintrack.mutual_funds;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;

import java.util.HashMap;
import java.util.Random;

public class TransferFundsActivity extends AppCompatActivity {
    public final String TAG = "TransferFundsActivity";
    private final String[] MFname_list = {"Invesco Growth Opportunities - D (G)", "ICICI Pru Bluechip Fund - D (G)", "HSBC Large Cap Equity Fund (G)", "Small & Mid Cap", "Axis Mid Cap Fund - Direct (G)", "HDFC Small Cap Fund - Direct (G)", "Axis Mid Cap Fund (G)", "Axis Focused 25 Fund - Direct (G)"};
    private final String[] Pnumber_list = {"NMNMH7848H", "TRTFH7895D", "AGJGH8945Q", "AGJGH1234Q", "AGJGH4454C", "AGJGH8144A", "AGJGQ1564K"};
    private final String[] Investor_list = {"Invesco Growth Opportunities - D (G)", "ICICI Pru Bluechip Fund - D (G)", "HSBC Large Cap Equity Fund (G)", "Small & Mid Cap", "Axis Mid Cap Fund - Direct (G)", "HDFC Small Cap Fund - Direct (G)", "Axis Mid Cap Fund (G)", "Axis Focused 25 Fund - Direct (G)"};
    // Session Manager Class
    SessionManager session;
    AutoCompleteTextView ac_MFname;
    ArrayAdapter<String> MFname_adapter;
    ImageView iv_menu_item;
    private EditText et_Fnumber, et_Pnumber, et_Investor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_transfer_funds);
        // Session class instance
        session = new SessionManager(TransferFundsActivity.this);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        Log.d(TAG, "Registration_Id " + user.get(SessionManager.KEY_Registration_Id));

        et_Fnumber = (EditText) findViewById(R.id.et_Fnumber);
        Random rand = new Random();
        int num = rand.nextInt(900000000) + 1000000000;
        et_Fnumber.setText(String.valueOf(num));
        et_Pnumber = (EditText) findViewById(R.id.et_Pnumber);
        et_Pnumber.setText(Pnumber_list[0]);
        et_Investor = (EditText) findViewById(R.id.et_Investor);
        et_Investor.setText(Investor_list[0]);

        ac_MFname = (AutoCompleteTextView) findViewById(R.id.ac_MFname);
        ac_MFname.setText(MFname_list[0]);
        MFname_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, MFname_list);
        ac_MFname.setThreshold(1);
        ac_MFname.setAdapter(MFname_adapter);
        ac_MFname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(TransferFundsActivity.this, MFname_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                Random rand = new Random();
                int num = rand.nextInt(900000000) + 100000000;
                et_Fnumber.setText(String.valueOf(num));
                et_Pnumber.setText(Pnumber_list[position]);
                et_Investor.setText(Investor_list[position]);
            }
        });

        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onSubmit(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
