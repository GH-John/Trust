package com.application.arenda.Entities.Announcements.LoadingAnnouncements.AllAnnouncements;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Announcements.Models.ModelAllAnnouncement;
import com.application.arenda.Entities.RecyclerView.NetworkStateViewHolder;
import com.application.arenda.Entities.Utils.Network.NetworkState;
import com.application.arenda.R;

import java.util.Objects;

public class RVAdapter extends PagedListAdapter<ModelAllAnnouncement, RecyclerView.ViewHolder> {

    private static DiffUtil.ItemCallback<ModelAllAnnouncement> UserDiffCallback = new DiffUtil.ItemCallback<ModelAllAnnouncement>() {
        @Override
        public boolean areItemsTheSame(@NonNull ModelAllAnnouncement oldItem, @NonNull ModelAllAnnouncement newItem) {
            return oldItem.getIdAnnouncement() == newItem.getIdAnnouncement();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ModelAllAnnouncement oldItem, @NonNull ModelAllAnnouncement newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    private Context context;
    private NetworkState networkState;

    private AllAnnouncementsViewHolder.OnItemClick itemViewClick;
    private AllAnnouncementsViewHolder.OnItemClick itemHeartClick;
    private View.OnClickListener onClickListenerRetryLoading;

    public RVAdapter(Context context) {
        super(UserDiffCallback);

        this.context = context;
    }

    public void setItemViewClick(AllAnnouncementsViewHolder.OnItemClick itemClick) {
        this.itemViewClick = itemClick;
    }

    public void setItemHeartClick(AllAnnouncementsViewHolder.OnItemClick itemHeartClick) {
        this.itemHeartClick = itemHeartClick;
    }

    public void setOnClickListenerRetryLoading(View.OnClickListener onClickListenerRetryLoading) {
        this.onClickListenerRetryLoading = onClickListenerRetryLoading;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.vh_announcement:
                AllAnnouncementsViewHolder allAnnouncementsViewHolder = AllAnnouncementsViewHolder.create(parent);
                allAnnouncementsViewHolder.setOnItemViewClick(itemViewClick);
                allAnnouncementsViewHolder.setOnItemHeartClick(itemHeartClick);

                return allAnnouncementsViewHolder;

            case R.layout.vh_network_state:
                NetworkStateViewHolder networkStateViewHolder = NetworkStateViewHolder.create(parent);
                networkStateViewHolder.setOnClickRetryLoading(onClickListenerRetryLoading);

                return networkStateViewHolder;

            default:
                throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.vh_announcement:
                ((AllAnnouncementsViewHolder) holder).onBind(context, getItem(position), position);
                break;
            case R.layout.vh_network_state:
                ((NetworkStateViewHolder) holder).onBind(networkState);
                break;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.vh_network_state;
        } else {
            return R.layout.vh_announcement;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setNetworkState(NetworkState newNetworkState) {
        if (getCurrentList() != null) {
            if (getCurrentList().size() != 0) {
                NetworkState previousState = this.networkState;
                boolean hadExtraRow = hasExtraRow();

                this.networkState = newNetworkState;

                boolean hasExtraRow = hasExtraRow();
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount());
                    } else {
                        notifyItemInserted(super.getItemCount());
                    }
                } else if (hasExtraRow && previousState != newNetworkState) {
                    notifyItemChanged(getItemCount() - 1);
                }
            }
        }
    }

}