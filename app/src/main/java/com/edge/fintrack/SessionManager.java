package com.edge.fintrack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    public static final String KEY_Registration_Id = "Registration_Id";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_Gender = "Gender";
    public static final String KEY_DOB = "DOB";
    public static final String KEY_PanCard_No = "PanCard_No";
    public static final String KEY_PanKYC_Status = "PanKYC_Status";
    public static final String KEY_Occupation = "Occupation";
    public static final String KEY_Annual_Income = "Annual_Income";
    public static final String KEY_Country_Permanent = "Country_Permanent";
    public static final String KEY_Fathers_Name = "Fathers_Name";
    public static final String KEY_Communication_Address = "Communication_Address";
    public static final String KEY_Communication_AddressLine2 = "Communication_AddressLine2";
    public static final String KEY_Country_Communication = "Country_Communication";
    public static final String KEY_State_Communication = "State_Communication";
    public static final String KEY_City_Communication = "City_Communication";
    public static final String KEY_Mobile_No = "Mobile_No";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_Zip_Communication = "Zip_Communication";
    public static final String KEY_Cust_Bankname = "Cust_Bankname";
    public static final String KEY_Cust_AccountType = "Cust_AccountType";
    public static final String KEY_Cust_HoldingMode = "Cust_HoldingMode";
    public static final String KEY_Cust_AccountNumber = "Cust_AccountNumber";
    public static final String KEY_Cust_IFSCCode = "Cust_IFSCCode";
    public static final String KEY_Cust_BranchAddress = "Cust_BranchAddress";
    public static final String KEY_COLOR = "color";
    private static final String KEY_Mothers_Name = "Mothers_Name";
    // Sharedpref file name
    private static final String PREF_NAME = "Fintrack";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */

    /**
     * [{"Registration_Id":"FIN2018717753833","Customer_Name":"Anand Rai","Gender":"M","DOB":"17/07/2018",
     * "PanCard_No":"GJHGJHG23f","PanKYC_Status":"","Occupation":"","Annual_Income":"","Country_Permanent":"",
     * "Fathers_Name":"","Mothers_Name":"","Communication_Address":"","Communication_AddressLine2":"","Country_Communication":"",
     * "State_Communication":"","City_Communication":"","Mobile_No":"9569702714","Email_Id":"anandnrai@gmail.com","Zip_Communication":"",
     * "Telephone_Communication":"","Cust_Bankname":"","Cust_AccountType":"","Cust_HoldingMode":"","Cust_AccountNumber":"","Cust_IFSCCode":"","Cust_BankCity":"","Cust_BranchAddress":""}]
     */

    public void createUserInfoSession(String Registration_Id, String name, String Gender, String DOB, String PanCard_No, String PanKYC_Status,
                                      String Occupation, String Annual_Income, String Fathers_Name, String Mothers_Name, String Mobile_No, String Email_Id) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // Storing name in pref
        editor.putString(KEY_Registration_Id, Registration_Id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_Gender, Gender);
        editor.putString(KEY_DOB, DOB);
        editor.putString(KEY_PanCard_No, PanCard_No);
        editor.putString(KEY_PanKYC_Status, PanKYC_Status);
        editor.putString(KEY_Occupation, Occupation);
        editor.putString(KEY_Annual_Income, Annual_Income);
        editor.putString(KEY_Fathers_Name, Fathers_Name);
        editor.putString(KEY_Mothers_Name, Mothers_Name);
        editor.putString(KEY_Mobile_No, Mobile_No);
        // Storing email in pref
        editor.putString(KEY_EMAIL, Email_Id);
        // commit changes
        editor.commit();
    }

    public void createUserCommunicationSession(String Country_Permanent, String Communication_Address, String Communication_AddressLine2, String Country_Communication,
                                               String State_Communication, String City_Communication, String Zip_Communication) {

        editor.putString(KEY_Country_Permanent, Country_Permanent);
        editor.putString(KEY_Communication_Address, Communication_Address);
        editor.putString(KEY_Communication_AddressLine2, Communication_AddressLine2);
        editor.putString(KEY_Country_Communication, Country_Communication);
        editor.putString(KEY_State_Communication, State_Communication);
        editor.putString(KEY_City_Communication, City_Communication);
        editor.putString(KEY_Zip_Communication, Zip_Communication);

        // commit changes
        editor.commit();
    }

    public void createUserBankInfoSession(String Cust_Bankname, String Cust_AccountType, String Cust_HoldingMode, String Cust_AccountNumber, String Cust_IFSCCode,
                                          String Cust_BranchAddress) {
        editor.putString(KEY_Cust_Bankname, Cust_Bankname);
        editor.putString(KEY_Cust_AccountType, Cust_AccountType);
        editor.putString(KEY_Cust_HoldingMode, Cust_HoldingMode);
        editor.putString(KEY_Cust_AccountNumber, Cust_AccountNumber);
        editor.putString(KEY_Cust_IFSCCode, Cust_IFSCCode);
        editor.putString(KEY_Cust_BranchAddress, Cust_BranchAddress);

        // commit changes
        editor.commit();
    }

    /**
     * Create color session
     */
    public void ChangeAppColorSession(String color) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing color in pref
        editor.putString(KEY_COLOR, color);

        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_Registration_Id, pref.getString(KEY_Registration_Id, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_Gender, pref.getString(KEY_Gender, null));
        user.put(KEY_DOB, pref.getString(KEY_DOB, null));
        user.put(KEY_PanCard_No, pref.getString(KEY_PanCard_No, null));
        user.put(KEY_PanKYC_Status, pref.getString(KEY_PanKYC_Status, null));
        user.put(KEY_Occupation, pref.getString(KEY_Occupation, null));
        user.put(KEY_Annual_Income, pref.getString(KEY_Annual_Income, null));
        user.put(KEY_Fathers_Name, pref.getString(KEY_Fathers_Name, null));
        user.put(KEY_Mothers_Name, pref.getString(KEY_Mothers_Name, null));
        user.put(KEY_Mobile_No, pref.getString(KEY_Mobile_No, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        user.put(KEY_Country_Permanent, pref.getString(KEY_Country_Permanent, null));
        user.put(KEY_Communication_Address, pref.getString(KEY_Communication_Address, null));
        user.put(KEY_Communication_AddressLine2, pref.getString(KEY_Communication_AddressLine2, null));
        user.put(KEY_Country_Communication, pref.getString(KEY_Country_Communication, null));
        user.put(KEY_State_Communication, pref.getString(KEY_State_Communication, null));
        user.put(KEY_City_Communication, pref.getString(KEY_City_Communication, null));
        user.put(KEY_Zip_Communication, pref.getString(KEY_Zip_Communication, null));

        user.put(KEY_Cust_Bankname, pref.getString(KEY_Cust_Bankname, null));
        user.put(KEY_Cust_AccountType, pref.getString(KEY_Cust_AccountType, null));
        user.put(KEY_Cust_HoldingMode, pref.getString(KEY_Cust_HoldingMode, null));
        user.put(KEY_Cust_AccountNumber, pref.getString(KEY_Cust_AccountNumber, null));
        user.put(KEY_Cust_IFSCCode, pref.getString(KEY_Cust_IFSCCode, null));
        user.put(KEY_Cust_BranchAddress, pref.getString(KEY_Cust_BranchAddress, null));

        user.put(KEY_COLOR, pref.getString(KEY_COLOR, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
