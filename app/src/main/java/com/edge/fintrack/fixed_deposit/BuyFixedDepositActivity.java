package com.edge.fintrack.fixed_deposit;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;
import com.edge.fintrack.account_detail.UpdateProfileActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorNominee;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getScheme;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getTenureB;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getTenureTB;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getVRates;
import static com.edge.fintrack.Api_Class.METHOD_NAME_postFixedDeposit;
import static com.edge.fintrack.Api_Class.METHOD_NAME_updateDataBind;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_InvestorNominee;
import static com.edge.fintrack.Api_Class.URL_getFixed;
import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isInternetOn;

public class BuyFixedDepositActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public final String TAG = "FixedDepositActivity";
    ImageView iv_menu_item;
    // Session Manager Class
    SessionManager session;
    String Registration_Id;
    SoapPrimitive Nominee_resultString;
    SoapPrimitive Scheme_resultString;
    ArrayList<String> invwster_list = new ArrayList<>();
    ArrayList<String> pmode_list = new ArrayList<>();
    ArrayList<String> SCi_list = new ArrayList<>();
    ArrayList<String> nominee_list = new ArrayList<>();
    Map<String, String> nominee_rel = new HashMap<String, String>();
    ArrayAdapter<String> nominee_adapter;

    ArrayList<String> schemename_list = new ArrayList<>();
    Map<String, String> schemename_id = new HashMap<String, String>();
    ArrayAdapter<String> schemename_adapter;

    ArrayList<String> VRates_list = new ArrayList<>();
    Map<String, String> VRates_id = new HashMap<String, String>();
    ArrayAdapter<String> VRates_adapter;

    ArrayList<String> tenure_list = new ArrayList<>();
    Map<String, String> tenure_id = new HashMap<String, String>();
    ArrayAdapter<String> tenure_adapter;

    ArrayList<String> tenureT_list = new ArrayList<>();
    Map<String, String> tenureT_id = new HashMap<String, String>();
    ArrayAdapter<String> tenureT_adapter;

    TextView et_n_date_of_birth, spinner_issuer, tv_fv_rate;
    DatePickerDialog datePickerDialog;
    int FlagVariableRates;
    Double Minimum_Amount;
    EditText tv_minimum_amount, ed_payment;
    TextView tv_rate_of_inte, tv_maturity_amount, tv_date_of_maturity;
    TextInputLayout layout_tv_minimum_amount;
    String interestRate, Investment_Start_Date, Date_Of_Maturity, Payment_Mode, Is_Senior_Citizen, Nominee_Name, Nominee_Relationship;
    private String Company_ID, Issuer, Scheme_ID, Scheme_Sub_Category = "", Tenure, TenureType;
    private Spinner spinner_investor_name, spinner_schemename, spinner_fv_rate, spinner_tenure, spinner_type, spinner_pmode, spinner_SCi, spinner_NList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_buy_fixed_deposit);
        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Company_ID = bundle.getString("Company_ID");
            Issuer = bundle.getString("Issuer");
        }

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        Registration_Id = user.get(SessionManager.KEY_Registration_Id);
        if (!isInternetOn(BuyFixedDepositActivity.this)) {
            ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
        } else {
            getDetailCallWS nomineeDetailCallWS = new getDetailCallWS();
            nomineeDetailCallWS.execute();

            getSchemeName getSchemeName = new getSchemeName();
            getSchemeName.execute();
        }
        spinner_investor_name = (Spinner) findViewById(R.id.spinner_investor_name);
        spinner_investor_name.setOnItemSelectedListener(this);
        invwster_list.add(user.get(SessionManager.KEY_NAME));
        ArrayAdapter<String> invwster_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, invwster_list);
        invwster_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_investor_name.setAdapter(invwster_adapter);

        spinner_issuer = (TextView) findViewById(R.id.spinner_issuer);
        spinner_issuer.setText(Issuer);
        //spinner_issuer.setOnItemSelectedListener(this);
        spinner_schemename = (Spinner) findViewById(R.id.spinner_schemename);
        spinner_schemename.setOnItemSelectedListener(this);
        schemename_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, schemename_list);
        schemename_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_schemename.setAdapter(schemename_adapter);

        tv_fv_rate = (TextView) findViewById(R.id.tv_fv_rate);
        spinner_fv_rate = (Spinner) findViewById(R.id.spinner_fv_rate);
        spinner_fv_rate.setOnItemSelectedListener(this);
        VRates_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, VRates_list);
        VRates_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_fv_rate.setAdapter(VRates_adapter);

        spinner_tenure = (Spinner) findViewById(R.id.spinner_tenure);
        spinner_tenure.setOnItemSelectedListener(this);
        tenure_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, tenure_list);
        tenure_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tenure.setAdapter(tenure_adapter);

        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        spinner_type.setOnItemSelectedListener(this);
       /* investment_type_list.add("Select Type");
        investment_type_list.add("Monthly Income Plan");
        investment_type_list.add("Quarterly Option");
        investment_type_list.add("Half-Yearly Option");
        investment_type_list.add("Annual Income Plan");
        investment_type_list.add("Cumulative Option*");*/
        tenureT_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, tenureT_list);
        tenureT_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(tenureT_adapter);

        spinner_pmode = (Spinner) findViewById(R.id.spinner_pmode);
        spinner_pmode.setOnItemSelectedListener(this);
        pmode_list.add("Select Mode");
        pmode_list.add("Cheque");
        pmode_list.add("Demant Draft");
        pmode_list.add("Online");
        pmode_list.add("Other");
        ArrayAdapter<String> pmode_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, pmode_list);
        pmode_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_pmode.setAdapter(pmode_adapter);

        spinner_SCi = (Spinner) findViewById(R.id.spinner_SCi);
        spinner_SCi.setOnItemSelectedListener(this);
        SCi_list.add("No");
        SCi_list.add("Yes");
        ArrayAdapter<String> SCi_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, SCi_list);
        SCi_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_SCi.setAdapter(SCi_adapter);

        spinner_NList = (Spinner) findViewById(R.id.spinner_NList);
        spinner_NList.setOnItemSelectedListener(this);
        nominee_adapter = new ArrayAdapter<>(BuyFixedDepositActivity.this, android.R.layout.simple_spinner_dropdown_item, nominee_list);
        nominee_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_NList.setAdapter(nominee_adapter);

        et_n_date_of_birth = (TextView) findViewById(R.id.et_n_date_of_birth);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        String formattedDate = df.format(c);
        SimpleDateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
        Investment_Start_Date = dff.format(c);
        et_n_date_of_birth.setText("Request Date : " + formattedDate);
        //et_n_date_of_birth.setOnClickListener(this);

        layout_tv_minimum_amount = (TextInputLayout) findViewById(R.id.layout_tv_minimum_amount);
        tv_rate_of_inte = (TextView) findViewById(R.id.tv_rate_of_inte);
        tv_maturity_amount = (TextView) findViewById(R.id.tv_maturity_amount);
        tv_date_of_maturity = (TextView) findViewById(R.id.tv_date_of_maturity);
        ed_payment = (EditText) findViewById(R.id.ed_payment);
        tv_minimum_amount = (EditText) findViewById(R.id.tv_minimum_amount);
        tv_minimum_amount.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (Double.parseDouble(String.valueOf(s)) < Minimum_Amount) {
                        tv_minimum_amount.setError("Not Less then Minimum Amount");
                        //Toast.makeText(BuyFixedDepositActivity.this, "Not Less then Minimum Amount", Toast.LENGTH_SHORT).show();
                    } else {
                        double P, R, N, finalAmount, A, T;
                        P = Double.parseDouble(String.valueOf(s));
                        R = Double.parseDouble(interestRate);
                        N = Double.parseDouble(TenureType);
                        T = Double.parseDouble(Tenure) / 12;
                        //int CI = (int) (principle * (Math.pow((1 + rate / 100), time)));
                        A = (P * Math.pow((1 + (R / (N * 100))), (N * T)));
                        //Toast.makeText(BuyFixedDepositActivity.this, "" + String.valueOf(A), Toast.LENGTH_SHORT).show();
                        tv_maturity_amount.setText(new DecimalFormat("##.###").format(A));

                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void onSubmit(View view) {
        Log.i(TAG, "onSubmitData" + Registration_Id + " " + Issuer + " " + Scheme_ID + " " + Tenure +
                " " + TenureType + " " + tv_minimum_amount.getText() + " " + Minimum_Amount + " " + Investment_Start_Date +
                " " + interestRate + " " + tv_maturity_amount.getText() + " " + Date_Of_Maturity + " " + Payment_Mode + " " + ed_payment.getText() +
                " " + Is_Senior_Citizen + " " + Nominee_Name + " " + Nominee_Relationship);

        buyFixData buyFixData = new buyFixData();
        buyFixData.execute();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        switch (parent.getId()) {
            case R.id.spinner_type:
                item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinner_gender_items" + item);
                if (item.equalsIgnoreCase("Select")) {
                    spinner_type.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    TenureType = tenureT_id.get(parent.getItemAtPosition(position).toString());
                    spinner_type.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                    updateData updateData = new updateData();
                    updateData.execute();
                }
                break;
            case R.id.spinner_SCi:
                item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinner_gender_items" + item);
                if (item.equalsIgnoreCase("No")) {
                    Is_Senior_Citizen = "0";
                } else {
                    Is_Senior_Citizen = "1";
                }
                break;

            case R.id.spinner_pmode:
                item = parent.getItemAtPosition(position).toString();
                Payment_Mode = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinner_gender_items" + item);
                if (item.equalsIgnoreCase("Select Mode")) {
                    spinner_pmode.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_pmode.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                break;
            case R.id.spinner_NList:
                item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinner_gender_items" + item);
                if (item.equalsIgnoreCase("Add New")) {
                    Intent intentUpdateProfile = new Intent(BuyFixedDepositActivity.this, UpdateProfileActivity.class);
                    intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentUpdateProfile.putExtra("layout", "layout_nominee");
                    startActivity(intentUpdateProfile);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                } else {
                    Nominee_Name = parent.getItemAtPosition(position).toString();
                    Nominee_Relationship = nominee_rel.get(parent.getItemAtPosition(position).toString());
                    Log.d(TAG, "Nominee_Name" + Nominee_Name + " Nominee_Relationship" + Nominee_Relationship);
                }
                break;
            case R.id.spinner_schemename:
                item = parent.getItemAtPosition(position).toString();
                Scheme_ID = schemename_id.get(parent.getItemAtPosition(position).toString());
                if (item.equalsIgnoreCase("Select")) {
                    spinner_schemename.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_schemename.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                    getVRates getVRates = new getVRates();
                    getVRates.execute();
                }
                break;
            case R.id.spinner_fv_rate:
                item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Select")) {
                    spinner_schemename.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    Scheme_Sub_Category = VRates_id.get(parent.getItemAtPosition(position).toString());
                    FlagVariableRates = 1;
                    getTenureB getTenureB = new getTenureB();
                    getTenureB.execute();
                    spinner_schemename.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                break;
            case R.id.spinner_tenure:
                item = parent.getItemAtPosition(position).toString();
                if (item.equalsIgnoreCase("Select")) {
                    spinner_schemename.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    try {
                        spinner_schemename.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                        Tenure = parent.getItemAtPosition(position).toString();
                        // String rates_id = VRates_id.get(parent.getItemAtPosition(position).toString());
                        getTenureT getTenureT = new getTenureT();
                        getTenureT.execute();

                        //create Calendar instance
                        Calendar now = Calendar.getInstance();

                        System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
                                + "-"
                                + now.get(Calendar.DATE)
                                + "-"
                                + now.get(Calendar.YEAR));

                        //add months to current date using Calendar.add method
                        int mm = Integer.parseInt(Tenure);
                        now.add(Calendar.MONTH, mm);

                        System.out.println("date after " + mm + " months : " + now.get(Calendar.DATE)
                                + "/"
                                + (now.get(Calendar.MONTH) + 1)
                                + "/"
                                + now.get(Calendar.YEAR));

                        Date_Of_Maturity = now.get(Calendar.DATE)
                                + "/"
                                + (now.get(Calendar.MONTH) + 1)
                                + "/"
                                + now.get(Calendar.YEAR);

                        tv_date_of_maturity.setText(String.format("%s%d/%d/%d", getResources().getString(R.string.date_of_maturity), now.get(Calendar.DATE), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR)));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_n_date_of_birth:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BuyFixedDepositActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                et_n_date_of_birth.setText("Investment Start Date:- " + dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
        }
    }

    public void getInvestorNominee(String responce) {
        try {
            /**
             * [{"Registration_Id":"FIN20186161032923","Customer_Name":"Anand Rai","Nominee_Name":"","Nominee_DOB":"",
             * "Nominee_Relation":"","Guardian_Name":""}]
             * */
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // EditText et_nominee_name=(EditText)findViewById(R.id.et_nominee_name);
                /*if (jsonObject.has("Customer_Name"))
                ((TextView) findViewById(R.id.et_nominee_name)).setText(jsonObject.getString("Customer_Name"));*/
                if (jsonObject.has("Nominee_Name"))
                    nominee_list.add(jsonObject.getString("Nominee_Name"));
                //((TextView) findViewById(R.id.et_nominee_name)).setText(jsonObject.getString("Nominee_Name"));
                /*if (jsonObject.has("Guardian_Name"))
                    ((TextView) findViewById(R.id.et_parent_guardian)).setText(jsonObject.getString("Guardian_Name"));*/
                if (jsonObject.has("Nominee_Relation"))
                    nominee_rel.put(jsonObject.getString("Nominee_Name"), jsonObject.getString("Nominee_Relation"));
                //((TextView) findViewById(R.id.et_relationship)).setText(jsonObject.getString("Nominee_Relation"));
                /*if (jsonObject.has("Nominee_DOB"))
                    ((TextView) findViewById(R.id.et_n_date_of_birth)).setText(jsonObject.getString("Nominee_DOB")); */
            }
            nominee_list.add("Add New");
            nominee_adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorNominee Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSchemeName(String responce) {
        try {
            if (!schemename_list.isEmpty()) {
                schemename_list.clear();
                schemename_id.clear();
            }
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // EditText et_nominee_name=(EditText)findViewById(R.id.et_nominee_name);
                if (jsonObject.has("Scheme_Name"))
                    schemename_list.add(jsonObject.getString("Scheme_Name"));
                if (jsonObject.has("Scheme_ID"))
                    schemename_id.put(jsonObject.getString("Scheme_Name"), jsonObject.getString("Scheme_ID"));
            }
            schemename_adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorNominee Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getVRates(String responce) {
        try {
            if (!VRates_list.isEmpty()) {
                VRates_list.clear();
                VRates_id.clear();
            }
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // EditText et_nominee_name=(EditText)findViewById(R.id.et_nominee_name);
                if (jsonObject.has("Scheme_Sub_Category"))
                    if (!jsonObject.getString("Scheme_Sub_Category").equalsIgnoreCase("")) {
                        spinner_fv_rate.setVisibility(View.VISIBLE);
                        tv_fv_rate.setVisibility(View.VISIBLE);
                        VRates_list.add(jsonObject.getString("Scheme_Sub_Category"));
                        if (jsonObject.has("Scheme_Sub_CategoryName"))
                            VRates_id.put(jsonObject.getString("Scheme_Sub_CategoryName"), jsonObject.getString("Scheme_Sub_CategoryName"));
                    } else {
                        spinner_fv_rate.setVisibility(View.GONE);
                        tv_fv_rate.setVisibility(View.GONE);
                        Scheme_Sub_Category = "";
                        FlagVariableRates = 0;
                        getTenureB getTenureB = new getTenureB();
                        getTenureB.execute();
                    }
            }
            VRates_adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorNominee Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTenureB(String responce) {
        try {
            if (!tenure_list.isEmpty()) {
                tenure_list.clear();
                tenure_id.clear();
            }
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // EditText et_nominee_name=(EditText)findViewById(R.id.et_nominee_name);
                /*[{"Tenure":"0","TenureName":"Select"}]*/
              /*  if (jsonObject.has("TenureName"))
                    tenure_list.add(jsonObject.getString("TenureName"));*/
                if (jsonObject.has("TenureName"))
                    if (jsonObject.getString("TenureName").contains("-")) {
                        String[] arrOfStr = jsonObject.getString("TenureName").split("-");
                        int Start = Integer.parseInt(arrOfStr[0]);
                        int End = Integer.parseInt(arrOfStr[1]);
                        for (int j = Start; j <= End; j++) {
                            tenure_list.add(String.valueOf(j));
                        }
                    } else {
                        //tenure_id.put(jsonObject.getString("TenureName"), jsonObject.getString("Tenure"));
                        tenure_list.add(jsonObject.getString("TenureName"));
                    }

            }
            tenure_adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorNominee Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTenureT(String responce) {
        /*[{"Type":"0","TypeName":"Select"},{"Type":"01","TypeName":"Cumulative"},{"Type":"1","TypeName":"Yearly"},{"Type":"12","TypeName":"Monthly"},{"Type":"2","TypeName":"Half Yearly"},{"Type":"4","TypeName":"Quarterly"}]*/
        try {
            if (!tenureT_list.isEmpty()) {
                tenureT_list.clear();
                tenureT_id.clear();
            }
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.has("TypeName"))
                    tenureT_list.add(jsonObject.getString("TypeName"));
                if (jsonObject.has("TypeName"))
                    tenureT_id.put(jsonObject.getString("TypeName"), jsonObject.getString("Type"));
            }
            tenureT_adapter.notifyDataSetChanged();
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorNominee Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class getDetailCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getInvestorNominee);
                Request.addProperty("Registration_Id", Registration_Id);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorNominee);

                transport.call(SOAP_ACTION + METHOD_NAME_getInvestorNominee, soapEnvelope);
                Nominee_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                // Log.i(TAG, "getDetailCallWS_Result : " + Nominee_resultString);
            } catch (Exception ex) {
                Log.e(TAG, "getDetailCallWS Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getDetailCallWS Result " + Nominee_resultString.toString());
                getInvestorNominee(Nominee_resultString.toString());
            } catch (NullPointerException e) {
                Log.d(TAG, "getDetailCallWS Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class getSchemeName extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "Scheme doInBackground " + Company_ID);
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getScheme);
                Request.addProperty("Company_ID", Company_ID);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_getScheme, soapEnvelope);
                Scheme_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //  Log.i(TAG, "getSchemeName_Result : " + Scheme_resultString);

            } catch (Exception ex) {
                Log.e(TAG, "getSchemeName Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getSchemeName Result " + Scheme_resultString.toString());
                getSchemeName(Scheme_resultString.toString());
            } catch (NullPointerException e) {
                Log.d(TAG, "getSchemeName Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class getVRates extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "getVRates doInBackground " + Company_ID + " " + Scheme_ID);
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getVRates);
                Request.addProperty("Company_ID", Company_ID);
                Request.addProperty("Scheme_ID", Scheme_ID);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_getVRates, soapEnvelope);
                Scheme_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //  Log.i(TAG, "getSchemeName_Result : " + Scheme_resultString);

            } catch (Exception ex) {
                Log.e(TAG, "getVRates Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getVRates Result " + Scheme_resultString.toString());
                getVRates(Scheme_resultString.toString());
            } catch (NullPointerException e) {
                Log.d(TAG, "getVRates Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class getTenureB extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "getVRates doInBackground " + Company_ID);
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getTenureB);
                Request.addProperty("Company_ID", Company_ID);
                Request.addProperty("Scheme_ID", Scheme_ID);
                Request.addProperty("Scheme_Sub_Category", Scheme_Sub_Category);
                Request.addProperty("FlagVariableRates", FlagVariableRates);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_getTenureB, soapEnvelope);
                Scheme_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //  Log.i(TAG, "getSchemeName_Result : " + Scheme_resultString);

            } catch (Exception ex) {
                Log.e(TAG, "getTenureB Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getTenureB Result " + Scheme_resultString.toString());
                getTenureB(Scheme_resultString.toString());
            } catch (NullPointerException e) {
                Log.d(TAG, "getTenureB Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class getTenureT extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "getTenureT doInBackground " + Company_ID);
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getTenureTB);
                Request.addProperty("Company_ID", Company_ID);
                Request.addProperty("Scheme_ID", Scheme_ID);
                Request.addProperty("Scheme_Sub_Category", Scheme_Sub_Category);
                Request.addProperty("Tenure", Tenure);
                Request.addProperty("FlagVariableRates", Tenure);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_getTenureTB, soapEnvelope);
                Scheme_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //  Log.i(TAG, "getSchemeName_Result : " + Scheme_resultString);

            } catch (Exception ex) {
                Log.e(TAG, "getTenureT Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getTenureT Result " + Scheme_resultString.toString());
                getTenureT(Scheme_resultString.toString());
            } catch (NullPointerException e) {
                Log.d(TAG, "getTenureT Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class updateData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "updateData doInBackground " + Company_ID + " " + Scheme_ID + " " + Scheme_Sub_Category + " " + Tenure + " " + TenureType + " ");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_updateDataBind);
                Request.addProperty("Company_ID", Company_ID);
                Request.addProperty("Scheme_ID", Scheme_ID);
                Request.addProperty("Scheme_Sub_Category", Scheme_Sub_Category);
                Request.addProperty("Tenure", Tenure);
                Request.addProperty("Type", TenureType);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_updateDataBind, soapEnvelope);
                Scheme_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //  Log.i(TAG, "getSchemeName_Result : " + Scheme_resultString);

            } catch (Exception ex) {
                Log.e(TAG, "updateData Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "updateData Result " + Scheme_resultString.toString());
                //getInvestorNominee(resultString.toString());
                /*[{"Company_ID":"000001","Minimum_Amount":"40000.00","Rate_of_Interest":"7.65","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.70","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.75","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"40000.00","Rate_of_Interest":"7.65","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.70","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.75","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""}]*/
                JSONArray jsonArray = new JSONArray(Scheme_resultString.toString());
                for (int i = 0; i < 1; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("Minimum_Amount")) {
                        Minimum_Amount = Double.valueOf(jsonObject.getInt("Minimum_Amount"));
                        layout_tv_minimum_amount.setHint(getResources().getString(R.string.investment_amount_minimum_amount) + "(" + jsonObject.getString("Minimum_Amount") + ")");
                    }
                    if (jsonObject.has("Rate_of_Interest")) {
                        tv_rate_of_inte.setText(getResources().getString(R.string.interest_rate) + " " + jsonObject.getString("Rate_of_Interest"));
                        interestRate = jsonObject.getString("Rate_of_Interest");
                    }
                }
            } catch (NullPointerException | JSONException e) {
                Log.d(TAG, "updateData Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class buyFixData extends AsyncTask<Void, Void, Void> {
        /*<id>string</id>
      <Investor_Registration_Id>string</Investor_Registration_Id>
      <Investor_Customer_Code>string</Investor_Customer_Code>
      <Issuer>string</Issuer>
      <Scheme_Id>string</Scheme_Id>
      <Tenure>string</Tenure>
      <Type>string</Type>
      <Investment_Amount>string</Investment_Amount>
      <Minimum_Amount>string</Minimum_Amount>
      <Investment_Start_Date>string</Investment_Start_Date>
      <Interest_Rate>string</Interest_Rate>
      <Maturity_Value>string</Maturity_Value>
      <Date_Of_Maturity>string</Date_Of_Maturity>
      <Payment_Mode>string</Payment_Mode>
      <Payment_Mode_Other>string</Payment_Mode_Other>
      <Is_Senior_Citizen>string</Is_Senior_Citizen>
      <Nominee_Name>string</Nominee_Name>
      <Nominee_Name_Other>string</Nominee_Name_Other>
      <Nominee_Address>string</Nominee_Address>
      <Nominee_Relationship>string</Nominee_Relationship>*/
        @Override
        protected Void doInBackground(Void... params) {

            Log.i(TAG, "updateData doInBackground " + Registration_Id + " " + Issuer + " " + Scheme_ID + " " + Tenure +
                    " " + TenureType + " " + tv_minimum_amount.getText() + " " + Minimum_Amount + " " + Investment_Start_Date +
                    " " + interestRate + " " + tv_maturity_amount.getText() + " " + Date_Of_Maturity + " " + Payment_Mode + " " + ed_payment.getText() +
                    " " + Is_Senior_Citizen + " " + Nominee_Name + " " + Nominee_Relationship);
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_postFixedDeposit);
                Request.addProperty("id", "");
                Request.addProperty("Investor_Registration_Id", Registration_Id);
                Request.addProperty("Investor_Customer_Code", "");
                Request.addProperty("Issuer", Issuer);
                Request.addProperty("Scheme_Id", Scheme_ID);
                Request.addProperty("Tenure", Tenure);
                Request.addProperty("Type", TenureType);
                Request.addProperty("Investment_Amount", tv_minimum_amount.getText().toString());
                Request.addProperty("Minimum_Amount", String.valueOf(Minimum_Amount));
                Request.addProperty("Investment_Start_Date", Investment_Start_Date);
                Request.addProperty("Interest_Rate", interestRate);
                Request.addProperty("Maturity_Value", tv_maturity_amount.getText().toString());
                Request.addProperty("Date_Of_Maturity", Date_Of_Maturity);
                Request.addProperty("Payment_Mode", Payment_Mode);
                Request.addProperty("Payment_Mode_Other", ed_payment.getText().toString());
                Request.addProperty("Is_Senior_Citizen", Is_Senior_Citizen);
                Request.addProperty("Nominee_Name", Nominee_Name);
                Request.addProperty("Nominee_Name_Other", "");
                Request.addProperty("Nominee_Address", "55");
                Request.addProperty("Nominee_Relationship", "hhhh");

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_postFixedDeposit, soapEnvelope);
                Scheme_resultString = (SoapPrimitive) soapEnvelope.getResponse();

                //  Log.i(TAG, "getSchemeName_Result : " + Scheme_resultString);

            } catch (Exception ex) {
                Log.e(TAG, "buyFixData Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "buyFixData Result " + Scheme_resultString.toString());
            /*try {
                Log.d(TAG, "buyFixData Result " + Scheme_resultString.toString());
                //getInvestorNominee(resultString.toString());
                *//*[{"Company_ID":"000001","Minimum_Amount":"40000.00","Rate_of_Interest":"7.65","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.70","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.75","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"40000.00","Rate_of_Interest":"7.65","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.70","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.75","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""},{"Company_ID":"000001","Minimum_Amount":"20000.00","Rate_of_Interest":"7.90","Senior_Citizen":"0.25","Scheme_Sub_Category":""}]*//*
                JSONArray jsonArray = new JSONArray(Scheme_resultString.toString());
                for (int i = 0; i < 1; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("Minimum_Amount")) {
                        Minimum_Amount = (double) jsonObject.getInt("Minimum_Amount");
                        layout_tv_minimum_amount.setHint(getResources().getString(R.string.investment_amount_minimum_amount) + "(" + jsonObject.getString("Minimum_Amount") + ")");
                    }
                    if (jsonObject.has("Rate_of_Interest")) {
                        tv_rate_of_inte.setText(String.format("%s %s", getResources().getString(R.string.interest_rate), jsonObject.getString("Rate_of_Interest")));
                        interestRate = jsonObject.getString("Rate_of_Interest");
                    }
                }
            } catch (NullPointerException | JSONException e) {
                Log.d(TAG, "buyFixData Error:- " + e.getMessage());
                ShowDilog(BuyFixedDepositActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }*/
        }
    }
}

/*double P, R, N, finalAmount, A, T;
        P = principal;
        R = interestRate;
        N = timesPerYear;
        T = years;
        A = (P * Math.Pow((1 + (R / (N * 100))), (N * T)));
        return finalAmount = A;*/