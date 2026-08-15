package com.harsh.visionx_signbridge;

public class SignModel {

    private String signName;
    private String hindiName;
    private String category;
    private int imageResource;

    public SignModel(String signName, String hindiName, String category, int imageResource) {
        this.signName = signName;
        this.hindiName = hindiName;
        this.category = category;
        this.imageResource = imageResource;
    }

    public String getSignName() {
        return signName;
    }

    public String getHindiName() {
        return hindiName;
    }

    public String getCategory() {
        return category;
    }

    public int getImageResource() {
        return imageResource;
    }
}
