package com.averton.Iplayer.entity;

import java.io.File;
import java.nio.file.Files;

public class Analytics {
    public String MediaType;
    public String MediaName;
    public int clickCount;

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }

    public String getMediaName() {
        return MediaName;
    }

    public void setMediaName(String mediaName) {
        MediaName = mediaName;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String writeAnalytics()
    {

        return  "Media type: " + getMediaType() + "/n" +
                "Media Name: " + getMediaName() + "/n" +
                "No of clicks: " + getClickCount() + "/n/n";
    }

    public Analytics(String mediaType, String mediaName, int clickCount) {
        MediaType = mediaType;
        MediaName = mediaName;
        this.clickCount = clickCount;
    }
}
