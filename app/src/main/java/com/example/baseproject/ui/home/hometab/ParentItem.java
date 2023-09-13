package com.example.baseproject.ui.home.hometab;

import java.util.List;

public class ParentItem {

    // Declaration of the variables
    private String itemTitle;
    private List<ChildItem> ChildItemList;

    // Constructor of the class
    // to initialize the variables
    public ParentItem(
            String ParentItemTitle,
            List<ChildItem> ChildItemList)
    {

        this.itemTitle = ParentItemTitle;
        this.ChildItemList = ChildItemList;
    }

    // Getter and Setter methods
    // for each parameter
    public String getParentItemTitle()
    {
        return itemTitle;
    }

    public void setParentItemTitle(
            String parentItemTitle)
    {
        itemTitle = parentItemTitle;
    }

    public List<ChildItem> getChildItemList()
    {
        return ChildItemList;
    }

    public void setChildItemList(
            List<ChildItem> childItemList)
    {
        ChildItemList = childItemList;
    }
}