package com.application.arenda.entities.recyclerView;

import androidx.recyclerview.widget.RecyclerView;

public class RVOnFlingListener extends RecyclerView.OnFlingListener {
    private RecyclerView recyclerView;

    private int maxFling = 10000;
    private float maxSpeed = 0.7f;

    public RVOnFlingListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {

        if (velocityY > maxFling) {
            recyclerView.fling(velocityX, velocityY *= maxSpeed);

            return true;

        } else if (velocityY < -maxFling) {
            recyclerView.fling(velocityX, velocityY *= maxSpeed);

            return true;
        }

        return false;
    }
}