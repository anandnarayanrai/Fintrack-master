package com.edge.fintrack.portfolio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.edge.fintrack.R;
import com.edge.fintrack.utility.RecyclerTouchListener;
import com.edge.fintrack.fixed_deposit.model_class.ViewFixed_ListItem;
import com.edge.fintrack.portfolio.equity.AddEquityActivity;
import com.edge.fintrack.portfolio.equity.AllEquityActivity;
import com.edge.fintrack.portfolio.fincome.FixedIncomeActivity;
import com.edge.fintrack.portfolio.mfund.AddMFundActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PortfolioActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fab_equity, fab_mutualfund, fab_fixcome;
    private PieChart mChart;
    private RelativeLayout layout_mf, layout_equity;
    private RecyclerView r_view_invitem;
    private PortfolioActivity_RAdapter portfolioActivity_rAdapter;
    private List<ViewFixed_ListItem> viewFixed_listItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);
        fab_equity = (FloatingActionButton) findViewById(R.id.fab_equity);
        fab_equity.setOnClickListener(this);

        fab_mutualfund = (FloatingActionButton) findViewById(R.id.fab_mutualfund);
        fab_mutualfund.setOnClickListener(this);

        fab_fixcome = (FloatingActionButton) findViewById(R.id.fab_fixcome);
        fab_fixcome.setOnClickListener(this);

        mChart = (PieChart) findViewById(R.id.mChart);
        setPieChart();

       /* layout_mf = (RelativeLayout) findViewById(R.id.layout_mf);
        layout_mf.setOnClickListener(this);

        layout_equity = (RelativeLayout) findViewById(R.id.layout_equity);
        layout_equity.setOnClickListener(this);*/

        r_view_invitem = (RecyclerView) findViewById(R.id.r_view_invitem);
        portfolioActivity_rAdapter = new PortfolioActivity_RAdapter(this, viewFixed_listItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        r_view_invitem.setLayoutManager(mLayoutManager);
        r_view_invitem.setItemAnimator(new DefaultItemAnimator());
        r_view_invitem.setAdapter(portfolioActivity_rAdapter);
        r_view_invitem.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), r_view_invitem, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
               /* currentCategory = categoriesList[position];
                selectedCategory = currentCategory.getName();
                tv_category.setText(currentCategory.getName());
                rl_touch.setVisibility(View.GONE);*/

                //Toast.makeText(getApplicationContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

                Intent IntentInvestmentsActivity = new Intent(PortfolioActivity.this, AllEquityActivity.class);
                IntentInvestmentsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(IntentInvestmentsActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareData();
    }

    private void prepareData() {
        /*for (int i = 0; i < 20; i++) {
            ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem("HDFC Ltd" + String.valueOf(i), "20000", "7.90%", "Aug 01,2018", "123456789", "123456789");
            viewFixed_listItemList.add(viewFixedListItem);
        }*/

        /*tv_dName, tv_netpl, tv_einvestmentAmount, tv_ecurrentValue, tv_eplv;*/

        ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.ic_bar_chart), "Equity", "200", "6000", "8000", "7.85%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.ic_equity), "Mutual Fund", "500", "2000", "5000", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem(getResources().getDrawable(R.drawable.ic_equity), "Fixed Income", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        portfolioActivity_rAdapter.notifyDataSetChanged();
    }

    public void setPieChart() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(true);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setHoleColor(Color.WHITE);
        mChart.animateY(2000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(34f, "Equity"));
        yValues.add(new PieEntry(56f, "M Fund"));
        yValues.add(new PieEntry(66f, "Fixed Income"));
        yValues.add(new PieEntry(45f, "Other Investment"));

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(10f);
        pieData.setValueTextColor(Color.YELLOW);
        //mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText("Investment Amount");
        mChart.setData(pieData);
        //PieChart Ends Here
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_equity:
                Intent intentAddEquityActivity = new Intent(this, AddEquityActivity.class);
                intentAddEquityActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAddEquityActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.fab_mutualfund:
                Intent intentAddMFundActivity = new Intent(this, AddMFundActivity.class);
                intentAddMFundActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAddMFundActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.fab_fixcome:
                Intent intenFixedIncomeActivity = new Intent(this, FixedIncomeActivity.class);
                intenFixedIncomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intenFixedIncomeActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
          /*  case R.id.layout_equity:
                Intent intentAllActivity = new Intent(this, AllEquityActivity.class);
                intentAllActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentAllActivity);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;*/
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
