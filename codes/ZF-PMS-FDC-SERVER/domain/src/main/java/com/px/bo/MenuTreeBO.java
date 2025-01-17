package com.px.bo;

import lombok.Data;

import java.util.List;

@Data
public class MenuTreeBO implements Tree<Long,String>{

    /**
     * id
     */
    private Long id;

    private String menuNo;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 类型 菜单/按钮
     */
    private String menuType;
    /**
     * 名称
     */
    private String name;

    private List<MenuTreeBO> children;

    private int orderNum;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getParentId() {
        return parentId;
    }

    @Override
    public int order() {
        return orderNum;
    }

    @Override
    public List<? extends Tree<Long, String>> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<? extends Tree<Long, String>> children) {
        this.children = (List<MenuTreeBO>) children;
    }


    @Override
    public String getName() {
        return name;
    }

}
