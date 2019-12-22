package com.application.arenda.UI.DropDownList;

import java.util.Collection;

public interface AdapterDropDownList {
    void setDropDownList(IDropDownList dropDownList);

    void clearRecyclerView();

    void rewriteCollection(Collection collection);

    Collection getModels();
}