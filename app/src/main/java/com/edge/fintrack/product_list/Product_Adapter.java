package com.edge.fintrack.product_list;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edge.fintrack.R;

import java.util.List;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.Product_Adapter_ViewHolder> {

    private List<Product_list> product_lists;

    public Product_Adapter(List<Product_list> product_lists) {
        this.product_lists = product_lists;
    }

    @NonNull
    @Override
    public Product_Adapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product, parent, false);

        return new Product_Adapter_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_Adapter_ViewHolder holder, int position) {
        Product_list productList = product_lists.get(position);
        holder.tv_pname.setText(productList.getName());
        holder.tv_valus.setText(productList.getValus());
        holder.tv_discription.setText(productList.getDiscription());
        // holder.rl_view.setBackgroundColor(Color.parseColor(productList.getColor()));
        Log.e("getName", "getName" + productList.getName());
        // Set a background color for CardView
        holder.cardview.setCardBackgroundColor(Color.parseColor(productList.getColor()));

        // Set the CardView maximum elevation
        holder.cardview.setMaxCardElevation(15);

        // Set CardView elevation
        holder.cardview.setCardElevation(9);

    }

    @Override
    public int getItemCount() {
        return product_lists.size();
    }

    public class Product_Adapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_pname;
        TextView tv_valus;
        TextView tv_discription;
        RelativeLayout rl_view;
        CardView cardview;

        public Product_Adapter_ViewHolder(View itemView) {
            super(itemView);
            tv_pname = (TextView) itemView.findViewById(R.id.tv_pname);
            tv_valus = (TextView) itemView.findViewById(R.id.tv_valus);
            tv_discription = (TextView) itemView.findViewById(R.id.tv_discription);
            rl_view = (RelativeLayout) itemView.findViewById(R.id.rl_view);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}
