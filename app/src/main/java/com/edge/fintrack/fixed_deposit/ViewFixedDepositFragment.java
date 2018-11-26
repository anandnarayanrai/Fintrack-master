package com.edge.fintrack.fixed_deposit;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edge.fintrack.R;
import com.edge.fintrack.fixed_deposit.adapter.ViewFixed_Adapter;
import com.edge.fintrack.fixed_deposit.model_class.ViewFixed_ListItem;

import org.ksoap2.serialization.SoapPrimitive;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewFixedDepositFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewFixedDepositFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFixedDepositFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String TAG = "ViewFixedDepositFragment";
    SoapPrimitive resultString;
    private BottomSheetBehavior mBottomSheetBehavior;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView r_view_vewlist;
    private ViewFixed_Adapter viewFixed_adapter;
    private List<ViewFixed_ListItem> viewFixed_listItemList = new ArrayList<>();

    public ViewFixedDepositFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewFixedDepositFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFixedDepositFragment newInstance(String param1, String param2) {
        ViewFixedDepositFragment fragment = new ViewFixedDepositFragment();
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
        View view = inflater.inflate(R.layout.fragment_view_fixed_deposit, container, false);
        r_view_vewlist = (RecyclerView) view.findViewById(R.id.r_view_viewlist);

        viewFixed_adapter = new ViewFixed_Adapter(getActivity(), viewFixed_listItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        r_view_vewlist.setLayoutManager(mLayoutManager);
        r_view_vewlist.setItemAnimator(new DefaultItemAnimator());
        r_view_vewlist.setAdapter(viewFixed_adapter);
        prepareData();


//        mBottomSheetBehavior = BottomSheetBehavior.from(view2);
        /*holder.bt_getdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Toast.makeText(context, "bt_getdetail", Toast.LENGTH_SHORT).show();

                mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        // React to state change
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // React to dragging events
                    }
                });
            }
        });*/
        return view;
    }

    private void prepareData() {
        /*for (int i = 0; i < 20; i++) {
            ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem("HDFC Ltd" + String.valueOf(i), "20000", "7.90%", "Aug 01,2018", "123456789", "123456789");
            viewFixed_listItemList.add(viewFixedListItem);
        }*/
        ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.hdfc_fd_logo), "HDFC Ltd", "20000", "7.90%", "7.75%%", "7.85%", "0.25%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.dhfl_fd_logo), "DHFL", "5000", "7.70%%", "7.75%", "8.50%", "0.25%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.mahindra_fd_logo), "Mahindra Finance", "5000", "7.70%", "8.25%", "8.50%", "0.25%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.lic_hfl_fd_logo), "LIC Housing Finance", "10,000", "7.30%", "7.45%", "7.45%", "0.10%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixed_adapter.notifyDataSetChanged();
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


}
