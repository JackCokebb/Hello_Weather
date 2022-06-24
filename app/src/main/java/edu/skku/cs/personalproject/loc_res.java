package edu.skku.cs.personalproject;

public class loc_res {
    public plus_code plus_code;
    public result[] results;
    public String status;

    public edu.skku.cs.personalproject.plus_code getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(edu.skku.cs.personalproject.plus_code plus_code) {
        this.plus_code = plus_code;
    }

    public result[] getResults() {
        return results;
    }

    public void setResults(result[] results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
class result{
    public address_component[] address_components;
    public String formatted_address;
    public geometry geometry;
    public String placed_id;
    public plus_code plus_code;
    public String[] types;

    public address_component[] getAddress_components() {
        return address_components;
    }

    public void setAddress_components(address_component[] address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public edu.skku.cs.personalproject.geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(edu.skku.cs.personalproject.geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlaced_id() {
        return placed_id;
    }

    public void setPlaced_id(String placed_id) {
        this.placed_id = placed_id;
    }

    public edu.skku.cs.personalproject.plus_code getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(edu.skku.cs.personalproject.plus_code plus_code) {
        this.plus_code = plus_code;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
class address_component{
    public String long_name;
    public String short_name;
    public String[] types;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
}
class geometry{
    public bounds bounds;
    public location location;
    public String location_type;
    public viewport viewport;

    public edu.skku.cs.personalproject.bounds getBounds() {
        return bounds;
    }

    public void setBounds(edu.skku.cs.personalproject.bounds bounds) {
        this.bounds = bounds;
    }

    public edu.skku.cs.personalproject.location getLocation() {
        return location;
    }

    public void setLocation(edu.skku.cs.personalproject.location location) {
        this.location = location;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public edu.skku.cs.personalproject.viewport getViewport() {
        return viewport;
    }

    public void setViewport(edu.skku.cs.personalproject.viewport viewport) {
        this.viewport = viewport;
    }
}

class viewport{
    public northeast northeast;
    public southwest southwest;

    public edu.skku.cs.personalproject.northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(edu.skku.cs.personalproject.northeast northeast) {
        this.northeast = northeast;
    }

    public edu.skku.cs.personalproject.southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(edu.skku.cs.personalproject.southwest southwest) {
        this.southwest = southwest;
    }
}
class location{
    Double lat;
    Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
class northeast{
    Double lat;
    Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
class southwest{
    Double lat;
    Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
class plus_code{
    String compound_code;
    String global_code;

    public String getCompound_code() {
        return compound_code;
    }

    public void setCompound_code(String compound_code) {
        this.compound_code = compound_code;
    }

    public String getGlobal_code() {
        return global_code;
    }

    public void setGlobal_code(String global_code) {
        this.global_code = global_code;
    }
}
class bounds{
    public northeast northeast;
    public southwest southwest;

    public edu.skku.cs.personalproject.northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(edu.skku.cs.personalproject.northeast northeast) {
        this.northeast = northeast;
    }

    public edu.skku.cs.personalproject.southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(edu.skku.cs.personalproject.southwest southwest) {
        this.southwest = southwest;
    }
}