package com.edge.fintrack.drawer_menu.data;


public class BaseItem {
    private String mName;
    private int icon;

    public BaseItem(String name, int icon) {
        mName = name;
        icon = icon;
    }

    public String getName() {
        return mName;
    }

    public int getIcon() {
        return icon;
    }
}
