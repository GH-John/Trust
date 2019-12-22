package com.application.arenda.Entities.Announcements.Models;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ViewModelLookAnnouncement {
    private String name = "";
    private String description = "";

    private int rating = 0;
    private int countRent = 0;
    private boolean statusRent = false;

    private int idUser = 0;
    private int idSubcategory = 0;
    private int idAnnouncement = 0;

    private Collection<URL> urlCollection;
    private Map<Uri, Bitmap> mapBitmap = new HashMap<>();

    private String location = "";

    private String phone_1 = "";
    private String phone_2 = "";
    private String phone_3 = "";

    private boolean visiblePhone_1 = false;
    private boolean visiblePhone_2 = false;
    private boolean visiblePhone_3 = false;
}