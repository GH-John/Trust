package com.application.arenda.entities.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelMessage;
import com.application.arenda.entities.models.ModelMessage.Type;
import com.application.arenda.entities.recyclerView.BaseViewHolder;

import org.threeten.bp.LocalTime;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageVH extends BaseViewHolder {

    @BindView(R.id.messageFromUser)
    TextView messageFrom;

    @BindView(R.id.messageToUser)
    TextView messageTo;

    @BindView(R.id.timeToUser)
    TextView timeTo;

    @BindView(R.id.timeFromUser)
    TextView timeFrom;

    private ModelMessage message;
    private int type;

    private MessageVH(@NonNull View itemView, int viewType) {
        super(itemView);
        type = viewType;
        ButterKnife.bind(this, itemView);
    }

    public static MessageVH create(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(isMine(viewType) ? R.layout.vh_message_from_user : R.layout.vh_message_to_user, parent, false);
        return new MessageVH(view, viewType);
    }

    private static boolean isMine(int type) {
        return Type.get(type) == Type.CHAT_MINE;
    }

    @Override
    public int getResourceLayoutId() {
        return isMine(type) ? R.layout.vh_message_from_user : R.layout.vh_message_to_user;
    }

    @Override
    public void onBind(IModel model, int position) {
        message = (ModelMessage) model;

        if (isMine(type)) {
            messageFrom.setText(message.getMessage());
            timeFrom.setText(LocalTime.now().toString());
        } else {
            messageTo.setText(message.getMessage());
            timeTo.setText(LocalTime.now().toString());
        }
    }
}