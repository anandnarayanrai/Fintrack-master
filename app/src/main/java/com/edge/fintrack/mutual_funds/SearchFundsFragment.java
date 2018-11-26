package com.edge.fintrack.mutual_funds;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edge.fintrack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFundsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFundsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFundsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final String URL_GET_DATA = "https://simplifiedcoding.net/demos/marvel/";
    private final String[] array = {"Invesco Growth Opportunities - D (G)", "ICICI Pru Bluechip Fund - D (G)", "HSBC Large Cap Equity Fund (G)", "Small & Mid Cap", "Axis Mid Cap Fund - Direct (G)", "HDFC Small Cap Fund - Direct (G)", "Axis Mid Cap Fund (G)", "Axis Focused 25 Fund - Direct (G)"};

    private final String[] array1 = {"Invesco Growth Opportunities - D (G)", "ICICI Pru Bluechip Fund - D (G)", "HSBC Large Cap Equity Fund (G)", "Small & Mid Cap", "Axis Mid Cap Fund - Direct (G)", "HDFC Small Cap Fund - Direct (G)", "Axis Mid Cap Fund (G)", "Mutual Fund7", "\n" +
            "Large Cap\tCrisil Rank\t6mth (%)\t1yr (%)\t3yr (%)\n" +
            "Invesco Growth Opportunities - D (G)\t    \t2.6%\t15.5%\t13.9%\n" +
            "ICICI Pru Bluechip Fund - D (G)\t    \t0.3%\t11.3%\t12.4%\n" +
            "HSBC Large Cap Equity Fund (G)\t    \t0.5%\t8.5%\t11.3%\n" +
            "Small & Mid Cap\tCrisil Rank\t6mth (%)\t1yr (%)\t3yr (%)\n" +
            "Axis Mid Cap Fund - Direct (G)\t    \t8.3%\t18.9%\t10.5%\n" +
            "HDFC Small Cap Fund - Direct (G)\t    \t-1.7%\t18.9%\t19.7%", "Mutual Fund9", "Mutual Fund10"};

    RecyclerView recyclerView;
    FoldingLayoutAdapter adapter;
    List<Model_fund_list> heroList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public SearchFundsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFundsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFundsFragment newInstance(String param1, String param2) {
        SearchFundsFragment fragment = new SearchFundsFragment();
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
        View view = inflater.inflate(R.layout.fragment_search_funds, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        heroList = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            Model_fund_list hero = new Model_fund_list(
                    array[i],
                    "MutualFund",
                    "MutualFund",
                    "MutualFund",
                    "MutualFund",
                    "MutualFund",
                    "MutualFund",
                    "MutualFund"
            );
            heroList.add(hero);
        }
        adapter = new FoldingLayoutAdapter(heroList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //loadHeroes();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void loadHeroes() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_GET_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < 7; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                               /* Model_fund_list hero = new Model_fund_list(
                                        obj.getString("name"),
                                        obj.getString("realname"),
                                        obj.getString("team"),
                                        obj.getString("firstappearance"),
                                        obj.getString("createdby"),
                                        obj.getString("publisher"),
                                        obj.getString("imageurl"),
                                        obj.getString("bio")
                                );*/

                                Model_fund_list hero = new Model_fund_list(
                                        "MutualFund",
                                        "MutualFund",
                                        "MutualFund",
                                        "MutualFund",
                                        "MutualFund",
                                        "MutualFund",
                                        "MutualFund",
                                        "MutualFund"
                                );

                                heroList.add(hero);
                            }
                            adapter = new FoldingLayoutAdapter(heroList, getContext());
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
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
