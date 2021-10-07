package com.cs203.locus.models.participant;

import javax.validation.constraints.NotBlank;

public class ParticipantDTO {

    private static final long serialVersionUID = -8661467404585749884L;

    @NotBlank
    private Integer id;

    @NotBlank
    private boolean vaxStatus;

    private String vaxGcsUrl;

    public ParticipantDTO() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id;}

    public void setVaxStatus(boolean vaxStatus){
        this.vaxStatus = vaxStatus;
    }
    public boolean getVaxStatus(){
        return vaxStatus;
    }

    public String getVaxGcsUrl(){
        return vaxGcsUrl;
    }
    public void setVaxGcsUrl(String vaxGcsUrl) { this.vaxGcsUrl = vaxGcsUrl; }

}
