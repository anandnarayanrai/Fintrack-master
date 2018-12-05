package com.edge.fintrack.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.WebViewActivity;
import com.edge.fintrack.anim_viewpager.DepthPageTransformer;
import com.edge.fintrack.mutual_funds.SlidingBanner_Adapter;
import com.edge.fintrack.portfolio.PortfolioActivity;
import com.edge.fintrack.product.ProductFragment;
import com.edge.fintrack.utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.edge.fintrack.utility.Constant.startNewActivity;

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
    private static final Integer[] IMAGES = {R.drawable.background_nav_header, R.drawable.advertising_image3};
    private static ViewPager vp_advertising;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private RecyclerView r_view_advertising;

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

        vp_advertising = (ViewPager) view.findViewById(R.id.vp_advertising);
        vp_advertising.setPageTransformer(true, new DepthPageTransformer());

        r_view_advertising = (RecyclerView) view.findViewById(R.id.r_view_advertising);
        r_view_advertising.addOnItemTouchListener(new RecyclerTouchListener(getContext(), r_view_advertising, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               /* currentCategory = categoriesList[position];
                selectedCategory = currentCategory.getName();
                tv_category.setText(currentCategory.getName());
                rl_touch.setVisibility(View.GONE);*/
                if (position > 2) {
                    Intent intentWebView = new Intent(getContext(), WebViewActivity.class);
                    intentWebView.putExtra("url", "http://blog.fintrackindia.com/Details/Index/5/mfs-pump-rs-11000-cr-in-equities-last-fortnig");
                    intentWebView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentWebView);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareData();
        init();
        return view;
    }

    private void init() {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);

        vp_advertising.setAdapter(new SlidingBanner_Adapter(getActivity(), ImagesArray));

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                vp_advertising.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 10000, 30000);

    }

    private void prepareData() {
        ArrayList<Model> list = new ArrayList<>();
        list.add(new Model(Model.FUNCTION_TYPE, "My Portfolio", "Hi. I display a cool image too besides the omnipresent TextView.", "My Portfolio", R.drawable.ic_suitcase));
        list.add(new Model(Model.FUNCTION_TYPE, "My Investment", "Hey. Pressing the FAB button will playback an audio file on loop.", "Add Investment", R.drawable.ic_equity));
        list.add(new Model(Model.FUNCTION_TYPE, "My Money", "Hi again. Another cool image here. Which one is better?", "Add Money", R.drawable.ic_sip));
        list.add(new Model(Model.NEWS_TYPE, "MUTUAL FUND", "Mutual fund houses have made investments of over Rs 11,000 crore in domestic equities.", "Read More", 0));
        list.add(new Model(Model.NEWS_TYPE, "MUTUAL FUND", "Mutual fund houses have made investments of over Rs 11,000 crore in domestic equities in the first two weeks of this month despite volatility in the stock markets,", "Read More", 0));
        list.add(new Model(Model.NEWS_TYPE, "MUTUAL FUND", "Mutual fund houses have made investments of over Rs 11,000 crore in domestic equities in the first two weeks of this month despite volatility in the stock markets, even as foreign investors pulled out a massive Rs 19,000 crore. This comes following a net infusion of Rs 11,600 crore in equities by the fund managers a ...", "Read More", 0));

        MultiViewTypeAdapter adapter = new MultiViewTypeAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);

        r_view_advertising.setLayoutManager(linearLayoutManager);
        r_view_advertising.setItemAnimator(new DefaultItemAnimator());
        r_view_advertising.setAdapter(adapter);

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

                startNewActivity(getContext(), "com.fintrackindia.investmentmanager");
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
