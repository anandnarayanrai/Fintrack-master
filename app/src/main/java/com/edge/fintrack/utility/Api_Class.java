package com.edge.fintrack.utility;

public class Api_Class {

    /**
     * Main url
     */
    public static final String MAIN_DOMAIN = "http://www.fintrackindia.com/WebServices/";
    public static final String SOAP_ACTION = "http://microsoft.com/webservices/";
    public static final String NAMESPACE = "http://microsoft.com/webservices/";

    /**
     * CustomerLogin
     */
    public static final String URL_CustomerLogin = MAIN_DOMAIN + "CustomerLoginAnroid.asmx";
    public static final String METHOD_NAME_CustomerLogin = "Get_CustomerLoginDataAnroid";

    /**
     * OpenCustomerAccount
     */
    public static final String URL_OpenCustomerAccountFirstStep = MAIN_DOMAIN + "OpenCustomerAccountFirstStep.asmx";
    public static final String METHOD_NAME_OpenCustomerAccountFirstStep = "CustomerAccountOpenFirstStepInsert";
    public static final String METHOD_NAME_OpenCustomerAccountSecondStep = "CustomerAccountOpenSecondStepInsert";

    /**
     * CheckAvailabilityEmail
     */
    public static final String URL_CheckAvailabilityEmailMobile = MAIN_DOMAIN + "CheckAvailabilityEmailMobile.asmx";
    public static final String METHOD_NAME_GetUser_AlreadyRegisterdEmail = "GetUser_AlreadyRegisterdEmail";
    public static final String METHOD_NAME_GetUser_AlreadyRegisterdMobile = "GetUser_AlreadyRegisterdMobile";

    /**
     * ValidateOTPCustomerAccount
     */
    public static final String URL_ValidateOTP = MAIN_DOMAIN + "ValidateOTP.asmx";
    public static final String METHOD_NAME_CValidateOTP = "GetOTPResponse";
    public static final String METHOD_NAME_GetOTPResponseIFA = "GetOTPResponseIFA";
    public static final String METHOD_NAME_RegenerateOTPClient = "RegenerateOTPClient";


    /**
     * InvestorProfileAddUpdate
     */
    public static final String URL_InvestorProfile = MAIN_DOMAIN + "InvestorProfileAddUpdate.asmx";
    public static final String METHOD_NAME_InvestorProfileAddUpdateBind = "InvestorProfileAddUpdateBind";
    public static final String METHOD_NAME_InvestorProfileAddUpdateRecord = "InvestorProfileAddUpdateRecord";

    /**
     * InvestorViewProfile
     */
    public static final String URL_InvestorViewProfile = MAIN_DOMAIN + "InvestorViewProfile.asmx";
    public static final String METHOD_NAME_getInvestorProfile = "InvestorProfileViewBind";


    /**
     * InvestorBankDetailAddUpdate
     */
    public static final String URL_getInvestorBankDetail = MAIN_DOMAIN + "InvestorBankDetailAddUpdate.asmx";
    public static final String METHOD_NAME_getInvestorBank = "InvestorBankAddUpdateBind";
    public static final String METHOD_NAME_updateInvestorBank = "InvestorBankDetailAddUpdateRecord";

    /**
     * InvestorNomineeDetailAddUpdate
     */
    public static final String URL_InvestorNominee = MAIN_DOMAIN + "InvestorNomineeDetailAddUpdate.asmx";
    public static final String METHOD_NAME_getInvestorNominee = "InvestorNomineeDetailAddUpdateBind";
    public static final String METHOD_NAME_updateInvestorNominee = "InvestorNomineeDetailAddUpdateRecord";

    /**
     * InvestorProfileChangePassword
     */
    public static final String URL_InvestorProfileChangePassword = MAIN_DOMAIN + "InvestorProfileChangePassword.asmx";
    public static final String METHOD_NAME_InvestorProfileChangeOldPassword = "InvestorProfileChangeOldPassword";
    public static final String METHOD_NAME_InvestorProfileOldPasswordBind = "InvestorProfileOldPasswordBind";


    /**
     * InvestorProfileCommunicationDetailAddUpdate
     */
    public static final String URL_InvestorProfileCommunication = MAIN_DOMAIN + "InvestorProfileCommunicationDetailAddUpdate.asmx";
    public static final String METHOD_NAME_getInvestorCommunication = "InvestorCommunicationDetailAddUpdateBind";
    public static final String METHOD_NAME_updateInvestorCommunication = "InvestorCommunicationDetailAddUpdateRecord";

    /**
     * InvestorProfilePhoto
     */
    public static final String URL_InvestorProfilePhoto = MAIN_DOMAIN + "InvestorChangeProfilePhoto.asmx";
    public static final String METHOD_NAME_getInvestorProfilePhoto = "InvestorChangeProfilePhoto_Bind";
    public static final String METHOD_NAME_updateInvestorProfilePhoto = "InvestorPhotoAddUpdateRecord";

    /**
     * getState And City
     */
    public static final String URL_state_and_city = MAIN_DOMAIN + "getState.asmx";
    public static final String METHOD_NAME_getState = "GetStateData";
    public static final String METHOD_NAME_getCity = "GetDistrictData";

    /**
     * FixedDeposit
     */
    public static final String URL_getFixed = MAIN_DOMAIN + "NewFixedDeposit.asmx";
    public static final String METHOD_NAME_getFixed = "AvilableFixedDeposit";
    public static final String METHOD_NAME_getScheme = "BindScheme";
    public static final String METHOD_NAME_getVRates = "FixedVariableRatesBind";
    public static final String METHOD_NAME_getTenureB = "TenureBind";
    public static final String METHOD_NAME_getTenureTB = "TenureTypeBind";
    public static final String METHOD_NAME_updateDataBind = "DataBind";
    public static final String METHOD_NAME_postFixedDeposit = "FixedDepositInsert";

    /*WebServices/CheckAvailabilityEmailMobile.asmx*/
    /*http://www.fintrackindia.com/WebServices/ValidateOTP.asmx*/

    /*http://www.fintrackindia.com/WebServices/getState.asmx*/

    /*http://www.fintrackindia.com/WebServices/NewFixedDeposit.asmx*/

   /* public static final String URL_Enquery = "http://www.fintrackindia.com/WebServices/EnquiryInsertRecord.asmx";
    public static String MainDomain = "http://www.fintrackindia.com/WebServices/CustomerLoginAnroid.asmx";
    public static final String GetCustomerLogin = MainDomain + "/Get_CustomerLoginDataAnroid";*/

   /*InvestorBankDetailAddUpdate.asmx
    InvestorNomineeDetailAddUpdate.asmx
    InvestorProfileAddUpdate.asmx
    InvestorProfileChangePassword.asmx
    InvestorProfileCommunicationDetailAddUpdate.asmx*/

    /*InvestorChangeProfilePhoto.asmx*/


}
