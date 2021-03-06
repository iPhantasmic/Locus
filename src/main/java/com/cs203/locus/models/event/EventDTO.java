package com.cs203.locus.models.event;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


public class EventDTO implements Serializable {

    private int id;

    private int organiserId;

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    private String inviteCode;

    private boolean isPrivate;

    private String tag;

    @NotBlank
    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private String startDateTime;

    @NotBlank
    private String endDateTime;

    private String imageGcsUrl;

    private double lat;

    private double lng;

    private int maxParticipants;

    public int getId() {return id;}

    public void setId(int id) { this.id = id; }

    public Integer getOrganiserId() { return organiserId; }

    public void setOrganiserId(Integer organiserId) { this.organiserId = organiserId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getInviteCode() { return inviteCode; }

    public void setInviteCode(String inviteCode) { this.inviteCode = inviteCode; }

    public boolean isPrivate() { return isPrivate; }

    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }

    public String getTag() { return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getStartDateTime() { return startDateTime; }

    public void setStartDateTime(String startDateTime) { this.startDateTime = startDateTime; }

    public String getEndDateTime() { return endDateTime; }

    public void setEndDateTime(String endDateTime) { this.endDateTime = endDateTime; }

    public String getImageGcsUrl() { return imageGcsUrl; }

    public void setImageGcsUrl(String imageGcsUrl) { this.imageGcsUrl = imageGcsUrl; }

    public double getLat() { return lat; }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() { return lng; }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getMaxParticipants() { return maxParticipants; }

    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants;}

    @Override
    public String toString() {
        return "EventDTO{" +
                "id=" + id +
                ", organiserId=" + organiserId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", isPrivate=" + isPrivate +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", startDateTime='" + startDateTime + '\'' +
                ", endDateTime='" + endDateTime + '\'' +
                ", imageGcsUrl='" + imageGcsUrl + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
}
