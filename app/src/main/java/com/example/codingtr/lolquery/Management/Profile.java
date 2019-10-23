package com.example.codingtr.lolquery.Management;

public class Profile {

    private String permaSpell1Url;
    private String permaSpell2Url;

    private long permaTimeStamp;
    private boolean permaWin;

    private String permaItem0Url;
    private String permaItem1Url;
    private String permaItem2Url;
    private String permaItem3Url;
    private String permaItem4Url;
    private String permaItem5Url;
    private String permaItem6Url;

    private String permaChampionPhotoUrl;

    private String permaLaneUrl;

    private int kills;
    private int deaths;
    private int assists;

    public Profile(String permaSpell1Url, String permaSpell2Url,
                   long permaTimeStamp,
                   boolean permaWin,
                   String permaLaneUrl,
                   int kills,
                   int deaths,
                   int assists,
                   String permaItem0Url,
                   String permaItem1Url,
                   String permaItem2Url,
                   String permaItem3Url,
                   String permaItem4Url,
                   String permaItem5Url,
                   String permaItem6Url,
                   String permaChampionPhotoUrl
                   ) {

        this.permaTimeStamp = permaTimeStamp;
        this.permaSpell1Url = permaSpell1Url;
        this.permaSpell2Url = permaSpell2Url;
        this.permaWin = permaWin;
        this.permaLaneUrl = permaLaneUrl;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.permaItem0Url = permaItem0Url;
        this.permaItem1Url = permaItem1Url;
        this.permaItem2Url = permaItem2Url;
        this.permaItem3Url = permaItem3Url;
        this.permaItem4Url = permaItem4Url;
        this.permaItem5Url = permaItem5Url;
        this.permaItem6Url = permaItem6Url;
        this.permaChampionPhotoUrl = permaChampionPhotoUrl;

    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getAssists() {
        return assists;
    }

    public String getPermaLaneUrl() {
        return permaLaneUrl;
    }

    public String getPermaSpell1Url() {
        return permaSpell1Url;
    }

    public String getPermaSpell2Url() {
        return permaSpell2Url;
    }

    public long getPermaTimeStamp() {
        return permaTimeStamp;
    }

    public boolean isPermaWin() {
        return permaWin;
    }

    public String getPermaItem0Url() {
        return permaItem0Url;
    }

    public String getPermaItem1Url() {
        return permaItem1Url;
    }

    public String getPermaItem2Url() {
        return permaItem2Url;
    }

    public String getPermaItem3Url() {
        return permaItem3Url;
    }

    public String getPermaItem4Url() {
        return permaItem4Url;
    }

    public String getPermaItem5Url() {
        return permaItem5Url;
    }

    public String getPermaItem6Url() {
        return permaItem6Url;
    }

    public String getPermaChampionPhotoUrl() {
        return permaChampionPhotoUrl;
    }
}
