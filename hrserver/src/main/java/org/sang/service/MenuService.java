package org.sang.service;

import org.sang.bean.Menu;
import org.sang.common.HrUtils;
import org.sang.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "menus_cache")
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

//    @Cacheable(key = "#root.methodName")
    public List<Menu> getAllMenu(){
        return menuMapper.getAllMenu();
    }

    public List<Menu> getMenusByHrId() {
        return menuMapper.getMenusByHrId(HrUtils.getCurrentHr().getId());
    }

    public List<Menu> getMenusByHrIdTree() {
        return menuMapper.getMenusByHrIdTree(HrUtils.getCurrentHr().getId());
    }


    public List<Menu> menuTree() {
        return menuMapper.menuTree();
    }

    public List<Long> getMenusByRid(Long rid) {
        List<Menu> allMenusInRole= menuMapper.getMenusByRid(rid);
        List<Long> leafMenus=new LinkedList<>();
        for(Integer i=0;i<allMenusInRole.size();i++)
        {
            boolean isParent=false;
            Long id=allMenusInRole.get(i).getId();
            for(Integer j=0;j<allMenusInRole.size();j++)
            {
                if(allMenusInRole.get(j).getParentId()==id)
                {
                    isParent=true;
                    break;
                }
            }
            if(!isParent)
                leafMenus.add(id);
        }
        return leafMenus;
    }
}
