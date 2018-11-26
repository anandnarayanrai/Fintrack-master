package com.edge.fintrack.drawer_menu.data;


public class GroupItem extends BaseItem {
    private int mLevel;

    public GroupItem(String name, int icon) {
        super(name, icon);
        mLevel = 0;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }
}
