package org.sang.mapper;

import org.apache.ibatis.annotations.Param;
import org.sang.bean.Role;

import java.util.List;

/**
 * Created by sang on 2018/1/1.
 */
public interface RoleMapper {
    List<Role> getRolesByHrid(Long hrid);

    int addNewRole(@Param("role") String role, @Param("roleZh") String roleZh);

    int deleteRoleById(Long rid);
}
