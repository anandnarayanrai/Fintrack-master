package com.edge.fintrack.mutual_funds.expendable_recyclerview;

import java.util.List;

public class MovieCategory implements ParentListItem {
    private String mName;
    private int mIcon;
    private List<Movies> mMovies;

    public MovieCategory(int mIcon, String name, List<Movies> movies) {
        mName = name;
        mMovies = movies;
        mIcon = mIcon;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return mIcon;
    }

    @Override
    public List<?> getChildItemList() {
        return mMovies;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
