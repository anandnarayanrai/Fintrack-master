package com.edge.fintrack.product;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edge.fintrack.R;
import com.edge.fintrack.WebViewActivity;
import com.edge.fintrack.anim_viewpager.DepthPageTransformer;
import com.edge.fintrack.fixed_deposit.FixedDepositActivity;
import com.edge.fintrack.insurance.InsuranceActivity;
import com.edge.fintrack.mutual_funds.MutualFundHomeActivity;
import com.edge.fintrack.mutual_funds.SlidingBanner_Adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Integer[] IMAGES = {R.drawable.mutual_fund, R.drawable.fixed_deposit};
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    TextView tv_mutualfund, tv_nps, tv_insurance, tv_fixed_deposit, tv_bonds, tv_equity_trading;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        mPager = (ViewPager) view.findViewById(R.id.vp_banner);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        tv_mutualfund = (TextView) view.findViewById(R.id.tv_mutualfund);
        tv_mutualfund.setOnClickListener(this);

        tv_nps = (TextView) view.findViewById(R.id.tv_nps);
        tv_nps.setOnClickListener(this);

        tv_insurance = (TextView) view.findViewById(R.id.tv_insurance);
        tv_insurance.setOnClickListener(this);

        tv_fixed_deposit = (TextView) view.findViewById(R.id.tv_fixed_deposit);
        tv_fixed_deposit.setOnClickListener(this);

        tv_bonds = (TextView) view.findViewById(R.id.tv_bonds);
        tv_bonds.setOnClickListener(this);

        tv_equity_trading = (TextView) view.findViewById(R.id.tv_equity_trading);
        tv_equity_trading.setOnClickListener(this);
        tv_mutualfund.setClickable(true);
        tv_nps.setClickable(true);
        tv_insurance.setClickable(true);
        tv_fixed_deposit.setClickable(true);
        tv_bonds.setClickable(true);
        tv_equity_trading.setClickable(true);
        init();
        return view;
    }

    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        mPager.setAdapter(new SlidingBanner_Adapter(getActivity(), ImagesArray));

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 10000);

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
        switch (v.getId()) {
            case R.id.tv_mutualfund:
                tv_mutualfund.setClickable(false);
                Intent intentMutualFund = new Intent(getActivity(), MutualFundHomeActivity.class);
                intentMutualFund.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentMutualFund);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_nps:
                tv_nps.setClickable(false);
                Intent intentWebView = new Intent(getActivity(), WebViewActivity.class);
                intentWebView.putExtra("url", "https://online.stockholding.com/nps_entry/OlnNPSHome.aspx?RefID=15072018");
                intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWebView);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_insurance:
                tv_insurance.setClickable(false);
                Intent intentInsurance = new Intent(getActivity(), InsuranceActivity.class);
                intentInsurance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentInsurance);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_fixed_deposit:
                tv_fixed_deposit.setClickable(false);
                Intent intentFixedDeposit = new Intent(getActivity(), FixedDepositActivity.class);
                intentFixedDeposit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentFixedDeposit);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.tv_bonds:
                // tv_bonds.setClickable(false);
                Toast.makeText(getActivity(), "This Functionality will be update soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_equity_trading:
                //tv_equity_trading.setClickable(false);
                Toast.makeText(getActivity(), "This Functionality will be update soon", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tv_mutualfund.setClickable(true);
        tv_nps.setClickable(true);
        tv_insurance.setClickable(true);
        tv_fixed_deposit.setClickable(true);
        tv_bonds.setClickable(true);
        tv_equity_trading.setClickable(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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
