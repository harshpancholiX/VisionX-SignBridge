package com.harsh.visionx_signbridge.Model;

public class LanguageModel {

    private final int id;
    private final String name;
    private final String nativeName;
    private final String code;
    private final String description;
    private final String wikiUrl;
    private final int icon;
    private boolean selected;

    public LanguageModel(
            int id,
            String name,
            String nativeName,
            String code,
            String description,
            String wikiUrl,
            int icon
    ) {
        this.id = id;
        this.name = name;
        this.nativeName = nativeName;
        this.code = code;
        this.description = description;
        this.wikiUrl = wikiUrl;
        this.icon = icon;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public int getIcon() {
        return icon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}