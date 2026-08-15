package com.harsh.visionx_signbridge;

public class FavoriteModel {

    private int id;
    private String phrase;
    private String translation;
    private String image;
    private String gif;
    private String audio;

    public FavoriteModel(
            int id,
            String phrase,
            String translation,
            String image,
            String gif,
            String audio) {

        this.id = id;
        this.phrase = phrase;
        this.translation = translation;
        this.image = image;
        this.gif = gif;
        this.audio = audio;
    }

    public int getId() {
        return id;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getTranslation() {
        return translation;
    }

    public String getImage() {
        return image;
    }

    public String getGif() {
        return gif;
    }

    public String getAudio() {
        return audio;
    }
}