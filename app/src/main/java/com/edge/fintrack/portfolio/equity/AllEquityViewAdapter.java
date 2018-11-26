package com.edge.fintrack.portfolio.equity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.fixed_deposit.model_class.ViewFixed_ListItem;

import java.util.List;

public class AllEquityViewAdapter extends RecyclerView.Adapter<AllEquityViewAdapter.myViewHolder> {
    Drawable drawable;
    private Context context;
    private List<ViewFixed_ListItem> viewFixed_listItemList;

    public AllEquityViewAdapter(Context context, List<ViewFixed_ListItem> viewFixed_listItemList) {
        this.context = context;
        this.viewFixed_listItemList = viewFixed_listItemList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_equity_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        final ViewFixed_ListItem viewFixedListItem = viewFixed_listItemList.get(position);
        holder.tv_dName.setText(viewFixedListItem.getdSName());

        //holder.dRate.setText(viewFixedListItem.getdInterest());

/*        holder.bt_getdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if (position % 2 == 0) {
                    drawable = context.getDrawable(R.drawable.sbi_life);
                } else {
                    drawable = context.getDrawable(R.drawable.hdfc_life);
                }*//*
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.activity_detail_fixed_deposit, null);

                TextView tv_iName = (TextView) customView.findViewById(R.id.tv_iName);
                tv_iName.setText(viewFixedListItem.getdSName());

                TextView tv_dMinAmount = (TextView) customView.findViewById(R.id.tv_dMinAmount);
                tv_dMinAmount.setText("Invested Amount \n" + "₹ " + viewFixedListItem.getdInvestAmount());

                TextView tv_iRate = (TextView) customView.findViewById(R.id.tv_iRate);
                tv_iRate.setText(viewFixedListItem.getdInterest() + "- Interest");

                TextView tv_fdNumber = (TextView) customView.findViewById(R.id.tv_fdNumber);
                tv_fdNumber.setText("FD No.:" + viewFixedListItem.getdFDNo());

                TextView tv_iTenure = (TextView) customView.findViewById(R.id.tv_iTenure);
                tv_iTenure.setText("Tenure :" + viewFixedListItem.getdInterest());

                TextView tv_type = (TextView) customView.findViewById(R.id.tv_type);
                tv_type.setText("Type :" + viewFixedListItem.getdInterest());

                TextView tv_mValue = (TextView) customView.findViewById(R.id.tv_mValue);
                tv_mValue.setText("Maturity Value * :" + viewFixedListItem.getdInterest());

                TextView tv_iDate = (TextView) customView.findViewById(R.id.tv_iDate);
                tv_iDate.setText("Investment Start Date :" + viewFixedListItem.getdInvestDate());

                TextView tv_fvRate = (TextView) customView.findViewById(R.id.tv_fvRate);
                tv_fvRate.setText("Fixed and Variable Rates :" + viewFixedListItem.getdInvestAmount());

                TextView tv_dMaturity = (TextView) customView.findViewById(R.id.tv_dMaturity);
                tv_dMaturity.setText("Date Of Maturity :" + viewFixedListItem.getdInvestDate());

                TextView tv_fScheme = (TextView) customView.findViewById(R.id.tv_fScheme);
                tv_fScheme.setText("Scheme Name :" + viewFixedListItem.getdSName());

                new BottomDialog.Builder(context)
                        .setTitle(viewFixedListItem.getdSName())
                        *//*  .setContent("Glad to see you like BottomDialogs! If you're up for it, we would really appreciate you reviewing us.")*//*
                        .setCustomView(customView)
                        .setIcon(viewFixedListItem.getdImage())
                        .setPositiveText("OK")
                        .setPositiveBackgroundColorResource(R.color.colorPrimary)
                        //.setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary)
                        *//* .setPositiveTextColorResource(android.R.color.white)
                         .setNegativeText("Exit")*//*
                        .setNegativeTextColorResource(R.color.colorAccent)
                        //.setNegativeTextColor(ContextCompat.getColor(this, R.color.colorAccent)
                        *//* .onNegative(new BottomDialog.ButtonCallback() {
                             @Override
                             public void onClick(BottomDialog dialog) {
                                 Log.d("BottomDialogs", "Do something!");
                             }
                         })*//*
                        .show();
            }
        });*/

        /*Invest Amount*/
    }

    @Override
    public int getItemCount() {
        return viewFixed_listItemList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        /*Min Amount*/
        TextView tv_dName, tv_qty, dRate, tv_avg_price, tv_lto;

        public myViewHolder(View itemView) {
            super(itemView);
            tv_dName = (TextView) itemView.findViewById(R.id.tv_dName);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_qty);
            dRate = (TextView) itemView.findViewById(R.id.dRate);
            tv_avg_price = (TextView) itemView.findViewById(R.id.tv_avg_price);
            tv_lto = (TextView) itemView.findViewById(R.id.tv_lto);
        }
    }
}