package com.application.arenda.entities.recyclerView;

import com.application.arenda.entities.models.IModel;

import java.util.List;

public interface RVAdapter<M extends IModel> {
    boolean isLoading();

    void setLoading(boolean b);

    void addToCollection(List<M> collection);

    void addToCollection(M model);

    void addToCollection(M model, int position);

    void removeFromCollection(int position);

    void rewriteCollection(List<M> collection);

    M getItem(int position);

    M getLastItem();
}