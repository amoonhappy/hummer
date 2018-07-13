package org.hummer.newweb.model.impl;

// Generated 2009-3-28 14:15:17 by Hibernate Tools 3.2.4.CR1

import org.hummer.newweb.model.intf.IUser;

import java.util.Date;

public class User implements IUser {

    private static final long serialVersionUID = 6422172386239411218L;

    private Long id;

    private String firstName;

    private String lastName;

    private String role;

    private String email;

    private Date crtDate;

    private String crtUserId;

    private Date mdfDate;

    private String mdfUserId;

    private String deletedYn;

    private String status;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String firstName, String lastName, String role, String email, Date crtDate,
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCrtDate() {
        return this.crtDate;
    }

    public void setCrtDate(Date crtDate) {
        this.crtDate = crtDate;
    }

    public String getCrtUserId() {
        return this.crtUserId;
    }

    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    public Date getMdfDate() {
        return this.mdfDate;
    }

    public void setMdfDate(Date mdfDate) {
        this.mdfDate = mdfDate;
    }

    public String getMdfUserId() {
        return this.mdfUserId;
    }

    public void setMdfUserId(String mdfUserId) {
        this.mdfUserId = mdfUserId;
    }

    public String getDeletedYn() {
        return this.deletedYn;
    }

    public void setDeletedYn(String deletedYn) {
        this.deletedYn = deletedYn;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
