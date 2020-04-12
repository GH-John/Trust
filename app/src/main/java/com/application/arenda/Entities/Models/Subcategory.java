package com.application.arenda.Entities.Models;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Map;

public class Subcategory extends BackendlessTable {
    private String name = "";

    public static Subcategory convertFromMap(@NonNull Context context, @NonNull Map map) {
        if (map == null)
            return null;

        Subcategory subcategory = new Subcategory();
        subcategory.setObjectId(String.valueOf(map.get("objectId")));
        subcategory.setCreated((Date) map.get("created"));
        subcategory.setUpdated((Date) map.get("updated"));
        subcategory.setName(String.valueOf(map.get("name")));

        return subcategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}