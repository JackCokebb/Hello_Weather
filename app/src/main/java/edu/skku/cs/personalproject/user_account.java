package edu.skku.cs.personalproject;

import java.util.ArrayList;

public class user_account {
    private String name;
    private String passwd;
    private ArrayList<loc> recent_locs;
    private loc recent_loc;

    public loc getRecent_loc() {
        return recent_loc;
    }

    public void setRecent_loc(loc recent_loc) {
        this.recent_loc = recent_loc;
    }

    public ArrayList<loc> getRecent_locs() {
        return recent_locs;
    }

    public void setRecent_locs(ArrayList<loc> recent_locs) {
        this.recent_locs = recent_locs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
class loc{
    public double lat;
    public double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}