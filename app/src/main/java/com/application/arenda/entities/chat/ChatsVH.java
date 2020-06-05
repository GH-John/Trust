package com.application.arenda.entities.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelChat;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;
import com.application.arenda.entities.utils.glide.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

class ChatsVH extends BaseViewHolder {

    @BindView(R.id.userChatAvatar)
    ImageView userChatAvatar;

    @BindView(R.id.userChatLogin)
    TextView userChatLogin;

    private ModelChat model;
    private int position;

    public ChatsVH(@NonNull View itemView, int viewType) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static ChatsVH create(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_user_chat, parent, false);
        return new ChatsVH(view, viewType);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_user_chat;
    }

    @Override
    public void onBind(IModel model, int position) {
        if (model == null)
            return;

        this.position = position;

        this.model = (ModelChat) model;

        bind(this.model);
    }

    private void bind(ModelChat model) {
        GlideUtils.loadAvatar(itemView.getContext(), model.getAvatar(), userChatAvatar);
        userChatLogin.setText(model.getLogin());
    }

    public void setOnItemViewClick(OnItemClick itemClick) {
        if (model != null && itemClick != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(this, model, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }
}
