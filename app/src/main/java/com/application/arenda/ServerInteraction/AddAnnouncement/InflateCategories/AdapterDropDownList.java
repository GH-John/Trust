package com.application.arenda.ServerInteraction.AddAnnouncement.InflateCategories;

import com.application.arenda.CustomComponents.DropDownList.IDropDownList;

import java.util.Collection;

public interface AdapterDropDownList {
    void setDropDownList(IDropDownList dropDownList);

    void clearRecyclerView();

    void refreshCollection(Collection collection);

    Collection getCollection();
}