package com.edge.fintrack.portfolio.equity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.edge.fintrack.R;
import com.edge.fintrack.fixed_deposit.model_class.ViewFixed_ListItem;

import java.util.ArrayList;
import java.util.List;

public class AllEquityActivity extends AppCompatActivity {
    private RecyclerView r_view_equitylist;
    private AllEquityViewAdapter allEquityViewAdapter;
    private List<ViewFixed_ListItem> viewFixed_listItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_equity);

        r_view_equitylist = (RecyclerView) findViewById(R.id.r_view_equitylist);
        allEquityViewAdapter = new AllEquityViewAdapter(this, viewFixed_listItemList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        r_view_equitylist.setLayoutManager(mLayoutManager);
        r_view_equitylist.setItemAnimator(new DefaultItemAnimator());
        r_view_equitylist.setAdapter(allEquityViewAdapter);
        prepareData();
    }

    private void prepareData() {
        /*for (int i = 0; i < 20; i++) {
            ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem("HDFC Ltd" + String.valueOf(i), "20000", "7.90%", "Aug 01,2018", "123456789", "123456789");
            viewFixed_listItemList.add(viewFixedListItem);
        }*/

        ViewFixed_ListItem viewFixedListItem = new ViewFixed_ListItem("DHFL", "200", "6000", "8000", "7.85%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("SBIN", "500", "2000", "5000", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("Yes Bank", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("TATA Power", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("BHEL", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("VEDl", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("BHEL", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        viewFixedListItem = new ViewFixed_ListItem("VEDl", "600", "68000", "71245", "8.50%");
        viewFixed_listItemList.add(viewFixedListItem);

        allEquityViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}
