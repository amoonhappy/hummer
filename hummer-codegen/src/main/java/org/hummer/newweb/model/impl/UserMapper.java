package org.hummer.newweb.model.impl;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);
}