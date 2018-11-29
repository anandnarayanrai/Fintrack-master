package com.edge.fintrack.account_detail;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_getInvestorProfile;
import static com.edge.fintrack.utility.Api_Class.METHOD_NAME_getInvestorProfilePhoto;
import static com.edge.fintrack.utility.Api_Class.NAMESPACE;
import static com.edge.fintrack.utility.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.utility.Api_Class.URL_InvestorProfilePhoto;
import static com.edge.fintrack.utility.Api_Class.URL_InvestorViewProfile;
import static com.edge.fintrack.utility.Constant.ShowDilog;
import static com.edge.fintrack.utility.Constant.isInternetOn;

public class ProfileViewActivity extends AppCompatActivity {
    public final String TAG = "ProfileViewActivity";
    // Session Manager Class
    SessionManager session;
    HashMap<String, String> user;
    SoapPrimitive ProfilePicResult;
    SoapPrimitive ProfileDtaResult;
    ImageView imageView_user, iv_back_image;
    CircleImageView profile_image;
    private TextView tv_Pusername, tv_Puserekvc, tv_Pusereview;

    /**
     * Personal Information
     */
    private TextView tv_email, tv_mobile, tv_fathername, tv_gender;

    /**
     * Primary Bank Information
     */
    private TextView tv_bank_name, tv_bank_account_ty, tv_bank_holding, tv_bank_loder_name1, tv_bank_loder_name2, tv_bank_add, tv_bank_ifsc, tv_bank_account;

    /**
     * Address Information
     */
    private TextView tv_address1, tv_address2, tv_country, tv_city, tv_pincode;

