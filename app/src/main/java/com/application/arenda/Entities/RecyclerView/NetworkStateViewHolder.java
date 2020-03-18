package com.application.arenda.Entities.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Utils.Network.NetworkState;
import com.application.arenda.Entities.Utils.Network.Status;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkStateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.errorMessage)
    TextView errorMessageTextView;

    @BindView(R.id.btnRetryLoading)
    Button retryLoadingButton;

    @BindView(R.id.progressBar)
    ProgressBar loadingProgressBar;

    private NetworkStateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static NetworkStateViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_network_state, parent, false);
        return new NetworkStateViewHolder(view);
    }

    public void setOnClickRetryLoading(View.OnClickListener listener){
        retryLoadingButton.setOnClickListener(listener);
    }

    public void onBind(NetworkState networkState) {
        //error message
        errorMessageTextView.setVisibility(networkState.getMessage() != null ? View.VISIBLE : View.GONE);
        if (networkState.getMessage() != null) {
            errorMessageTextView.setText(networkState.getMessage());
        }

        //loading and retry
        retryLoadingButton.setVisibility(networkState.getStatus() == Status.FAILED ? View.VISIBLE : View.GONE);
        loadingProgressBar.setVisibility(networkState.getStatus() == Status.RUNNING ? View.VISIBLE : View.GONE);
    }

}