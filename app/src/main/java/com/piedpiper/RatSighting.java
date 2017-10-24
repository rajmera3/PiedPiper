package com.piedpiper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pbokey on 10/9/17.
 */

public class RatSighting implements Serializable {

    private String uniqueKey;
    private String createdDate;
    private String locationType;
    private String incidentZip;
    private String incidentAddress;
    private String city;
    private String borough;
    private String latitude;
    private String longitude;

    public RatSighting() {
        this(null, null, null, null, null, null, null, null);
    }

    public RatSighting(String createdDate, String locationType, String incidentZip, String incidentAddress, String city, String borough, String latitude, String longitude) {
//        this.uniqueKey = uniqueKey;
        this.createdDate = createdDate;
        this.locationType = locationType;
        this.incidentZip = incidentZip;
        this.incidentAddress = incidentAddress;
        this.city = city;
        this.borough = borough;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Getter for unique key
     * @return unique key
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
     * Getter for created date
     * @return created data
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * Getter for location type
     * @return location type
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Getter for incident zip
     * @return incident zip
     */
    public String getIncidentZip() {
        return incidentZip;
    }

    /**
     * Getter for incident address
     * @return incident address
     */
    public String getIncidentAddress() {
        return incidentAddress;
    }

    /**
     * Getter for city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter for borough
     * @return borough
     */
    public String getBorough() {
        return borough;
    }

    /**
     * Getter for latitude
     * @return latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Getter for longitude
     * @return longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Setter for unique key
     * @param uniqueKey
     */
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    /**
     * Setter for created date
     * @param createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Setter for location type
     * @param locationType
     */
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * Setter for incident zip
     * @param incidentZip
     */
    public void setIncidentZip(String incidentZip) {
        this.incidentZip = incidentZip;
    }

    /**
     * Setter for incident address
     * @param incidentAddress
     */
    public void setIncidentAddress(String incidentAddress) {
        this.incidentAddress = incidentAddress;
    }

    /**
     * Setter for city
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Setter for borough
     * @param borough
     */
    public void setBorough(String borough) {
        this.borough = borough;
    }

    /**
     * Setter for latitude
     * @param latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Setter for longitude
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Get map to put in Firebase
     * @return map of information
     */
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("Borough", borough);
        map.put("City", city);
        map.put("Created Date", createdDate);
        map.put("Incident Address", incidentAddress);
        map.put("Incident Zip", incidentZip);
        map.put("Latitude", latitude);
        map.put("Location Type", locationType);
        map.put("Longitude", longitude);
        map.put("Unique Key", uniqueKey);
        return map;
    }
}
