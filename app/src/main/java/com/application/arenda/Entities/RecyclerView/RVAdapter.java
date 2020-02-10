package com.application.arenda.Entities.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;

import java.util.List;

public interface RVAdapter {
    boolean isLoading();

    void setLoading(boolean b);

    void addToCollection(List<ModelAllAnnouncement> collection);

    void addToCollection(ModelAllAnnouncement model);

    void removeFromCollection(int position);

    void rewriteCollection(List<ModelAllAnnouncement> collection);

    ModelAllAnnouncement getItem(int position);

    ModelAllAnnouncement getLastItem();
}