package com.edge.fintrack.insurance;

import android.os.AsyncTask;
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

import com.edge.fintrack.NumberTextWatcherForThousand;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.edge.fintrack.Api_Class.METHOD_NAME_getCity;
import static com.edge.fintrack.Api_Class.METHOD_NAME_getState;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_state_and_city;
import static com.edge.fintrack.Constant.ShowDilog;
import static com.edge.fintrack.Constant.isInternetOn;

public class TermInsuranceActivity extends AppCompatActivity {
    public final String TAG = "TermInsuranceActivity";
    SoapPrimitive resultString;
    ImageView iv_menu_item;
    // Session Manager Class
    SessionManager session;
    EditText et_cover_amount, et_annual_income, et_dob, et_perfix, et_customername, et_mobile, et_email;
    AutoCompleteTextView ac_country, et_state, et_city;

    String[] country = {"India", "US", "UK", "Japan"};

    ArrayList<String> statearrayList = new ArrayList<>();
    Map<String, String> state_id = new HashMap<String, String>();
    ArrayList<String> stateid_list = new ArrayList<>();
    ArrayList<String> cityarrayList = new ArrayList<>();
    Map<String, String> city_id = new HashMap<String, String>();
    ArrayList<String> codeid_list = new ArrayList<>();
    ArrayAdapter<String> country_adapter;
    ArrayAdapter<String> state_adapter;
    ArrayAdapter<String> city_adapter;
    String userCountry, userState, userCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_term_insurance);

        iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // Session class instance
        session = new SessionManager(TermInsuranceActivity.this);
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        Log.d(TAG, "Registration_Id " + user.get(SessionManager.KEY_Registration_Id));

        userCountry = user.get(SessionManager.KEY_Country_Communication);
        userState = user.get(SessionManager.KEY_State_Communication);
        userCity = user.get(SessionManager.KEY_City_Communication);

        et_cover_amount = (EditText) findViewById(R.id.et_cover_amount);
        et_cover_amount.setText("10,00,000");
        et_cover_amount.addTextChangedListener(new NumberTextWatcherForThousand(et_cover_amount));
        et_annual_income = (EditText) findViewById(R.id.et_annual_income);
        et_annual_income.setText("50,00,000");
        et_annual_income.addTextChangedListener(new NumberTextWatcherForThousand(et_annual_income));
        //et_perfix = (EditText) findViewById(R.id.et_perfix);
        et_customername = (EditText) findViewById(R.id.et_customername);
        et_customername.setText(user.get(SessionManager.KEY_NAME));
        et_dob = (EditText) findViewById(R.id.et_dob);
        et_dob.setText(user.get(SessionManager.KEY_DOB));
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_mobile.setText(user.get(SessionManager.KEY_Mobile_No));
        et_email = (EditText) findViewById(R.id.et_email);
        et_email.setText(user.get(SessionManager.KEY_EMAIL));

        ac_country = (AutoCompleteTextView) findViewById(R.id.ac_country);
        et_state = (AutoCompleteTextView) findViewById(R.id.et_state);
        et_city = (AutoCompleteTextView) findViewById(R.id.et_city);
        ac_country.setText(country[0]);
        country_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, country);
        ac_country.setThreshold(1);
        ac_country.setAdapter(country_adapter);
        ac_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(TermInsuranceActivity.this, country_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
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
                if (!isInternetOn(TermInsuranceActivity.this)) {
                    ShowDilog(TermInsuranceActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
                } else {
                    et_city.setText("");
                    userState = state_id.get(state_adapter.getItem(position).toString());
                    getCity getCity = new getCity(userState);
                    getCity.execute();
                }
            }
        });

        et_city.setText(user.get(SessionManager.KEY_City_Communication));
        city_adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, cityarrayList);
        et_city.setThreshold(1);//will start working from first character
        et_city.setAdapter(city_adapter);//setting the adapter data into the AutoCompleteTextView
        et_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(TermInsuranceActivity.this, city_adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        if (!isInternetOn(TermInsuranceActivity.this)) {
            ShowDilog(TermInsuranceActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
        } else {
            getState getState = new getState();
            getState.execute();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    private class getState extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!statearrayList.isEmpty()) {
                statearrayList.clear();
                state_id.clear();
            }

            if (!stateid_list.isEmpty()) {
                stateid_list.clear();
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
                    stateid_list.add(jsonObject.getString("Code"));
                    if (jsonObject.getString("Code").equalsIgnoreCase(userState)) ;
                    // et_state.setText(user.get(SessionManager.KEY_State_Communication));

                }
                state_adapter.notifyDataSetChanged();
                int position = stateid_list.indexOf(userState);
                et_state.setText(statearrayList.get(position));
                getCity getCity = new getCity(userState);
                getCity.execute();
            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                Log.d(TAG, "getDetailCallWS Error:- " + e.getMessage());
                ShowDilog(TermInsuranceActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
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
                cityarrayList.clear();
            }
            if (!codeid_list.isEmpty()) {
                codeid_list.clear();
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
                    codeid_list.add(jsonObject.getString("DISTRICTID"));
                }

                city_adapter = new ArrayAdapter<String>(TermInsuranceActivity.this, android.R.layout.select_dialog_item, cityarrayList);
                et_city.setAdapter(city_adapter);
                city_adapter.notifyDataSetChanged();
            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                Log.d(TAG, "getDetailCallWS Error:- " + e.getMessage());
                ShowDilog(TermInsuranceActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }
}
