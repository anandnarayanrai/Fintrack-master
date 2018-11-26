package com.edge.fintrack.account_detail;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.edge.fintrack.Api_Class.METHOD_NAME_getCity;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorBank;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorCommunication;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorNominee;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorProfile;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getState;
import static com.edge.fintrack.Api_Class.METHOD_NAME_updateInvestorBank;
import static com.edge.fintrack.Api_Class.METHOD_NAME_updateInvestorCommunication;
import static com.edge.fintrack.Api_Class.METHOD_NAME_updateInvestorNominee;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_InvestorNominee;
import static com.edge.fintrack.Api_Class.URL_InvestorProfileCommunication;
import static com.edge.fintrack.Api_Class.URL_InvestorViewProfile;
import static com.edge.fintrack.Api_Class.URL_getInvestorBankDetail;
import static com.edge.fintrack.Api_Class.URL_state_and_city;
import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isInternetOn;

public class UpdateProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final String TAG = "UpdateProfileActivity";
    ImageView iv_menu_item;
    TextView et_date_of_birth, et_n_date_of_birth;
    DatePickerDialog datePickerDialog;
    EditText et_pan, et_rpan;

    //create a list of items for the spinner.
    /*String[] spinner_gender_items = new String[]{"Select", "Male", "Female", "Other"};
    String[] _spinner_marital_statusitems = new String[]{"Select", "Single", "Married"};
    String[] items = new String[]{"Select", "Item1", "Item2", "Item3", "Item4", "Item5"};*/

    String layout_name;
    LinearLayout layout_Investror, layout_Address, layout_Nominee, layout_BankDetail;

    SoapPrimitive resultString;
    // Session Manager Class
    SessionManager session;
    String Registration_Id;
    Spinner spinner_gender, spinner_marital_status, spinner_investor_name, spinner_country, spinner_state, spinner_city, spinner_bankname, spinner_AccountType, spinner_account_holding;

    ArrayList<String> statearrayList = new ArrayList<>();
    Map<String, String> state_id = new HashMap<String, String>();
    Map<String, String> state_name = new HashMap<String, String>();
    ArrayList<String> cityarrayList = new ArrayList<>();
    Map<String, String> city_id = new HashMap<String, String>();
    Map<String, String> city_name = new HashMap<String, String>();

    ArrayList<String> codeid_list = new ArrayList<>();
    ArrayAdapter<String> country_adapter;
    ArrayAdapter<String> state_adapter;
    ArrayAdapter<String> city_adapter;
    String userCountry, userState, userCity;

    AutoCompleteTextView ac_country, et_state, et_city;

    ArrayList<String> invwster_list = new ArrayList<>();
    private String Country_Communication = "IN", City_Communication, Communication_Address, Communication_AddressLine2, Zip_Communication, Telephone_Communication, Mobile_No, Email_Id;
    String Cust_Bankname, Cust_AccountType, Cust_HoldingMode;
    String[] country = {"India"};
    String Nominee_Name, Nominee_DOB, Nominee_Relation, Guardian_Name;
    private ProgressDialog mProgressDialog;
    CountryCodePicker ccp;
    private String CountryCode;
    SoapPrimitive ProfileDtaResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_update_profile);
        // making notification bar transparent
        // changeStatusBarColor();

        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layout_Investror = (LinearLayout) findViewById(R.id.layout_Investror);
        layout_Address = (LinearLayout) findViewById(R.id.layout_Address);
        layout_Nominee = (LinearLayout) findViewById(R.id.layout_Nominee);
        layout_BankDetail = (LinearLayout) findViewById(R.id.layout_BankDetail);

        ac_country = (AutoCompleteTextView) findViewById(R.id.ac_country);
        et_state = (AutoCompleteTextView) findViewById(R.id.et_state);
        et_city = (AutoCompleteTextView) findViewById(R.id.et_city);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                CountryCode = selectedCountry.getPhoneCode();
               // Toast.makeText(UpdateProfileActivity.this, "Updated " + selectedCountry.getPhoneCode(), Toast.LENGTH_SHORT).show();
            }
        });

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        Registration_Id = user.get(SessionManager.KEY_Registration_Id);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            layout_name = null;
        } else {
            layout_name = bundle.getString("layout");
            if (layout_name.equalsIgnoreCase("layout_investor")) {
                layout_Investror.setVisibility(View.VISIBLE);
                layout_Address.setVisibility(View.GONE);
                layout_Nominee.setVisibility(View.GONE);
                layout_BankDetail.setVisibility(View.GONE);
                if (!isInternetOn(UpdateProfileActivity.this)) {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                } else {
                    getDetailCallWS nomineeDetailCallWS = new getDetailCallWS(METHOD_NAME_getInvestorBank, URL_getInvestorBankDetail);
                    nomineeDetailCallWS.execute();
                }

            } else if (layout_name.equalsIgnoreCase("layout_bank")) {
                layout_Investror.setVisibility(View.GONE);
                layout_Address.setVisibility(View.GONE);
                layout_Nominee.setVisibility(View.GONE);
                layout_BankDetail.setVisibility(View.VISIBLE);
                if (!isInternetOn(UpdateProfileActivity.this)) {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                } else {
                    getDetailCallWS nomineeDetailCallWS = new getDetailCallWS(METHOD_NAME_getInvestorBank, URL_getInvestorBankDetail);
                    nomineeDetailCallWS.execute();
                }

            } else if (layout_name.equalsIgnoreCase("layout_address")) {
                layout_Nominee.setVisibility(View.GONE);
                layout_Investror.setVisibility(View.GONE);
                layout_Address.setVisibility(View.VISIBLE);
                layout_BankDetail.setVisibility(View.GONE);
                if (!isInternetOn(UpdateProfileActivity.this)) {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                } else {
                    getDetailCallWS nomineeDetailCallWS = new getDetailCallWS(METHOD_NAME_getInvestorCommunication, URL_InvestorProfileCommunication);
                    nomineeDetailCallWS.execute();
                }

            } else if (layout_name.equalsIgnoreCase("layout_nominee")) {
                layout_Nominee.setVisibility(View.VISIBLE);
                layout_Investror.setVisibility(View.GONE);
                layout_Address.setVisibility(View.GONE);
                layout_BankDetail.setVisibility(View.GONE);
                if (!isInternetOn(UpdateProfileActivity.this)) {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                } else {
                    getDetailCallWS nomineeDetailCallWS = new getDetailCallWS(METHOD_NAME_getInvestorNominee, URL_InvestorNominee);
                    nomineeDetailCallWS.execute();
                }
            }
        }
        et_pan = (EditText) findViewById(R.id.et_pan);
        et_rpan = (EditText) findViewById(R.id.et_rpan);
        et_pan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et_rpan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        et_date_of_birth = (TextView) findViewById(R.id.et_date_of_birth);
        et_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                et_date_of_birth.setText(getResources().getString(R.string.date_of_birth) + " :- " + dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                et_date_of_birth.setTextColor(getResources().getColor(R.color.colorBlack));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
                datePickerDialog.show();
            }
        });

        et_n_date_of_birth = (TextView) findViewById(R.id.et_n_date_of_birth);
        et_n_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                et_n_date_of_birth.setText(getResources().getString(R.string.date_of_birth) + " " + dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                Nominee_DOB = dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year;
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
                datePickerDialog.show();
            }
        });

        //get the spinner from the xml.
        spinner_gender = findViewById(R.id.spinner_gender);
        spinner_gender.setOnItemSelectedListener(this);
        spinner_marital_status = findViewById(R.id.spinner_marital_status);
        spinner_marital_status.setOnItemSelectedListener(this);

        spinner_country = findViewById(R.id.spinner_country);
        spinner_country.setOnItemSelectedListener(this);

        spinner_state = findViewById(R.id.spinner_state);
        spinner_state.setOnItemSelectedListener(this);
        spinner_state.setEnabled(false);

        spinner_city = findViewById(R.id.spinner_city);
        spinner_city.setOnItemSelectedListener(this);
        spinner_state.setEnabled(false);

        spinner_bankname = findViewById(R.id.spinner_bankname);
        spinner_bankname.setOnItemSelectedListener(this);

        spinner_AccountType = findViewById(R.id.spinner_AccountType);
        spinner_AccountType.setOnItemSelectedListener(this);

        spinner_account_holding = findViewById(R.id.spinner_account_holding);
        spinner_account_holding.setOnItemSelectedListener(this);

        spinner_investor_name = findViewById(R.id.spinner_investor_name);
        spinner_investor_name.setOnItemSelectedListener(this);

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        /* ArrayAdapter<String> spinner_gender_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinner_gender_items);
        spinner_gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> spinner_marital_status_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, _spinner_marital_statusitems);
        spinner_marital_status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the spinners adapter to the previously created one.
        spinner_gender.setAdapter(spinner_gender_adapter);
        spinner_marital_status.setAdapter(spinner_marital_status_adapter);*/
        invwster_list.add(user.get(SessionManager.KEY_NAME));
        ArrayAdapter<String> item_adapter = new ArrayAdapter<>(UpdateProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, invwster_list);
        item_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_investor_name.setAdapter(item_adapter);


        country_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, country);
        ac_country.setThreshold(1);
        ac_country.setAdapter(country_adapter);
        ac_country.setText(country[0]);
        ac_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(UpdateProfileActivity.this, country_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                if (country_adapter.getItem(position).equalsIgnoreCase("India")) {
                    Country_Communication = "IN";
                    if (!isInternetOn(UpdateProfileActivity.this)) {
                        ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                    } else {
                        getState getState = new getState();
                        getState.execute();
                    }
                }
            }
        });

        //et_state.setText(user.get(SessionManager.KEY_State_Communication));
        state_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, statearrayList);
        et_state.setThreshold(1);//will start working from first character
        et_state.setAdapter(state_adapter);//setting the adapter data into the AutoCompleteTextView
        // et_state.setTextColor(Color.RED);
        et_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Toast.makeText(TermInsuranceActivity.this, state_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                //State_Communication = state_id.get(parent.getItemAtPosition(position).toString());
                if (!isInternetOn(UpdateProfileActivity.this)) {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                } else {
                    et_city.setText("");
                    userState = state_id.get(state_adapter.getItem(position).toString());
                    getCity getCity = new getCity(userState);
                    getCity.execute();
                  //  Toast.makeText(UpdateProfileActivity.this, "" + userState, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //et_city.setText(user.get(SessionManager.KEY_City_Communication));
        city_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, cityarrayList);
        et_city.setThreshold(1);//will start working from first character
        et_city.setAdapter(city_adapter);//setting the adapter data into the AutoCompleteTextView
        et_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                City_Communication = city_adapter.getItem(position).toString();
                //Toast.makeText(UpdateProfileActivity.this, city_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));

            /*// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/

            View decor = getWindow().getDecorView();
            // decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public void onProfileUpdate(View view) {
        if (layout_name.equalsIgnoreCase("layout_investor")) {

        } else if (layout_name.equalsIgnoreCase("layout_bank")) {
            if (isInvestorBankValidForm()) {
                addInvestorBank addInvestorBank = new addInvestorBank();
                addInvestorBank.execute();
            }
        } else if (layout_name.equalsIgnoreCase("layout_address")) {
            if (isCommunicationValidForm()) {
                addInvestorCommunication addInvestorCommunication = new addInvestorCommunication();
                addInvestorCommunication.execute();
            }
        } else if (layout_name.equalsIgnoreCase("layout_nominee")) {
            if (isNomineeDetailValidForm()) {
                addNomineeDetailCallWS nomineeDetailCallWS = new addNomineeDetailCallWS();
                nomineeDetailCallWS.execute();
            }
        }
    }

    private boolean isInvestorBankValidForm() {
        if (((EditText) findViewById(R.id.et_account)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_account)).requestFocus();
            ((EditText) findViewById(R.id.et_account)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_ifsc)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_ifsc)).requestFocus();
            ((EditText) findViewById(R.id.et_ifsc)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_bankcity)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_bankcity)).requestFocus();
            ((EditText) findViewById(R.id.et_bankcity)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_bankbranch)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_telephone)).requestFocus();
            ((EditText) findViewById(R.id.et_telephone)).setError(getString(R.string.field_required));
            return false;
        } else if (!isInternetOn(UpdateProfileActivity.this)) {
            ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    private boolean isCommunicationValidForm() {
        if (((EditText) findViewById(R.id.et_address1)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_address1)).requestFocus();
            ((EditText) findViewById(R.id.et_address1)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_address2)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_address2)).requestFocus();
            ((EditText) findViewById(R.id.et_address2)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_pincode)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_pincode)).requestFocus();
            ((EditText) findViewById(R.id.et_pincode)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_telephone)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_telephone)).requestFocus();
            ((EditText) findViewById(R.id.et_telephone)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_mobile)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_mobile)).requestFocus();
            ((EditText) findViewById(R.id.et_mobile)).setError(getString(R.string.field_required));
            return false;
        } else if (!isInternetOn(UpdateProfileActivity.this)) {
            ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    private boolean isNomineeDetailValidForm() {
        if (((EditText) findViewById(R.id.et_nominee_name)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_nominee_name)).requestFocus();
            ((EditText) findViewById(R.id.et_nominee_name)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_parent_guardian)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_parent_guardian)).requestFocus();
            ((EditText) findViewById(R.id.et_parent_guardian)).setError(getString(R.string.field_required));
            return false;
        } else if (((EditText) findViewById(R.id.et_relationship)).getText().toString().isEmpty()) {
            ((EditText) findViewById(R.id.et_relationship)).requestFocus();
            ((EditText) findViewById(R.id.et_relationship)).setError(getString(R.string.field_required));
            return false;
        } else if (!isInternetOn(UpdateProfileActivity.this)) {
            ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        switch (parent.getId()) {
            case R.id.spinner_gender:
                item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinner_gender_items" + item);

                if (item.equalsIgnoreCase("Select")) {
                    spinner_gender.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_gender.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
              /*  switch (position) {
                    case 0:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_gender_items" + item);
                        //   Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_gender_items" + item);
                        //Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_gender_items" + item);
                        //Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_gender_items" + item);
                        //Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                }*/
                break;

            case R.id.spinner_marital_status:

                item = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "spinner_gender_items" + item);
                if (item.equalsIgnoreCase("Select")) {
                    spinner_marital_status.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_marital_status.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                /*switch (position) {
                    case 0:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_marital_status" + item);
                        // Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_marital_status" + item);
                        //Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG, "spinner_marital_status" + item);
                        //Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
                        break;
                }*/
                break;
            case R.id.spinner_country:
                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Select")) {
                    spinner_country.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                    spinner_state.setEnabled(true);
                    Country_Communication = parent.getItemAtPosition(position).toString();


                    if (!isInternetOn(UpdateProfileActivity.this)) {
                        ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                    } else {
                        getState getState = new getState();
                        getState.execute();
                    }
                } else {
                    spinner_country.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                    spinner_state.setEnabled(false);
                    spinner_city.setEnabled(false);
                }
                break;

            case R.id.spinner_state:
                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Select")) {
                    spinner_city.setEnabled(true);
                    spinner_state.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                    userState = state_id.get(parent.getItemAtPosition(position).toString());
                    if (!isInternetOn(UpdateProfileActivity.this)) {
                        ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                    } else {
                       /* getCity getCity = new getCity(State_Communication);
                        getCity.execute();*/
                    }
                } else {
                    spinner_city.setEnabled(false);
                    spinner_state.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                }

                Toast.makeText(this, "" + userState, Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner_city:
                if (!parent.getItemAtPosition(position).toString().equalsIgnoreCase("Select")) {
                    // City_Communication = city_id.get(parent.getItemAtPosition(position).toString());
                    spinner_city.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                } else {
                    spinner_city.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                }

                break;
            case R.id.spinner_bankname:
                Cust_Bankname = parent.getItemAtPosition(position).toString();
                if (Cust_Bankname.equalsIgnoreCase("Select")) {
                    spinner_bankname.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_bankname.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                //Toast.makeText(this, "" + Cust_Bankname, Toast.LENGTH_SHORT).show();

                break;
            case R.id.spinner_AccountType:
                Cust_AccountType = parent.getItemAtPosition(position).toString();
                if (Cust_AccountType.equalsIgnoreCase("Select")) {
                    spinner_AccountType.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_AccountType.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                break;
            case R.id.spinner_account_holding:
                Cust_HoldingMode = parent.getItemAtPosition(position).toString();
                if (Cust_HoldingMode.equalsIgnoreCase("Select")) {
                    spinner_account_holding.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_account_holding.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                break;

            case R.id.spinner_investor_name:
                Cust_HoldingMode = parent.getItemAtPosition(position).toString();
                if (Cust_HoldingMode.equalsIgnoreCase("Select")) {
                    spinner_account_holding.setBackground(getResources().getDrawable(R.drawable.spinner_clicked));
                } else {
                    spinner_account_holding.setBackground(getResources().getDrawable(R.drawable.spinner_enabled));
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void getInvestorBank(String responce) {
        try {
            /**
             * [{"Registration_Id":"FIN20186161032923","Cust_Bankname":"0","Cust_AccountType":"0","Cust_HoldingMode":"0",
             * "Cust_AccountNumber":"","Cust_IFSCCode":"","Cust_BankCity":"","Cust_BranchAddress":""}]
             * */
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("Cust_AccountNumber"))
                    ((EditText) findViewById(R.id.et_account)).setText(jsonObject.getString("Cust_AccountNumber"));
                Toast.makeText(this, "" + jsonObject.getString("Cust_AccountNumber"), Toast.LENGTH_SHORT).show();
                if (jsonObject.has("Cust_IFSCCode"))
                    ((EditText) findViewById(R.id.et_ifsc)).setText(jsonObject.getString("Cust_IFSCCode"));
                if (jsonObject.has("Cust_BankCity"))
                    ((EditText) findViewById(R.id.et_bankcity)).setText(jsonObject.getString("Cust_BankCity"));
                if (jsonObject.has("Cust_BranchAddress"))
                    ((EditText) findViewById(R.id.et_bankbranch)).setText(jsonObject.getString("Cust_BranchAddress"));
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorBank Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getInvestorCommunication(String responce) {
        try {
            /**
             * [{"Registration_Id":"FIN20186161032923","Communication_Address":"","Communication_AddressLine2":"",
             * "City_Communication":"","Zip_Communication":"","State_Communication":"","Country_Communication":"","Telephone_Communication":"","Mobile_No":"9599308185","Email_Id":"anandnrai@gmail.com"}]
             * */
            JSONArray jsonArray = new JSONArray(responce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.has("Communication_Address"))
                    ((TextView) findViewById(R.id.et_address1)).setText(jsonObject.getString("Communication_Address"));
                if (jsonObject.has("Communication_AddressLine2"))
                    ((TextView) findViewById(R.id.et_address2)).setText(jsonObject.getString("Communication_AddressLine2"));
                if (jsonObject.has("Country_Communication")) {
                    /*if (jsonObject.getString("Country_Communication").equalsIgnoreCase("IN"))
                        ac_country.setText(country[0]);*/
                }
                if (jsonObject.has("State_Communication")) {
                    userState = jsonObject.getString("State_Communication");
                }
                if (jsonObject.has("City_Communication")) {
                    City_Communication = jsonObject.getString("City_Communication");
                }
                if (jsonObject.has("Zip_Communication"))
                    ((TextView) findViewById(R.id.et_pincode)).setText(jsonObject.getString("Zip_Communication"));
                if (jsonObject.has("Telephone_Communication"))
                    ((TextView) findViewById(R.id.et_telephone)).setText(jsonObject.getString("Telephone_Communication"));
                if (jsonObject.has("Mobile_No"))
                    ((TextView) findViewById(R.id.et_mobile)).setText(jsonObject.getString("Mobile_No"));

                getState getState = new getState();
                getState.execute();
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorCommunication Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
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
                    ((TextView) findViewById(R.id.et_nominee_name)).setText(jsonObject.getString("Nominee_Name"));
                if (jsonObject.has("Guardian_Name"))
                    ((TextView) findViewById(R.id.et_parent_guardian)).setText(jsonObject.getString("Guardian_Name"));
                if (jsonObject.has("Nominee_Relation"))
                    ((TextView) findViewById(R.id.et_relationship)).setText(jsonObject.getString("Nominee_Relation"));
                if (jsonObject.has("Nominee_DOB"))
                    ((TextView) findViewById(R.id.et_n_date_of_birth)).setText(getResources().getString(R.string.date_of_birth) + " " + jsonObject.getString("Nominee_DOB"));
            }
        } catch (NullPointerException e) {
            Log.d(TAG, "getInvestorNominee Error:- " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class getDetailCallWS extends AsyncTask<Void, Void, Void> {

        String METHOD_NAME, URL;

        getDetailCallWS(String METHOD_NAME, String URL) {
            this.METHOD_NAME = METHOD_NAME;
            this.URL = URL;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
                Request.addProperty("Registration_Id", Registration_Id);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL);

                transport.call(SOAP_ACTION + METHOD_NAME, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "getDetailCallWS_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "getDetailCallWS Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getDetailCallWS Result " + resultString.toString());

                if (layout_name.equalsIgnoreCase("layout_investor")) {

                } else if (layout_name.equalsIgnoreCase("layout_bank")) {
                    getInvestorBank(resultString.toString());
                } else if (layout_name.equalsIgnoreCase("layout_address")) {
                    getInvestorCommunication(resultString.toString());
                } else if (layout_name.equalsIgnoreCase("layout_nominee")) {
                    getInvestorNominee(resultString.toString());
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "getDetailCallWS Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class addNomineeDetailCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Nominee_Name = ((EditText) findViewById(R.id.et_nominee_name)).getText().toString();
            Guardian_Name = ((EditText) findViewById(R.id.et_parent_guardian)).getText().toString();
            Nominee_Relation = ((EditText) findViewById(R.id.et_relationship)).getText().toString();

            mProgressDialog = ProgressDialog.show(UpdateProfileActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_updateInvestorNominee);
                Request.addProperty("Registration_Id", Registration_Id);
                Request.addProperty("Nominee_Name", Nominee_Name);
                Request.addProperty("Nominee_DOB", Nominee_DOB);
                Request.addProperty("Nominee_Relation", Nominee_Relation);
                Request.addProperty("Guardian_Name", Guardian_Name);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorNominee);

                transport.call(SOAP_ACTION + METHOD_NAME_updateInvestorNominee, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "NomineeDetailAddUpdateRecord_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "InvestorProfileAddUpdateBind Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {
                Log.d(TAG, "NomineeDetailAddUpdateRecord Result " + resultString.toString());
                if (resultString.toString().equalsIgnoreCase("success")) {
                    UserprofilegCallWS userprofilegCallWS = new UserprofilegCallWS(Registration_Id);
                    userprofilegCallWS.execute();
                    Toast.makeText(UpdateProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                }
                /*JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String UserId = jsonObject.getString("UserId");
                    mProgressDialog.hide();
                    if (UserId.equalsIgnoreCase("-1")) {
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "User are not activated !! We have send an email, please activated account first.");
                    } else if (UserId.equalsIgnoreCase("-2")) {
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        LoginActivity.this.finish();
                        //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();
                    }
                }*/
            } catch (NullPointerException e) {
                Log.d(TAG, "NomineeDetailAddUpdateRecord Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class addInvestorBank extends AsyncTask<Void, Void, Void> {
        String Cust_AccountNumber, Cust_IFSCCode, Cust_BankCity, Cust_BranchAddress;

        @Override
        protected void onPreExecute() {
            Cust_AccountNumber = ((EditText) findViewById(R.id.et_account)).getText().toString();
            Cust_IFSCCode = ((EditText) findViewById(R.id.et_ifsc)).getText().toString();
            Cust_BankCity = ((EditText) findViewById(R.id.et_bankcity)).getText().toString();
            Cust_BranchAddress = ((EditText) findViewById(R.id.et_bankbranch)).getText().toString();

            mProgressDialog = ProgressDialog.show(UpdateProfileActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_updateInvestorBank);
                Request.addProperty("Registration_Id", Registration_Id);
                Request.addProperty("Cust_Bankname", Cust_Bankname);
                Request.addProperty("Cust_AccountType", Cust_AccountType);
                Request.addProperty("Cust_HoldingMode", Cust_HoldingMode);
                Request.addProperty("Cust_AccountNumber", Cust_AccountNumber);
                Request.addProperty("Cust_IFSCCode", Cust_IFSCCode);
                Request.addProperty("Cust_BankCity", Cust_BankCity);
                Request.addProperty("Cust_BranchAddress", Cust_BranchAddress);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_getInvestorBankDetail);

                transport.call(SOAP_ACTION + METHOD_NAME_updateInvestorBank, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "addInvestorBank_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "addInvestorBankd Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {
                Log.d(TAG, "addInvestorBank Result " + resultString.toString());
                if (resultString.toString().equalsIgnoreCase("success")) {
                    UserprofilegCallWS userprofilegCallWS = new UserprofilegCallWS(Registration_Id);
                    userprofilegCallWS.execute();
                    Toast.makeText(UpdateProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                }
                /*JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String UserId = jsonObject.getString("UserId");
                    mProgressDialog.hide();
                    if (UserId.equalsIgnoreCase("-1")) {
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "User are not activated !! We have send an email, please activated account first.");
                    } else if (UserId.equalsIgnoreCase("-2")) {
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        LoginActivity.this.finish();
                        //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();
                    }
                }*/
            } catch (NullPointerException e) {
                Log.d(TAG, "addInvestorBank Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class addInvestorCommunication extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Communication_Address = ((EditText) findViewById(R.id.et_address1)).getText().toString();
            Communication_AddressLine2 = ((EditText) findViewById(R.id.et_address2)).getText().toString();
            Zip_Communication = ((EditText) findViewById(R.id.et_pincode)).getText().toString();
            Telephone_Communication = ((EditText) findViewById(R.id.et_telephone)).getText().toString();
            Mobile_No = ((EditText) findViewById(R.id.et_mobile)).getText().toString();

            mProgressDialog = ProgressDialog.show(UpdateProfileActivity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_updateInvestorCommunication);
                Request.addProperty("Registration_Id", Registration_Id);
                Request.addProperty("Communication_Address", Communication_Address);
                Request.addProperty("Communication_AddressLine2", Communication_AddressLine2);
                Request.addProperty("City_Communication", City_Communication);
                Request.addProperty("Zip_Communication", Zip_Communication);
                Request.addProperty("State_Communication", userState);
                Request.addProperty("Country_Communication", Country_Communication);
                Request.addProperty("Telephone_Communication", Telephone_Communication);
                Request.addProperty("Mobile_No", Mobile_No);

                Log.i(TAG, "addInvestorCommunication  SoapObject Request : " + Request.toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorProfileCommunication);

                transport.call(SOAP_ACTION + METHOD_NAME_updateInvestorCommunication, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "addInvestorCommunication_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "addInvestorCommunication Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {
                Log.d(TAG, "addInvestorCommunication Result " + resultString.toString());

                if (resultString.toString().equalsIgnoreCase("success")) {
                    UserprofilegCallWS userprofilegCallWS = new UserprofilegCallWS(Registration_Id);
                    userprofilegCallWS.execute();
                    Toast.makeText(UpdateProfileActivity.this, "Profile Update Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                }
                /*JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String UserId = jsonObject.getString("UserId");
                    mProgressDialog.hide();
                    if (UserId.equalsIgnoreCase("-1")) {
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "User are not activated !! We have send an email, please activated account first.");
                    } else if (UserId.equalsIgnoreCase("-2")) {
                        ShowDilog(LoginActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        LoginActivity.this.finish();
                        //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();
                    }
                }*/
            } catch (NullPointerException e) {
                Log.d(TAG, "addInvestorCommunication Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class getState extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!statearrayList.isEmpty()) {
                statearrayList.clear();
                state_id.clear();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getState);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL_state_and_city);

                transport.call(SOAP_ACTION + METHOD_NAME_getState, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "getStateResult : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "getState Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getState Result " + resultString.toString());
                /**
                 *
                 * [{"Code":"0","Description":"Select","DISTRICTID":null,"DISTRICT":null},
                 * {"Code":"AN","Description":"Andaman & Nicobar","DISTRICTID":null,"DISTRICT":null},
                 * {"Code":"AP","Description":"Andhra Pradesh","DISTRICTID":null,"DISTRICT":null},
                 * {"Code":"AR","Description":"Arunachal Pradesh","DISTRICTID":null,"DISTRICT":null},
                 * {"Code":"AS","Description":"Assam","DISTRICTID":null,"DISTRICT":null},{"Code":"BR","Description":"Bihar","DISTRICTID":null,"DISTRICT":null},{"Code":"CG","Description":"Chattisgarh","DISTRICTID":null,"DISTRICT":null},{"Code":"CH","Description":"Chandigarh","DISTRICTID":null,"DISTRICT":null},{"Code":"DD","Description":"Daman & Diu","DISTRICTID":null,"DISTRICT":null},{"Code":"DL","Description":"Delhi","DISTRICTID":null,"DISTRICT":null},{"Code":"DN","Description":"Dadra and Nagar Haveli","DISTRICTID":null,"DISTRICT":null},{"Code":"GA","Description":"Goa","DISTRICTID":null,"DISTRICT":null},{"Code":"GJ","Description":"Gujarat","DISTRICTID":null,"DISTRICT":null},{"Code":"HP","Description":"Himachal Pradesh","DISTRICTID":null,"DISTRICT":null},{"Code":"HR","Description":"Haryana","DISTRICTID":null,"DISTRICT":null},{"Code":"JH","Description":"Jharkhand","DISTRICTID":null,"DISTRICT":null},{"Code":"JK","Description":"Jammu & Kashmir","DISTRICTID":null,"DISTRICT":null},{"Code":"KA","Description":"Karnataka","DISTRICTID":null,"DISTRICT":null},{"Code":"KL","Description":"Kerala","DISTRICTID":null,"DISTRICT":null},{"Code":"LD","Description":"Lakshadweep","DISTRICTID":null,"DISTRICT":null},{"Code":"MH","Description":"Maharashtra","DISTRICTID":null,"DISTRICT":null},{"Code":"ML","Description":"Meghalaya","DISTRICTID":null,"DISTRICT":null},{"Code":"MN","Description":"Manipur","DISTRICTID":null,"DISTRICT":null},{"Code":"MP","Description":"Madhya Pradesh","DISTRICTID":null,"DISTRICT":null},{"Code":"MZ","Description":"Mizoram","DISTRICTID":null,"DISTRICT":null},{"Code":"NL","Description":"Nagaland","DISTRICTID":null,"DISTRICT":null},{"Code":"OR","Description":"Orissa","DISTRICTID":null,"DISTRICT":null},{"Code":"PB","Description":"Punjab","DISTRICTID":null,"DISTRICT":null},{"Code":"PY","Description":"Pondicherry","DISTRICTID":null,"DISTRICT":null},{"Code":"RJ","Description":"Rajasthan","DISTRICTID":null,"DISTRICT":null},{"Code":"SK","Description":"Sikkim","DISTRICTID":null,"DISTRICT":null},{"Code":"TN","Description":"Tamil Nadu","DISTRICTID":null,"DISTRICT":null},{"Code":"TR","Description":"Tripura","DISTRICTID":null,"DISTRICT":null},{"Code":"TS","Description":"Telangana","DISTRICTID":null,"DISTRICT":null},{"Code":"UA","Description":"Uttarakhand","DISTRICTID":null,"DISTRICT":null},{"Code":"UP","Description":"Uttar Pradesh","DISTRICTID":null,"DISTRICT":null},{"Code":"WB","Description":"West Bengal","DISTRICTID":null,"DISTRICT":null},{"Code":"XX","Description":"OTHER","DISTRICTID":null,"DISTRICT":null}]*/

                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    statearrayList.add(jsonObject.getString("Description"));
                    state_id.put(jsonObject.getString("Description"), jsonObject.getString("Code"));
                    state_name.put(jsonObject.getString("Code"), jsonObject.getString("Description"));
                }

                ArrayAdapter<String> item_adapter = new ArrayAdapter<>(UpdateProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, statearrayList);
                item_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_state.setAdapter(item_adapter);


                state_adapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.select_dialog_item, statearrayList);
                et_state.setThreshold(1);//will start working from first character
                et_state.setAdapter(state_adapter);//setting the adapter data into the AutoCompleteTextView
                state_adapter.notifyDataSetChanged();
                et_state.setText(state_name.get(userState));
                getCity getCity = new getCity(userState);
                getCity.execute();
            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                Log.d(TAG, "getStateCallWS Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    private class getCity extends AsyncTask<Void, Void, Void> {

        String StateId;

        getCity(String StateId) {
            this.StateId = StateId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!cityarrayList.isEmpty()) {
                city_id.clear();
                city_name.clear();
                cityarrayList.clear();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getCity);
                Request.addProperty("State_Id", StateId);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL_state_and_city);

                transport.call(SOAP_ACTION + METHOD_NAME_getCity, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "getCityResult : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "getCity Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getCity Result " + resultString.toString());
                /**
                 * [{"Code":null,"Description":null,"DISTRICTID":"0","DISTRICT":"Select"},
                 {"Code":null,"Description":null,"DISTRICTID":"Bastar","DISTRICT":"Bastar"},{"Code":null,"Description":null,
                 "DISTRICTID":"Bijapur","DISTRICT":"Bijapur"},{"Code":null,"Description":null,"DISTRICTID":"Bijapur(CGH)","DISTRICT":"Bijapur(CGH)"},{"Code":null,"Description":null,"DISTRICTID":"Bilaspur","DISTRICT":"Bilaspur"},{"Code":null,"Description":null,"DISTRICTID":"Bilaspur(CGH)","DISTRICT":"Bilaspur(CGH)"},{"Code":null,"Description":null,"DISTRICTID":"Dakshin Bastar Dantewada","DISTRICT":"Dakshin Bastar Dantewada"},{"Code":null,"Description":null,"DISTRICTID":"Dantewada","DISTRICT":"Dantewada"},{"Code":null,"Description":null,"DISTRICTID":"Dhamtari","DISTRICT":"Dhamtari"},{"Code":null,"Description":null,"DISTRICTID":"Durg","DISTRICT":"Durg"},{"Code":null,"Description":null,"DISTRICTID":"Gariaband","DISTRICT":"Gariaband"},{"Code":null,"Description":null,"DISTRICTID":"Janjgir - Champa","DISTRICT":"Janjgir - Champa"},{"Code":null,"Description":null,"DISTRICTID":"Janjgir-champa","DISTRICT":"Janjgir-champa"},{"Code":null,"Description":null,"DISTRICTID":"Jashpur","DISTRICT":"Jashpur"},{"Code":null,"Description":null,"DISTRICTID":"Kanker","DISTRICT":"Kanker"},{"Code":null,"Description":null,"DISTRICTID":"Kawardha","DISTRICT":"Kawardha"},{"Code":null,"Description":null,"DISTRICTID":"Korba","DISTRICT":"Korba"},{"Code":null,"Description":null,"DISTRICTID":"Koriya","DISTRICT":"Koriya"},{"Code":null,"Description":null,"DISTRICTID":"Mahasamund","DISTRICT":"Mahasamund"},{"Code":null,"Description":null,"DISTRICTID":"Narayanpur","DISTRICT":"Narayanpur"},{"Code":null,"Description":null,"DISTRICTID":"Raigarh","DISTRICT":"Raigarh"},{"Code":null,"Description":null,"DISTRICTID":"Raipur","DISTRICT":"Raipur"},{"Code":null,"Description":null,"DISTRICTID":"Rajnandgaon","DISTRICT":"Rajnandgaon"},{"Code":null,"Description":null,"DISTRICTID":"Surguja","DISTRICT":"Surguja"},{"Code":null,"Description":null,"DISTRICTID":"Uttar Bastar Kanker","DISTRICT":"Uttar Bastar Kanker"}]*/
                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    cityarrayList.add(jsonObject.getString("DISTRICT"));
                    city_id.put(jsonObject.getString("DISTRICT"), jsonObject.getString("DISTRICTID"));
                    city_name.put(jsonObject.getString("DISTRICTID"), jsonObject.getString("DISTRICT"));
                }
                ArrayAdapter<String> item_adapter = new ArrayAdapter<>(UpdateProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, cityarrayList);
                item_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_city.setAdapter(item_adapter);
                //  Toast.makeText(UpdateProfileActivity.this, "" + String.valueOf(retval), Toast.LENGTH_SHORT).show();
                // spinner_city.setSelection(retval);

                city_adapter = new ArrayAdapter<String>(UpdateProfileActivity.this, android.R.layout.select_dialog_item, cityarrayList);
                et_city.setThreshold(1);//will start working from first character
                et_city.setAdapter(city_adapter);//setting the adapter data into the AutoCompleteTextView
                et_city.setText(city_name.get(City_Communication));

                ///Toast.makeText(UpdateProfileActivity.this, "" + city_name.get(City_Communication) + " " + City_Communication, Toast.LENGTH_SHORT).show();
            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                Log.d(TAG, "getCityCallWS Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UserprofilegCallWS extends AsyncTask<Void, Void, Void> {
        String UserId;

        UserprofilegCallWS(String UserId) {
            this.UserId = UserId;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getInvestorProfile);
                Request.addProperty("Registration_Id", UserId);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorViewProfile);

                transport.call(SOAP_ACTION + METHOD_NAME_getInvestorProfile, soapEnvelope);
                ProfileDtaResult = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "InvestorProfileAddUpdateBind_Result : " + ProfileDtaResult);
            } catch (Exception ex) {
                Log.e(TAG, "InvestorProfileAddUpdateBind Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {

                /**
                 * [{"Registration_Id":"FIN2018717753833","Customer_Name":"Anand Rai","Gender":"M","DOB":"17/07/2018",
                 "PanCard_No":"GJHGJHG23f","PanKYC_Status":"","Occupation":"","Annual_Income":"","Country_Permanent":"",
                 "Fathers_Name":"","Mothers_Name":"","Communication_Address":"","Communication_AddressLine2":"","Country_Communication":"",
                 "State_Communication":"","City_Communication":"","Mobile_No":"9569702714","Email_Id":"anandnrai@gmail.com","Zip_Communication":"",
                 "Telephone_Communication":"","Cust_Bankname":"","Cust_AccountType":"","Cust_HoldingMode":"","Cust_AccountNumber":"","Cust_IFSCCode":"","Cust_BankCity":"","Cust_BranchAddress":""}] */

                JSONArray jsonArray = new JSONArray(ProfileDtaResult.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Log.d(TAG, "Registration_Id Result " + jsonObject.getString("Registration_Id"));
                    /*(String Registration_Id,String name,String Gender,String DOB,String PanCard_No,String PanKYC_Status,
                                   String Occupation,String Annual_Income,String Fathers_Name,String Mothers_Name,String Mobile_No,String Email_Id)*/
                    session.createUserInfoSession(
                            jsonObject.getString("Registration_Id"),
                            jsonObject.getString("Customer_Name"),
                            jsonObject.getString("Gender"),
                            jsonObject.getString("DOB"),
                            jsonObject.getString("PanCard_No"),
                            jsonObject.getString("PanKYC_Status"),
                            jsonObject.getString("Occupation"),
                            jsonObject.getString("Annual_Income"),
                            jsonObject.getString("Fathers_Name"),
                            jsonObject.getString("Mothers_Name"),
                            jsonObject.getString("Mobile_No"),
                            jsonObject.getString("Email_Id"));

                  /*(String Country_Permanent, String Communication_Address,String Communication_AddressLine2,String Country_Communication,
                                   String State_Communication,String City_Communication,String Zip_Communication)*/

                    session.createUserCommunicationSession(jsonObject.getString("Country_Permanent"),
                            jsonObject.getString("Communication_Address"),
                            jsonObject.getString("Communication_AddressLine2"),
                            jsonObject.getString("Country_Communication"),
                            jsonObject.getString("State_Communication"),
                            jsonObject.getString("City_Communication"),
                            jsonObject.getString("Zip_Communication"));

                  /*createUserBankInfoSession(String Cust_Bankname, String Cust_AccountType, String Cust_HoldingMode, String Cust_AccountNumber, String Cust_IFSCCode,
                                   String Cust_BranchAddress)*/

                    session.createUserBankInfoSession(jsonObject.getString("Cust_Bankname"),
                            jsonObject.getString("Cust_AccountType"),
                            jsonObject.getString("Cust_HoldingMode"),
                            jsonObject.getString("Cust_AccountNumber"),
                            jsonObject.getString("Cust_IFSCCode"),
                            jsonObject.getString("Cust_BranchAddress"));

                    //

                }
            } catch (NullPointerException | JSONException e) {
                Log.d(TAG, "CustomerProfile Error:- " + e.getMessage());
                ShowDilog(UpdateProfileActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

}
