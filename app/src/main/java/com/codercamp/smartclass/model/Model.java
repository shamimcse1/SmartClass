package com.codercamp.smartclass.model;

public class Model {
    String batchNo,roomNo,time;

    public Model() {
    }

    public Model(String batchNo, String roomNo, String time) {
        this.batchNo = batchNo;
        this.roomNo = roomNo;
        this.time = time;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getTime() {
        return time;
    }
}
