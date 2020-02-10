package com.application.arenda.Entities.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progressBarLoadImg)
    ProgressBar progressBar;

    public ProgressViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void onBind() {
        progressBar.setIndeterminate(true);
    }
}