package com.application.arenda.Entities.RecyclerView;

import com.application.arenda.Entities.Models.BackendlessTable;

import java.util.List;

public interface RVAdapter<M extends BackendlessTable> {
    boolean isLoading();

    void setLoading(boolean b);

    void addToCollection(List<M> collection);

    void addToCollection(M model);

    void removeFromCollection(int position);

    void rewriteCollection(List<M> collection);

    M getItem(int position);

    M getLastItem();
}