package org.hummer.web.model.impl;

// Generated 2009-3-28 14:15:17 by Hibernate Tools 3.2.4.CR1

import org.acegisecurity.GrantedAuthority;
import org.hummer.web.model.intf.IUser;

import java.util.Date;

/**
 * @hibernate.class table="users"
 */
public class User implements IUser {

    /**
     *
     */
    private static final long serialVersionUID = 6422172386239411218L;
    /**
     * @hibernate.id generator-class="assigned" type="java.lang.String"
     * column="id"
     */
    private String id;
    /**
     * @hibernate.property column="firstName" length="60"
     */
    private String firstName;
    /**
     * @hibernate.property column="lastName" length="60"
     */
    private String lastName;
    /**
     * @hibernate.property column="role" length="20"
     */
    private String role;
    /**
     * @hibernate.property column="email" length="60"
     */
    private String email;
    /**
     * @hibernate.property column="crtdate" length="10"
     */
    private Date crtDate;
    /**
     * @hibernate.property column="crtuserid" length="32"
     */
    private String crtUserId;
    /**
     * @hibernate.property column="mdfdate" length="10"
     */
    private Date mdfDate;
    /**
     * @hibernate.property column="mdfuserid" length="32"
     */
    private String mdfUserId;
    /**
     * @hibernate.property column="deleteyn" length="1"
     */
    private String deletedYn;
    /**
     * @hibernate.property column="status" length="1"
     */
    private String status;

    public User() {
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String firstName, String lastName, String role, String email, Date crtDate,
                String crtUserId, Date mdfDate, String mdfUserId, String deletedYn, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.email = email;
        this.crtDate = crtDate;
        this.crtUserId = crtUserId;
        this.mdfDate = mdfDate;
        this.mdfUserId = mdfUserId;
        this.deletedYn = deletedYn;
        this.status = status;
    }

    /**
     * * @hibernate.id generator-class="assigned" type="java.lang.String"
     * column="id"
     */
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * * @hibernate.property column="firstName" length="60"
     */
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * * @hibernate.property column="lastName" length="60"
     */
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * * @hibernate.property column="role" length="20"
     */
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * * @hibernate.property column="email" length="60"
     */
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * * @hibernate.property column="crtdate" length="10"
     */
    public Date getCrtDate() {
        return this.crtDate;
    }

    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    /**
     * * @hibernate.property column="crtuserid" length="32"
     */
    public String getCrtUserId() {
        return this.crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * * @hibernate.property column="mdfdate" length="10"
     */
    public Date getMdfDate() {
        return this.mdfDate;
    }

    public void setMdfDate(Date mdfDate) {
        this.mdfDate = mdfDate;
    }

    /**
     * * @hibernate.property column="mdfuserid" length="32"
     */
    public String getMdfUserId() {
        return this.mdfUserId;
    }

    public void setMdfUserId(String mdfUserId) {
        this.mdfUserId = mdfUserId;
    }

    /**
     * * @hibernate.property column="deleteyn" length="1"
     */
    public String getDeletedYn() {
        return this.deletedYn;
    }

    public void setDeletedYn(String deletedYn) {
        this.deletedYn = deletedYn;
    }

    /**
     * * @hibernate.property column="status" length="10"
     */
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GrantedAuthority[] getAuthorities() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

}
