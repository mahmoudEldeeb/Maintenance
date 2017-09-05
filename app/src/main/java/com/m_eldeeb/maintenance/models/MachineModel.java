package com.m_eldeeb.maintenance.models;

import java.io.Serializable;

/**
 * Created by melde on 7/23/2017.
 */

public class MachineModel implements Serializable {
    public  String MacineRowId;
    public String userId;
    public String catId;
    public String modelId;
    public String workTime;
    public String catName;
    public String modelName;

    public String getMacineRowId() {
        return MacineRowId;
    }

    public void setMacineRowId(String macineRowId) {
        MacineRowId = macineRowId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
