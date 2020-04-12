package com.application.arenda.Entities.Models;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Map;

public class Category extends BackendlessTable {
    private String name = "";
    private String iconUri = "";

    public static Category convertFromMap(@NonNull Context context, @NonNull Map map) {
        if (map == null)
            return null;

        Category category = new Category();
        category.setObjectId(String.valueOf(map.get("objectId")));
        category.setCreated((Date) map.get("created"));
        category.setUpdated((Date) map.get("updated"));
        category.setName(String.valueOf(map.get("name")));
        category.setIconUri(String.valueOf(map.get("iconUri")));

        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String uri) {
        this.iconUri = uri;
    }
}