package com.application.arenda.Entities.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.IModel;
import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;

import java.util.List;

public interface RVAdapter<M extends IModel> {
    boolean isLoading();

    void setLoading(boolean b);

    void addToCollection(List<M> collection);

    void addToCollection(M model);

    void removeFromCollection(int position);

    void rewriteCollection(List<M> collection);

    M getItem(int position);

    M getLastItem();
}