package com.edge.fintrack.dashboard;

public class Model {

    public static final int FUNCTION_TYPE = 0;
    public static final int NEWS_TYPE = 1;

    private int Layout_Type;
    private String Title;
    private String Description;
    private String ButtomText;
    private int Image;

    public Model(int Layout_Type, String Title, String Description, String ButtomText, int Image) {
        this.Layout_Type = Layout_Type;
        this.Title = Title;
        this.Description = Description;
        this.ButtomText = ButtomText;
        this.Image = Image;
    }

    public int getLayout_Type() {
        return Layout_Type;
    }

    public void setLayout_Type(int layout_Type) {
        Layout_Type = layout_Type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getButtomText() {
        return ButtomText;
    }

    public void setButtomText(String buttomText) {
        ButtomText = buttomText;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
