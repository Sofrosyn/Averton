package com.virmana.Iplayer.entity;

public class Music {
    private String artistName;
    private String artistSong;
    private String artistAlbum;
    private String artistPath;

    private String artistThumbnail;
    public String getArtistPath() {
        return artistPath;
    }

    public void setArtistPath(String artistPath) {
        this.artistPath = artistPath;
    }



    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistSong() {
        return artistSong;
    }

    public void setArtistSong(String artistSong) {
        this.artistSong = artistSong;
    }

    public String getArtistAlbum() {
        return artistAlbum;
    }

    public void setArtistAlbum(String artistAlbum) {
        this.artistAlbum = artistAlbum;
    }

    public String getArtistThumbnail() {
        return artistThumbnail;
    }

    public void setArtistThumbnail(String artistThumbnail) {
        this.artistThumbnail = artistThumbnail;
    }
    public Music(){}

    public Music(String artistName, String artistSong, String artistAlbum, String artistThumbnail) {
        this.artistName = artistName;
        this.artistSong = artistSong;
        this.artistAlbum = artistAlbum;
        this.artistThumbnail = artistThumbnail;
    }
    public Music(String artistName, String artistSong){
        this.artistName = artistName;
        this.artistSong = artistSong;

    }
}
