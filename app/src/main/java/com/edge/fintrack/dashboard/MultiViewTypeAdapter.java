package com.edge.fintrack.dashboard;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edge.fintrack.R;
import com.edge.fintrack.portfolio.PortfolioActivity;
import com.edge.fintrack.product.ProductFragment;

import java.util.ArrayList;

import static com.edge.fintrack.utility.Constant.startNewActivity;


public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;

    public static class FunctionTypeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Title;
        TextView tv_Description;
        TextView tv_ButtomText;
        ImageView iv_icon;

        public FunctionTypeViewHolder(View itemView) {
            super(itemView);
            this.tv_Title = (TextView) itemView.findViewById(R.id.tv_Title);
            this.tv_Description = (TextView) itemView.findViewById(R.id.tv_Description);
            this.tv_ButtomText = (TextView) itemView.findViewById(R.id.tv_ButtomText);
            this.iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }

    }

    public static class NewsTypeViewHolder extends RecyclerView.ViewHolder {
        TextView tv_news;

        public NewsTypeViewHolder(View itemView) {
            super(itemView);
            this.tv_news = (TextView) itemView.findViewById(R.id.tv_news);
        }

    }

    public MultiViewTypeAdapter(ArrayList<Model> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.FUNCTION_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.function_type, parent, false);
                return new FunctionTypeViewHolder(view);
            case Model.NEWS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_type, parent, false);
                return new NewsTypeViewHolder(view);
        }
        return null;

    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).getLayout_Type()) {
            case 0:
                return Model.FUNCTION_TYPE;
            case 1:
                return Model.NEWS_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        final Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.getLayout_Type()) {
                case Model.FUNCTION_TYPE:
                    ((FunctionTypeViewHolder) holder).tv_Title.setText(object.getTitle());
                    ((FunctionTypeViewHolder) holder).tv_Description.setText(object.getDescription());
                    ((FunctionTypeViewHolder) holder).tv_ButtomText.setText(object.getButtomText());
                    ((FunctionTypeViewHolder) holder).iv_icon.setImageResource(object.getImage());
                    ((FunctionTypeViewHolder) holder).tv_ButtomText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (object.getButtomText()) {
                                case "My Portfolio":
                                    Intent intentUpdateProfile = new Intent(mContext, PortfolioActivity.class);
                                    intentUpdateProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intentUpdateProfile);
                                    // Objects.requireNonNull(mContext)).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                                    break;
                                case "Add Investment":
                                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                                    FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, new ProductFragment());
                                    fragmentTransaction.commit();
                                    break;
                                case "Add Money":
                                    startNewActivity(mContext, "com.fintrackindia.investmentmanager");
                                    break;
                            }
                        }
                    });
                    break;
                case Model.NEWS_TYPE:
                    ((NewsTypeViewHolder) holder).tv_news.setText(object.getDescription());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
