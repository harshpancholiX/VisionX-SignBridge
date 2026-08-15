package com.harsh.visionx_signbridge;

public class OnboardingItem {
    private String title;
    private String description;
    private int lottieRawRes;

    public OnboardingItem(String title, String description, int lottieRawRes) {
        this.title = title;
        this.description = description;
        this.lottieRawRes = lottieRawRes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getLottieRawRes() {
        return lottieRawRes;
    }
}
