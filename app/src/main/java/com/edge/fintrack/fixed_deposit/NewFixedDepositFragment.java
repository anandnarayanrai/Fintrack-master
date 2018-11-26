package com.edge.fintrack.fixed_deposit;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.edge.fintrack.R;
import com.edge.fintrack.fixed_deposit.adapter.NewFixed_Adapter;
import com.edge.fintrack.fixed_deposit.model_class.NewFixed_ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.edge.fintrack.Api_Class.METHOD_NAME_getFixed;
import static com.edge.fintrack.Api_Class.NAMESPACE;
import static com.edge.fintrack.Api_Class.SOAP_ACTION;
import static com.edge.fintrack.Api_Class.URL_getFixed;
import static com.edge.fintrack.Constant.ShowDilog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewFixedDepositFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewFixedDepositFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewFixedDepositFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String TAG = "NewFixedDepositFragment";
    SoapPrimitive resultString;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView r_view_newlist;
    private NewFixed_Adapter newFixed_adapter;
    private List<NewFixed_ListItem> newFixed_listItemList = new ArrayList<>();

    private ProgressDialog mProgressDialog;

    public NewFixedDepositFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewFixedDepositFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewFixedDepositFragment newInstance(String param1, String param2) {
        NewFixedDepositFragment fragment = new NewFixedDepositFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_fixed_deposit, container, false);
        r_view_newlist = (RecyclerView) view.findViewById(R.id.r_view_newlist);

        newFixed_adapter = new NewFixed_Adapter(getActivity(), newFixed_listItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        r_view_newlist.setLayoutManager(mLayoutManager);
        r_view_newlist.setItemAnimator(new DefaultItemAnimator());
        r_view_newlist.setAdapter(newFixed_adapter);
        //prepareData();

        getFixed getState = new getFixed();
        getState.execute();

        return view;
    }

    private void prepareData() {
        /*for (int i = 0; i < 10; i++) {
            NewFixed_ListItem newFixedListItem = new NewFixed_ListItem("HDFC Ltd" + String.valueOf(i), "20000", "7.90%", "7.75%", "7.85%", "0.25");
            newFixed_listItemList.add(newFixedListItem);
        }*/

        NewFixed_ListItem newFixedListItem = new NewFixed_ListItem(getResources().getDrawable(R.drawable.hdfc_fd_logo), "HDFC Ltd", "20000", "7.90%", "7.75%%", "7.85%", "0.25%");
        newFixed_listItemList.add(newFixedListItem);

        newFixedListItem = new NewFixed_ListItem(getResources().getDrawable(R.drawable.dhfl_fd_logo), "DHFL", "5000", "7.70%%", "7.75%", "8.50%", "0.25%");
        newFixed_listItemList.add(newFixedListItem);

        newFixedListItem = new NewFixed_ListItem(getResources().getDrawable(R.drawable.mahindra_fd_logo), "Mahindra Finance", "5000", "7.70%", "8.25%", "8.50%", "0.25%");
        newFixed_listItemList.add(newFixedListItem);

        newFixedListItem = new NewFixed_ListItem(getResources().getDrawable(R.drawable.lic_hfl_fd_logo), "LIC Housing Finance", "10,000", "7.30%", "7.45%", "7.45%", "0.10%");
        newFixed_listItemList.add(newFixedListItem);

        newFixed_adapter.notifyDataSetChanged();
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        r_view_newlist.setLayoutAnimation(animation);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } /*else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class getFixed extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = ProgressDialog.show(getContext(), null, null, true);
            mProgressDialog.setContentView(R.layout.layout_progressdialog);
            Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            try {
                SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME_getFixed);

                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE transport = new HttpTransportSE(URL_getFixed);

                transport.call(SOAP_ACTION + METHOD_NAME_getFixed, soapEnvelope);
                resultString = (SoapPrimitive) soapEnvelope.getResponse();

                Log.i(TAG, "getFixedResult : " + resultString);
            } catch (Exception ex) {
                Log.e(TAG, "getFixed Error: " + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Log.d(TAG, "getFixed Result " + resultString.toString());
                /**
                 * [{"Company_ID":"000001","Company_Name":"HDFC Ltd","Minimum_Invest_Amount":"20000.00",
                 * "FirstYear":"7.90%","ThirdYear":"7.75%     ","FifthYear":"7.85%","SrCitizen":"0.25",
                 * "Display_Order":"1","Status":"7.85%"},{"Company_ID":"000002","Company_Name":"DHFL",
                 * "Minimum_Invest_Amount":"5000.00","FirstYear":"7.70%","ThirdYear":"8. 45%","FifthYear":"8.50%",
                 * "SrCitizen":"0.25%","Display_Order":"2","Status":"8.50%"},{"Company_ID":"000003","Company_Name":"Mahindra & Mahindra Finance",
                 * "Minimum_Invest_Amount":"5000.00","FirstYear":"7.70%","ThirdYear":"8.25%","FifthYear":"8.50%",
                 * "SrCitizen":"0.25%","Display_Order":"3","Status":"8.50%"},{"Company_ID":"000004","Company_Name":"LIC Housing Finance",
                 * "Minimum_Invest_Amount":"10000.00","FirstYear":"7.30%","ThirdYear":"7.45%
                 * ","FifthYear":"7.45%","SrCitizen":"0.10%","Display_Order":"4","Status":"7.45%"}]*/

                mProgressDialog.dismiss();
                JSONArray jsonArray = new JSONArray(resultString.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    //ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.hdfc_fd_logo), "HDFC Ltd", "20000", "7.90%", "7.75%%", "7.85%", "0.25%");
                    NewFixed_ListItem newFixedListItem = new NewFixed_ListItem();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("Company_ID"))
                        newFixedListItem.setdCompany_ID(jsonObject.getString("Company_ID"));
                    if (jsonObject.has("Company_Name"))
                        newFixedListItem.setdName(jsonObject.getString("Company_Name"));
                    if (jsonObject.has("Minimum_Invest_Amount"))
                        newFixedListItem.setdMinAmount(jsonObject.getString("Minimum_Invest_Amount"));
                    if (jsonObject.has("FirstYear"))
                        newFixedListItem.setdRate1(jsonObject.getString("FirstYear"));
                    if (jsonObject.has("ThirdYear"))
                        newFixedListItem.setdRate3(jsonObject.getString("ThirdYear"));
                    if (jsonObject.has("FifthYear"))
                        newFixedListItem.setdRate5(jsonObject.getString("FifthYear"));
                    if (jsonObject.has("SrCitizen"))
                        newFixedListItem.setdSrC(jsonObject.getString("SrCitizen"));

                    if (jsonObject.getString("Company_Name").equalsIgnoreCase("HDFC Ltd")) {
                        newFixedListItem.setdImage(getResources().getDrawable(R.drawable.hdfc_fd_logo));
                    } else if (jsonObject.getString("Company_Name").equalsIgnoreCase("DHFL")) {
                        newFixedListItem.setdImage(getResources().getDrawable(R.drawable.dhfl_fd_logo));
                    } else if (jsonObject.getString("Company_Name").equalsIgnoreCase("Mahindra & Mahindra Finance")) {
                        newFixedListItem.setdImage(getResources().getDrawable(R.drawable.mahindra_fd_logo));
                    } else if (jsonObject.getString("Company_Name").equalsIgnoreCase("LIC Housing Finance")) {
                        newFixedListItem.setdImage(getResources().getDrawable(R.drawable.lic_hfl_fd_logo));
                    }

                    newFixed_listItemList.add(newFixedListItem);
                    newFixed_adapter.notifyDataSetChanged();
                    int resId = R.anim.layout_animation_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    r_view_newlist.setLayoutAnimation(animation);
                }
            } catch (NullPointerException | JSONException | IndexOutOfBoundsException e) {
                mProgressDialog.dismiss();
                Log.d(TAG, "getDetailCallWS Error:- " + e.getMessage());
                ShowDilog(getContext(), "" + getString(R.string.app_name), "" + getString(R.string.went_wrong));
            }
        }
    }
}
