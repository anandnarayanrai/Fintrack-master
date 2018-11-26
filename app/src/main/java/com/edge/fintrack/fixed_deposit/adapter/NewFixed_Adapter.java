package com.edge.fintrack.fixed_deposit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.fixed_deposit.BuyFixedDepositActivity;
import com.edge.fintrack.fixed_deposit.model_class.NewFixed_ListItem;

import java.util.List;

public class NewFixed_Adapter extends RecyclerView.Adapter<NewFixed_Adapter.myViewHolder> {
    private Context context;
    private List<NewFixed_ListItem> newFixed_listItemList;


    public NewFixed_Adapter(Context context, List<NewFixed_ListItem> newFixed_listItemList) {
        this.context = context;
        this.newFixed_listItemList = newFixed_listItemList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_newfixed, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final NewFixed_ListItem newFixedListItem = newFixed_listItemList.get(position);
        holder.tv_dName.setText(newFixedListItem.getdName());
        holder.tv_dMinAmount.setText("₹ " + newFixedListItem.getdMinAmount());
        holder.tv_dRate1.setText(newFixedListItem.getdRate1());
        holder.tv_dRate3.setText(newFixedListItem.getdRate3());
        holder.tv_dRate5.setText(newFixedListItem.getdRate5());
        holder.tv_dSrC.setText(newFixedListItem.getdSrC());

        holder.iv_logo.setImageDrawable(newFixedListItem.getdImage());
       /* if (position % 2 == 0) {
            holder.iv_logo.setImageDrawable(context.getDrawable(R.drawable.hdfc_life));
        } else {
            holder.iv_logo.setImageDrawable(context.getDrawable(R.drawable.sbi_life));
        }*/

        holder.bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentBuyFixed = new Intent(context, BuyFixedDepositActivity.class);
                intentBuyFixed.putExtra("Company_ID", newFixedListItem.getdCompany_ID());
                intentBuyFixed.putExtra("Issuer", newFixedListItem.getdName());
                intentBuyFixed.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intentBuyFixed);
                // context.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newFixed_listItemList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        /*Min Amount*/
        TextView tv_dName, tv_dMinAmount, tv_dRate1, tv_dRate3, tv_dRate5, tv_dSrC;
        Button bt_buy;
        ImageView iv_logo;

        public myViewHolder(View itemView) {
            super(itemView);
            iv_logo = (ImageView) itemView.findViewById(R.id.iv_logo);
            tv_dName = (TextView) itemView.findViewById(R.id.tv_dName);
            tv_dMinAmount = (TextView) itemView.findViewById(R.id.tv_dMinAmount);
            tv_dRate1 = (TextView) itemView.findViewById(R.id.tv_dRate1);
            tv_dRate3 = (TextView) itemView.findViewById(R.id.tv_dRate3);
            tv_dRate5 = (TextView) itemView.findViewById(R.id.tv_dRate5);
            tv_dSrC = (TextView) itemView.findViewById(R.id.tv_dSrC);
            bt_buy = (Button) itemView.findViewById(R.id.bt_buy);
        }
    }
}
