package com.edge.fintrack.mutual_funds;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.WebViewActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeTabFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Integer[] IMAGES_ICON = {R.drawable.insurance, R.drawable.insurance, R.drawable.insurance};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public final String TAG = "HomeTabFragment";
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private TextView tv_cSIP, tv_inLS, tv_rSIP, tv_susTP, tv_sysWP, tv_traFund;


    public HomeTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeTabFragment newInstance(String param1, String param2) {
        HomeTabFragment fragment = new HomeTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);


        tv_cSIP = (TextView) view.findViewById(R.id.tv_cSIP);
        tv_cSIP.setOnClickListener(this);
        tv_inLS = (TextView) view.findViewById(R.id.tv_inLS);
        tv_inLS.setOnClickListener(this);
        tv_rSIP = (TextView) view.findViewById(R.id.tv_rSIP);
        tv_rSIP.setOnClickListener(this);
        tv_susTP = (TextView) view.findViewById(R.id.tv_susTP);
        tv_susTP.setOnClickListener(this);
        tv_sysWP = (TextView) view.findViewById(R.id.tv_sysWP);
        tv_sysWP.setOnClickListener(this);
        tv_traFund = (TextView) view.findViewById(R.id.tv_traFund);
        tv_traFund.setOnClickListener(this);
        return view;
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

    @Override
    public void onClick(View v) {
        Intent intentWebView = new Intent(getContext(), WebViewActivity.class);
        Intent intentTransferFunds = new Intent(getContext(), TransferFundsActivity.class);
        switch (v.getId()) {
            case R.id.tv_cSIP:
                intentWebView.putExtra("url", "https://www.mfuonline.com/");
                intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWebView);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_inLS:
                intentWebView.putExtra("url", "https://www.mfuonline.com/");
                intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWebView);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_rSIP:
                intentWebView.putExtra("url", "https://www.mfuonline.com/");
                intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWebView);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_susTP:
                intentWebView.putExtra("url", "https://www.mfuonline.com/");
                intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWebView);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_sysWP:
                intentWebView.putExtra("url", "https://www.mfuonline.com/");
                intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWebView);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_traFund:
                intentTransferFunds.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentTransferFunds);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
        }
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

}
