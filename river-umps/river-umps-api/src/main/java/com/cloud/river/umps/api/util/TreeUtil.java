package com.cloud.river.umps.api.util;

import cn.hutool.core.bean.BeanUtil;
import com.cloud.river.umps.api.dto.MenuTree;
import com.cloud.river.umps.api.dto.TreeNode;
import com.cloud.river.umps.api.entity.SysMenu;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RiverCloud
 * @description:  工具类 菜单转换为树
 * @author: River
 * @create: 2019-03-30 15:54
 **/
@UtilityClass
public class TreeUtil {
    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public <T extends TreeNode> List<T> build(List<T> treeNodes, Integer root){
        List<T> trees = new ArrayList<>();

        for(T treeNode : treeNodes){
            if(root.equals(treeNode.getParentId())){
                trees.add(treeNode);
            }

            for(T it : treeNodes){
                if(treeNode.getId()==it.getParentId()){
                    if(treeNode.getChildren() == null){
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Integer root){
        List<T> trees = new ArrayList<>();
        for(T treeNode : treeNodes){
            if(root.equals(treeNode.getParentId())){
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes){
        for(T it: treeNodes){
            if(it.getParentId() == treeNode.getId()){
                if(treeNode.getChildren() == null){
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * 通过sysMenu创建树形节点
     *
     * @param menus
     * @param root
     * @return
     */
    public List<MenuTree> buildTree(List<SysMenu> menus, Integer root){
        List<MenuTree> trees = new ArrayList<>();

        MenuTree node;
        for(SysMenu menu : menus){
            node = new MenuTree();
            BeanUtil.copyProperties(menu, node);
            node.setId(menu.getMenuId());
            node.setCode(menu.getPermission());
            node.setLabel(menu.getName());
            trees.add(node);
        }

        return TreeUtil.build(trees, root);
    }
}
