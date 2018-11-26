package com.edge.fintrack.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.portfolio.PortfolioActivity;
import com.edge.fintrack.product.ProductFragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeshboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeshboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeshboardFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tv_addinvestment, tv_addportfolio, tv_Addmonet;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public DeshboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeshboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeshboardFragment newInstance(String param1, String param2) {
        DeshboardFragment fragment = new DeshboardFragment();
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
        View view = inflater.inflate(R.layout.fragment_deshboard, container, false);
        tv_addinvestment = (TextView) view.findViewById(R.id.tv_addinvestment);
        tv_addinvestment.setOnClickListener(this);

        tv_addportfolio = (TextView) view.findViewById(R.id.tv_addportfolio);
        tv_addportfolio.setOnClickListener(this);

        tv_Addmonet = (TextView) view.findViewById(R.id.tv_Addmonet);
        tv_Addmonet.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.tv_addinvestment:
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new ProductFragment());
                fragmentTransaction.commit();
                break;
            case R.id.tv_Addmonet:
               /* Intent intentUpdateProfile = new Intent(getActivity(), AddExpenseActivity.class);
                intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentUpdateProfile);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
*/
                break;
            case R.id.tv_addportfolio:
                Intent intentUpdateProfile = new Intent(getActivity(), PortfolioActivity.class);
                intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentUpdateProfile);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
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
