package com.edge.fintrack.product_list;

public class Product_list {
    private String name;
    private String valus;
    private String discription;
    private String color;
    private int icon;

    public Product_list(String name, String valus, String discription, String color) {
        this.name = name;
        this.valus = valus;
        this.discription = discription;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValus() {
        return valus;
    }

    public void setValus(String valus) {
        this.valus = valus;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
