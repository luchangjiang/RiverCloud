package com.cloud.river.upms.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: RiverCloud
 * @description: tree node
 * @author: River
 * @create: 2019-03-26 11:38
 **/
@Data
public class TreeNode {
    protected int id;
    protected int parentId;
    protected List<TreeNode> children= new ArrayList<TreeNode>();

    public void add(TreeNode node){ children.add(node); }
}
