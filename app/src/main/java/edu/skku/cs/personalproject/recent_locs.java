package edu.skku.cs.personalproject;

import java.util.ArrayList;

public class recent_locs {
    public ArrayList<recent_loc> recent_locs;

    public ArrayList<recent_loc> getRecent_locs() {
        return recent_locs;
    }

    public void setRecent_locs(ArrayList<recent_loc> recent_locs) {
        this.recent_locs = recent_locs;
    }
}
class recent_loc{
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