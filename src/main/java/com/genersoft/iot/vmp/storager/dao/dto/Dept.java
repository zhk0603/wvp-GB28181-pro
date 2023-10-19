package com.genersoft.iot.vmp.storager.dao.dto;

import java.time.LocalDateTime;

public class Dept {

    private int id;
    private String deptUid;
    private String deptName;
    private String deptPid;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeptUid() {
        return deptUid;
    }

    public void setDeptUid(String deptUid) {
        this.deptUid = deptUid;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptPid() {
        return deptPid;
    }

    public void setDeptPid(String deptPid) {
        this.deptPid = deptPid;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