    private String Registration_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.layout_profile);
        // making notification bar transparent
        // changeStatusBarColor();

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        user = session.getUserDetails();

        Log.d(TAG, "Registration_Id " + user.get(SessionManager.KEY_Registration_Id));
        Registration_Id = user.get(SessionManager.KEY_Registration_Id);

        imageView_user = (ImageView) findViewById(R.id.imageView_user);

        iv_back_image = (ImageView) findViewById(R.id.iv_back_image);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        // profile_image.setOnClickListener(this);

        ImageView iv_menu_item = (ImageView) findViewById(R.id.iv_menu_item);
        iv_menu_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (!isInternetOn(ProfileViewActivity.this)) {
            ShowDilog(ProfileViewActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.Internet_not_found));
        } else {
            UserprofilegCallWS userprofilegCallWS = new UserprofilegCallWS(Registration_Id);
            userprofilegCallWS.execute();
            getProfilePhoto task = new getProfilePhoto();
            task.execute();
            SetProfile();
        }

    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          /*  Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            /*// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void SetProfile() {
        tv_Pusername = (TextView) findViewById(R.id.tv_Pusername);
        tv_Pusername.setText(user.get(SessionManager.KEY_NAME));

        tv_Puserekvc = (TextView) findViewById(R.id.tv_Puserekvc);
        tv_Puserekvc.setText("PAN:- " + user.get(SessionManager.KEY_PanCard_No));

        tv_Pusereview = (TextView) findViewById(R.id.tv_Pusereview);
        tv_Pusereview.setText(user.get(SessionManager.KEY_PanKYC_Status));
        if (user.get(SessionManager.KEY_PanKYC_Status).equalsIgnoreCase("Not Completed"))
            tv_Pusereview.setTextColor(getResources().getColor(R.color.md_deep_orange_500));

        /**
         * Personal Information
         * */
        tv_email = (TextView) findViewById(R.id.tv_emailid);
        tv_email.setText("Email Address :- " + user.get(SessionManager.KEY_EMAIL));

        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_mobile.setText("Mobile :- " + user.get(SessionManager.KEY_Mobile_No));

        tv_fathername = (TextView) findViewById(R.id.tv_fathername);
        tv_fathername.setText("Father Name :- " + user.get(SessionManager.KEY_Fathers_Name));

        tv_gender = (TextView) findViewById(R.id.tv_gender);
        tv_gender.setText("Gender  :- " + user.get(SessionManager.KEY_Gender));

        /**
         * Primary Bank Information
         * */
        tv_bank_name = (TextView) findViewById(R.id.tv_bank_name);
        tv_bank_name.setText("Name of the Bank:- " + user.get(SessionManager.KEY_Cust_Bankname));

        tv_bank_account_ty = (TextView) findViewById(R.id.tv_bank_account_ty);
        tv_bank_account_ty.setText("Account Type:- " + user.get(SessionManager.KEY_Cust_AccountType));

        tv_bank_holding = (TextView) findViewById(R.id.tv_bank_holding);
        tv_bank_holding.setText("Mode of Holding:- " + user.get(SessionManager.KEY_Cust_HoldingMode));

        tv_bank_loder_name1 = (TextView) findViewById(R.id.tv_bank_loder_name1);
        tv_bank_loder_name1.setText("1st Account Holder Name:- " + user.get(SessionManager.KEY_NAME));

        tv_bank_loder_name2 = (TextView) findViewById(R.id.tv_bank_loder_name2);
        tv_bank_loder_name2.setText("2nd Account Holder Name:- " + user.get(SessionManager.KEY_NAME));

        tv_bank_add = (TextView) findViewById(R.id.tv_bank_add);
        tv_bank_add.setText("Branch Address:- " + user.get(SessionManager.KEY_Cust_BranchAddress));

        tv_bank_ifsc = (TextView) findViewById(R.id.tv_bank_ifsc);
        tv_bank_ifsc.setText("IFSC Code:- " + user.get(SessionManager.KEY_Cust_IFSCCode));

        tv_bank_account = (TextView) findViewById(R.id.tv_bank_account);
        tv_bank_account.setText("Account Number:- " + user.get(SessionManager.KEY_Cust_AccountNumber));

        /**
         * Address Information
         * */
        tv_address1 = (TextView) findViewById(R.id.tv_address1);
        tv_address1.setText("Address Line 1:- " + user.get(SessionManager.KEY_Communication_Address));

        tv_address2 = (TextView) findViewById(R.id.tv_address2);
        tv_address2.setText("Address Line 3:- " + user.get(SessionManager.KEY_Communication_AddressLine2));

        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_country.setText("Country :- " + user.get(SessionManager.KEY_Country_Communication));

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_city.setText("City/Location :- " + user.get(SessionManager.KEY_City_Communication) + "(" + user.get(SessionManager.KEY_State_Communication) + ", " + user.get(SessionManager.KEY_Country_Communication) + ")");

        tv_pincode = (TextView) findViewById(R.id.tv_pincode);
        tv_pincode.setText("Pincode :- " + user.get(SessionManager.KEY_Zip_Communication));
    }

    @SuppressLint("StaticFieldLeak")
    private class getProfilePhoto extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getInvestorProfilePhoto);
                Request.addProperty("Registration_Id", Registration_Id);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);

                HttpTransportSE transport = new HttpTransportSE(URL_InvestorProfilePhoto);

                transport.call(SOAP_ACTION + METHOD_NAME_getInvestorProfilePhoto, soapEnvelope);
                ProfilePicResult = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "getProfilePhoto_Result : " + ProfilePicResult);
            } catch (Exception ex) {
                Log.e(TAG, "getProfilePhoto Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getProfilePhoto Result " + ProfilePicResult.toString() + " " + Registration_Id);
                /**
                 * [{"Registration_Id":"FIN201822772158860","Profile_Pic":"http://fintrackindia.com/FilesUploaded/Investor/profile/FIN20182277215886081navdeep.jpg"}] FIN201822772158860
                 * */
                JSONArray jsonArray = new JSONArray(ProfilePicResult.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("Profile_Pic")) {
                       /* try {

                            Picasso.get()
                                    .load(jsonObject.getString("Profile_Pic"))
                                    .placeholder(R.drawable.user_icon)
                                    .error(R.drawable.user_icon)
                                    .into(profile_image);

                            Picasso.get()
                                    .load(jsonObject.getString("Profile_Pic"))
                                    .resize(200, 100)
                                    .centerCrop()
                                    .into(iv_back_image);

                        } catch (IllegalArgumentException e) {
                            Log.d(TAG, "Profile_Pic Error:- " + e.getMessage());
                        }*/

                        if (jsonObject.getString("Profile_Pic").contains(".jpg")) {
                            try {
                                Picasso.get()
                                        .load(jsonObject.getString("Profile_Pic"))
                                        .placeholder(R.drawable.user_icon)
                                        .error(R.drawable.user_icon)
                                        .into(profile_image);
                                Picasso.get()
                                        .load(jsonObject.getString("Profile_Pic"))
                                        .placeholder(R.drawable.user_icon)
                                        .error(R.drawable.user_icon)
                                        .into(iv_back_image);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String imageString = jsonObject.getString("Profile_Pic").replace("http://fintrackindia.com/FilesUploaded/Investor/profile/", "");

                            //decode base64 string to image
                            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            profile_image.setImageBitmap(decodedImage);
                            iv_back_image.setImageBitmap(decodedImage);
                        }
                    }
                }
            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                Log.d(TAG, "getProfilePhoto Error:- " + e.getMessage());
                ShowDilog(ProfileViewActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
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
                    SetProfile();

                    //

                }
            } catch (NullPointerException | JSONException e) {
                SetProfile();
                Log.d(TAG, "CustomerProfile Error:- " + e.getMessage());
                ShowDilog(ProfileViewActivity.this, "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }

}
