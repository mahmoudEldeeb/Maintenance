package com.m_eldeeb.maintenance.models;

/**
 * Created by melde on 7/23/2017.
 */

public class PartModel {

    public String partId;
    public String userMacineId;
    public String partName;
    public String mainLivetime;
    public String crruintLivetime;

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getUserMacineId() {
        return userMacineId;
    }

    public void setUserMacineId(String userMacineId) {
        this.userMacineId = userMacineId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getMainLivetime() {
        return mainLivetime;
    }

    public void setMainLivetime(String mainLivetime) {
        this.mainLivetime = mainLivetime;
    }

    public String getCrruintLivetime() {
        return crruintLivetime;
    }

    public void setCrruintLivetime(String crruintLivetime) {
        this.crruintLivetime = crruintLivetime;
    }
}
