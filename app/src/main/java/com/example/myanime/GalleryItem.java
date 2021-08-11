package com.example.myanime;

public class GalleryItem {
    String url;
    boolean isVideo;

    public GalleryItem(String url, boolean isVideo) {
        this.url = url;
        this.isVideo = isVideo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
