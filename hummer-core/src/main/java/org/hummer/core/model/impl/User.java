package org.hummer.core.model.impl;

import org.hummer.core.model.intf.IModel;

public class User implements IModel {
    String id;

    public User(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
