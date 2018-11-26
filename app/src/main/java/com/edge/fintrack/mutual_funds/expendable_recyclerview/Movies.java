package com.edge.fintrack.mutual_funds.expendable_recyclerview;

public class Movies {
    private String mName;
    private int mIcon;

    public Movies(int mIcon, String name) {
        mName = name;
        mIcon = mIcon;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

}
