package org.sang.service;

import org.sang.bean.Menu;
import org.sang.bean.MenuMeta;
import org.sang.common.HrUtils;
import org.sang.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    public List<Menu> getMenuTreeByHrId() {
        List<Menu> allMenus= menuMapper.getMenuTreeByHrId(HrUtils.getCurrentHr().getId());
        List<Menu> list=buildGroupTree(allMenus);
        return list;
    }

    public List<Menu> buildGroupTree(List<Menu> allmenus) {
        List<Menu> list = new ArrayList<Menu>();
        for (Menu menu : allmenus){
            if(menu.getParentId()==null) {//根级目录
                Menu child = new Menu();
                child.setId(menu.getId());
                child.setName(menu.getName());
                child.setEnabled(menu.isEnabled());
                child.setIconCls(menu.getIconCls());
                child.setParentId(menu.getParentId());
                child.setPath(menu.getPath());
                child.setUrl(menu.getUrl());
                child.setComponent(menu.getComponent());
                MenuMeta menuMeta=new MenuMeta();
                menuMeta.setKeepAlive(menu.getMeta().isKeepAlive());
                menuMeta.setRequireAuth(menu.getMeta().isRequireAuth());
                child.setMeta(menuMeta);
                child.setChildren(treeChild(menu.getId(),allmenus));
                child.setRoles(menu.getRoles());
                list.add(child);
            }
        }
        return list;
    }
    public List<Menu> treeChild(Long parentId,List<Menu> allmenus){
        List<Menu> list = new ArrayList<Menu>();
        for(Menu menu : allmenus){
            Menu child = new Menu();
            if(parentId.equals(menu.getParentId())){
                child.setId(menu.getId());
                child.setName(menu.getName());
                child.setEnabled(menu.isEnabled());
                child.setIconCls(menu.getIconCls());
                child.setParentId(menu.getParentId());
                child.setPath(menu.getPath());
                child.setUrl(menu.getUrl());
                child.setComponent(menu.getComponent());
                MenuMeta menuMeta=new MenuMeta();
                menuMeta.setKeepAlive(menu.getMeta().isKeepAlive());
                menuMeta.setRequireAuth(menu.getMeta().isRequireAuth());
                child.setMeta(menuMeta);
                child.setChildren(treeChild(menu.getId(),allmenus));//递归循环
                child.setRoles(menu.getRoles());
                list.add(child);
            }
        }
        return list;
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

    public int AddMenu(Menu menu)
    {
        return menuMapper.addMenu(menu);
    }
    public  int updateMenu(Menu menu)
    {
        return menuMapper.updateMenu(menu);
    }
    public  int deleteMenu(Long id)
    {
        return menuMapper.deleteMenu(id);
    }
}
