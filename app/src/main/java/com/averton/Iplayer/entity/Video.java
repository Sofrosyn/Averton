package com.averton.Iplayer.entity;

public class Video {
    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoGenre() {
        return videoGenre;
    }

    public void setVideoGenre(String videoGenre) {
        this.videoGenre = videoGenre;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    private String videoName;
    private String videoGenre;
    private String videoPath;

    public String getVideoThumbNail() {
        return videoThumbNail;
    }

    public void setVideoThumbNail(String videoThumbNail) {
        this.videoThumbNail = videoThumbNail;
    }

    private String videoThumbNail;

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    private String videoDuration;

    public Video(String videoName, String videoGenre, String videoPath) {
        this.videoName = videoName;
        this.videoGenre = videoGenre;
        this.videoPath = videoPath;
    }

    public Video(){}


}
