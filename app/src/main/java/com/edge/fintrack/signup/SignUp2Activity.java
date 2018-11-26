package com.edge.fintrack.signup;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.edge.fintrack.MainActivity;
import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.edge.fintrack.Api_Class.METHOD_NAME_OpenCustomerAccountSecondStep;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getInvestorProfile;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_InvestorViewProfile;
import static com.edge.fintrack.Api_Class.URL_OpenCustomerAccountFirstStep;
import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isStringEmpty;

public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public final String TAG = "SignUpActivity";
    EditText tv_dob;
    DatePickerDialog datePickerDialog;
    Button bt_submit;
    String ResidentialStatus, Gender, Marital, Date;
    Spinner spinner_ResidentialStatus, spinner_gender, spinner_marital;
    EditText et_pan, et_aadhar, et_ckyc;
    SoapPrimitive resultString;
    String RegistrationId;
    Map<String, String> ResidentialStatus_id;
    Map<String, String> Gender_id;
    Map<String, String> Marital_id;
    SessionManager session;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_sign_up2);
        session = new SessionManager(SignUp2Activity.this);
        final ScrollView scrollview = ((ScrollView) findViewById(R.id.scrollview));
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            RegistrationId = null;
        } else {
            RegistrationId = bundle.getString("RegistrationId");
        }

        ResidentialStatus_id = new HashMap<String, String>();
        ResidentialStatus_id.put("Select", "Select");
        ResidentialStatus_id.put("Resident Individual", "01");
        ResidentialStatus_id.put("Non Resident Indian", "02");
        ResidentialStatus_id.put("Foreign National", "03");
        ResidentialStatus_id.put("Person of Indian Origin", "04");
        spinner_ResidentialStatus = (Spinner) findViewById(R.id.spinner_ResidentialStatus);
        spinner_ResidentialStatus.setOnItemSelectedListener(this);

        Gender_id = new HashMap<String, String>();
        Gender_id.put("Select", "Select");
        Gender_id.put("Male", "M");
        Gender_id.put("Female", "F");
        Gender_id.put("Transgender", "O");
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        spinner_gender.setOnItemSelectedListener(this);

        Marital_id = new HashMap<String, String>();
        Marital_id.put("Select", "Select");
        Marital_id.put("Married", "01");
        Marital_id.put("UnMarried", "02");
        spinner_marital = (Spinner) findViewById(R.id.spinner_marital);
        spinner_marital.setOnItemSelectedListener(this);

        tv_dob = (EditText) findViewById(R.id.tv_dob);
        tv_dob.setOnClickListener(this);

        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);

        et_pan = (EditText) findViewById(R.id.et_pan);
        et_pan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        et_aadhar = (EditText) findViewById(R.id.et_aadhar);
        et_ckyc = (EditText) findViewById(R.id.et_ckyc);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dob:
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(SignUp2Activity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                if (monthOfYear + 1 < 10) {
                                    tv_dob.setText(dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year);
                                    //Date = "0" + (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                                    Date = dayOfMonth + "-" + "0" + (monthOfYear + 1) + "-" + year;
                                } else {
                                    tv_dob.setText(dayOfMonth + "/"
                                            + (monthOfYear + 1) + "/" + year);
                                    //Date = (monthOfYear + 1) + "-" + dayOfMonth + "-" + year;
                                    Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                }
                              //  Toast.makeText(SignUp2Activity.this, "" + Date, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
                datePickerDialog.show();
                break;
            case R.id.bt_submit:
                if (isValidForm()) {
                   /* Intent intentMain = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                    //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/
                    OpenCustomerAccountCallWS task = new OpenCustomerAccountCallWS();
                    task.execute();
                }
                break;
        }
    }

    private boolean isValidForm() {   // check the edit text are empty and the spinner are select or not
       /* if (ResidentialStatus.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please Select The Residential Status", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Gender.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please Select The Gender", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Marital.equalsIgnoreCase("Select")) {
            Toast.makeText(this, "Please Select The Marital Status", Toast.LENGTH_SHORT).show();
            return false;
        } else */
        if (et_pan.getText().toString().isEmpty()) {
            et_pan.requestFocus();
            et_pan.setError(getString(R.string.pan_number_required));
            return false;
        } else if (et_aadhar.getText().toString().isEmpty()) {
            et_aadhar.requestFocus();
            et_aadhar.setError(getString(R.string.pan_number_required));
            return false;
        } else if (isStringEmpty(Date)) {
            Toast.makeText(this, "Please Select the Date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_ResidentialStatus:
                ResidentialStatus = ResidentialStatus_id.get(spinner_ResidentialStatus.getItemAtPosition(position).toString());
                break;
            case R.id.spinner_gender:
                Gender = Gender_id.get(spinner_gender.getItemAtPosition(position).toString());
                break;
            case R.id.spinner_marital:
                Marital = Marital_id.get(spinner_marital.getItemAtPosition(position).toString());
                //Toast.makeText(this, "" + Marital, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class OpenCustomerAccountCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(SignUp2Activity.this, null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_OpenCustomerAccountSecondStep);

              /*  Request.addProperty("RegistrationId", RegistrationId);
                Request.addProperty("Residential_Status", ResidentialStatus);
                Request.addProperty("Gender", Gender);
                Request.addProperty("DOB", Date);
                Request.addProperty("Marital_Status", Marital);
                Request.addProperty("PanCard_No", et_pan.getText().toString());
                Request.addProperty("AadharCard_No", et_aadhar.getText().toString()); */

               /* <RegistrationId>string</RegistrationId>
      <DOB>string</DOB>
      <PanCard_No>string</PanCard_No>
      <CKYCNo>string</CKYCNo>
      <AadharCard_No>string</AadharCard_No>*/

                //2018111971012

                Request.addProperty("RegistrationId", RegistrationId);
                Request.addProperty("DOB", Date);
                Request.addProperty("PanCard_No", et_pan.getText().toString());
                Request.addProperty("CKYCNo", et_ckyc.getText().toString());
                Request.addProperty("AadharCard_No", et_aadhar.getText().toString());
                Log.i(TAG, "SoapObject Result: " + Request.toString());

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_OpenCustomerAccountFirstStep);

                transport.call(SOAP_ACTION + METHOD_NAME_OpenCustomerAccountSecondStep, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "Result METHOD_NAME_OpenCustomerAccountSecondStep: " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "OpenCustomerAccountSecondStep Result" + resultString.toString());
                mProgressDialog.dismiss();
                if (resultString.toString().equalsIgnoreCase("success")) {
                    /*Intent intentMain = new Intent(SignUp2Activity.this, MainActivity.class);
                    //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                    startActivity(intentMain);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/

                    UserprofilegCallWS userprofilegCallWS = new UserprofilegCallWS();
                    userprofilegCallWS.execute();

                } else {
                    Log.d(TAG, "CustomerProfile Response:- " + resultString.toString());
                    ShowDilog(SignUp2Activity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "CustomerProfile Response:- " + e.getMessage());
            }


           /* Intent intentMain = new Intent(SignUp2Activity.this, SignUp2Activity.class);
            //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity(intentMain);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);*/
         /*   try {
                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String UserId = jsonObject.getString("UserId");
                    mProgressDialog.hide();
                    if (UserId.equalsIgnoreCase("-1")) {
                        ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "User are not activated !! We have send an email, please activated account first.");
                    } else if (UserId.equalsIgnoreCase("-2")) {
                        ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "Email and Password is incorrect");
                    } else {
                        Toast.makeText(SignUpActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intentMain = new Intent(SignUpActivity.this, MainActivity.class);
                        //  intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(intentMain);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        SignUpActivity.this.finish();
                        //  Toast.makeText(LoginActivity.this, "" + UserId, Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "Error.Response:- " + e.getMessage());
                ShowDilog(SignUpActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }

    }

    private class UserprofilegCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getInvestorProfile);
                Request.addProperty("Registration_Id", RegistrationId);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorViewProfile);

                transport.call(SOAP_ACTION + METHOD_NAME_getInvestorProfile, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "InvestorProfileAddUpdateBind_Result : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "InvestorProfileAddUpdateBind Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mProgressDialog.dismiss();
            try {

                /**
                 * [{"Registration_Id":"FIN2018717753833","Customer_Name":"Anand Rai","Gender":"M","DOB":"17/07/2018",
                 "PanCard_No":"GJHGJHG23f","PanKYC_Status":"","Occupation":"","Annual_Income":"","Country_Permanent":"",
                 "Fathers_Name":"","Mothers_Name":"","Communication_Address":"","Communication_AddressLine2":"","Country_Communication":"",
                 "State_Communication":"","City_Communication":"","Mobile_No":"9569702714","Email_Id":"anandnrai@gmail.com","Zip_Communication":"",
                 "Telephone_Communication":"","Cust_Bankname":"","Cust_AccountType":"","Cust_HoldingMode":"","Cust_AccountNumber":"","Cust_IFSCCode":"","Cust_BankCity":"","Cust_BranchAddress":""}] */

                JSONArray jsonArray = new JSONArray(resultString.toString());
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

                    Intent mainIntent = new Intent(SignUp2Activity.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SignUp2Activity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    SignUp2Activity.this.finish();
                }
            } catch (NullPointerException e) {
                Log.d(TAG, "CustomerProfile Error:- " + e.getMessage());
                ShowDilog(SignUp2Activity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
