package com.application.arenda.CustomComponents.DropDownList;

import java.util.Collection;

public interface AdapterDropDownList {
    void setDropDownList(IDropDownList dropDownList);

    void clearRecyclerView();

    void refreshCollection(Collection collection);

    Collection getCollection();
}