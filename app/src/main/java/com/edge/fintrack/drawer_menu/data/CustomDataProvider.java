package com.edge.fintrack.drawer_menu.data;

import com.edge.fintrack.R;

import java.util.ArrayList;
import java.util.List;

public class CustomDataProvider {

    private static final int MAX_LEVELS = 3;

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;

    private static List<BaseItem> mMenu = new ArrayList<>();

    public static List<BaseItem> getInitialItems() {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();

        /*
         * ITEM = TANPA CHILD
         * GROUPITEM = DENGAN CHILD
         * */
        rootMenu.add(new GroupItem("My Account Detail", R.drawable.ic_menu_gallery));
        rootMenu.add(new Item("Portfolio", R.drawable.ic_menu_gallery));
        rootMenu.add(new GroupItem("Mutual Funds", R.drawable.ic_menu_gallery));
        rootMenu.add(new GroupItem("Fixed Deposit", R.drawable.ic_menu_gallery));
        rootMenu.add(new GroupItem("NPS", R.drawable.ic_menu_manage));
        rootMenu.add(new GroupItem("IPO/FPO/BUYPACK", R.drawable.ic_menu_gallery));
        rootMenu.add(new Item("Insurance", R.drawable.ic_menu_send));
        rootMenu.add(new GroupItem("Equity", R.drawable.ic_menu_slideshow));

        return rootMenu;
    }

    public static List<BaseItem> getSubItems(BaseItem baseItem) {

        List<BaseItem> result = new ArrayList<>();
        int level = ((GroupItem) baseItem).getLevel() + 1;
        String menuItem = baseItem.getName();

        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }

        /*
         * HANYA UNTUK GROUP-ITEM
         * */
        switch (level) {
            case LEVEL_1:
                switch (menuItem) {
                    case "My Account Detail":
                        result = getListAccountDetail();
                        break;
                    case "Mutual Funds":
                        result = getListMurualFunds();
                        break;
                    case "Fixed Deposit":
                        result = getListFixedDeposit();
                        break;
                    case "NPS":
                        result = getListNps();
                        break;
                    case "IPO/FPO/BUYPACK":
                        result = getListBuypack();
                        break;
                    case "Equity":
                        result = getListEquity();
                        break;
                }
                break;

            case LEVEL_2:
                switch (menuItem) {
                    case "Invest":
                        result = getListInvest();
                        break;
                    case "Systematic Plan":
                        result = getListSystematic();
                        break;
                }
                break;
        }

        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }

    private static List<BaseItem> getListAccountDetail() {

        List<BaseItem> list = new ArrayList<>();

        list.add(new Item("Investor Profile", 1));
        list.add(new Item("Download Form", 1));

        return list;
    }

    private static List<BaseItem> getListMurualFunds() {

        List<BaseItem> list = new ArrayList<>();

        // Setiap membuat groupItem harus di set levelnya
        GroupItem InvestItem = new GroupItem("Invest", R.drawable.ic_menu_gallery);
        InvestItem.setLevel(InvestItem.getLevel() + 1);

        GroupItem SystematicItem = new GroupItem("Systematic Plan", R.drawable.ic_menu_gallery);
        SystematicItem.setLevel(SystematicItem.getLevel() + 1);

        list.add(new Item("Home", R.drawable.ic_menu_gallery));
        list.add(InvestItem);
        list.add(SystematicItem);
        list.add(new Item("Transfer Funds", R.drawable.ic_menu_gallery));
        list.add(new Item("Redeem", R.drawable.ic_menu_gallery));
        list.add(new Item("My MF Holding", R.drawable.ic_menu_gallery));

        return list;
    }

    private static List<BaseItem> getListFixedDeposit() {

        List<BaseItem> list = new ArrayList<>();

        list.add(new Item("New Fixed Deposit", R.drawable.ic_menu_gallery));
        list.add(new Item("Your Fixed Deposit", R.drawable.ic_menu_gallery));
        list.add(new Item("FAQ", R.drawable.ic_menu_gallery));

        return list;
    }

    private static List<BaseItem> getListNps() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Transact", R.drawable.ic_menu_gallery));
        list.add(new Item("Holdings & Service", R.drawable.ic_menu_gallery));
        return list;
    }

    private static List<BaseItem> getListBuypack() {

        List<BaseItem> list = new ArrayList<>();

        list.add(new Item("Transact", R.drawable.ic_menu_gallery));

        return list;
    }

    private static List<BaseItem> getListEquity() {

        List<BaseItem> list = new ArrayList<>();

        list.add(new Item("Transact", R.drawable.ic_menu_gallery));
        list.add(new Item("Holdings & Service", R.drawable.ic_menu_gallery));

        return list;
    }

    private static List<BaseItem> getListInvest() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("New Investment", R.drawable.ic_menu_gallery));
        list.add(new Item("Additional Investment", R.drawable.ic_menu_gallery));
        return list;
    }

    private static List<BaseItem> getListSystematic() {
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Regular SIP", R.drawable.ic_menu_gallery));
        list.add(new Item("Flexi SIP", R.drawable.ic_menu_gallery));
        list.add(new Item("Step Up SIP", R.drawable.ic_menu_gallery));
        list.add(new Item("Alert SIP", R.drawable.ic_menu_gallery));
        list.add(new Item("Sip With Insurance", R.drawable.ic_menu_gallery));
        list.add(new Item("Systematic Transfer Plan", R.drawable.ic_menu_gallery));
        list.add(new Item("Systematic Withdrawal Plan", R.drawable.ic_menu_gallery));
        return list;
    }

}
