package org.hummer.newweb.model.impl;

import java.io.Serializable;

public class EmployeePO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long createTime;
    private Long updateTime;
    private String address;
    private String email;
    private String imageAddress;
    private String job;
    private String loginName;
    private String markName;
    private String phone;
    private String sex;
    private String signature;
    private String telephone;
    private String userId;
    private String userName;
    private String userSystem;
    private String userType;
    private String weixinId;
    private Long turnoverTime;
    private Long workingTime;
    private Long appId;
    private Long count;
    private Long firstLogin;
    private Integer deleteFlag;
    private Integer loginMaxCount;
    private Byte createType;
    private String ldapDn;
    private String phoneType;
    private String pinyinName;
    private String initial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress == null ? null : imageAddress.trim();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job == null ? null : job.trim();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName == null ? null : markName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserSystem() {
        return userSystem;
    }

    public void setUserSystem(String userSystem) {
        this.userSystem = userSystem == null ? null : userSystem.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId == null ? null : weixinId.trim();
    }

    public Long getTurnoverTime() {
        return turnoverTime;
    }

    public void setTurnoverTime(Long turnoverTime) {
        this.turnoverTime = turnoverTime;
    }

    public Long getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(Long workingTime) {
        this.workingTime = workingTime;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Long firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Integer getLoginMaxCount() {
        return loginMaxCount;
    }

    public void setLoginMaxCount(Integer loginMaxCount) {
        this.loginMaxCount = loginMaxCount;
    }

    public Byte getCreateType() {
        return createType;
    }

    public void setCreateType(Byte createType) {
        this.createType = createType;
    }

    public String getLdapDn() {
        return ldapDn;
    }

    public void setLdapDn(String ldapDn) {
        this.ldapDn = ldapDn == null ? null : ldapDn.trim();
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType == null ? null : phoneType.trim();
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName == null ? null : pinyinName.trim();
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial == null ? null : initial.trim();
    }
}