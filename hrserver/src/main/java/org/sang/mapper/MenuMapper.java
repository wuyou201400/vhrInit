package org.sang.mapper;

import org.sang.bean.Menu;

import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
public interface MenuMapper {
    List<Menu> getAllMenu();

    List<Menu> getMenuTreeByHrId(Long hrId);

    List<Menu> menuTree();

    List<Menu> getMenusByRid(Long rid);

    int addMenu(Menu menu);
    int updateMenu(Menu menu);
    int deleteMenu(Long id);
}
