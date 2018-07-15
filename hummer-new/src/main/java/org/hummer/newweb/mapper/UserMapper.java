package org.hummer.newweb.mapper;

import org.apache.ibatis.annotations.Param;
import org.hummer.newweb.model.intf.IUser;

import java.util.List;

public interface UserMapper {
    List<IUser> selectActiveUsersByName(@Param("firstName") String firstName, @Param("role") String role);

    List<IUser> selectActiveUsersByName(IUser user);
}