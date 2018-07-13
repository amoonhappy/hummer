package org.hummer.core.model.impl;

import org.hummer.core.model.intf.IModel;

public class User implements IModel {
    Long id;

    public User(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
