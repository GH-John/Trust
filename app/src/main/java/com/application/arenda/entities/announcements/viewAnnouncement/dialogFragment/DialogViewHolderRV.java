package com.application.arenda.entities.announcements.viewAnnouncement.dialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelPhoneNumber;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogViewHolderRV extends BaseViewHolder {

    @Nullable
    @BindView(R.id.titlePhoneNumber)
    TextView titlePhoneNumber;

    private ModelPhoneNumber modelPhoneNumber;
    private int position;

    public DialogViewHolderRV(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static DialogViewHolderRV create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_dialog_call_phone_number, parent, false);
        return new DialogViewHolderRV(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_dialog_call_phone_number;
    }

    @Override
    public void onBind(IModel model, int position) {
        if (model != null) {
            this.position = position;
            modelPhoneNumber = ((ModelPhoneNumber) model);
            titlePhoneNumber.setText(modelPhoneNumber.getNumber());
        }
    }

    public void setOnItemClick(OnItemClick itemClick) {
        if (itemClick != null && modelPhoneNumber != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(this, modelPhoneNumber, position));
        } else {
            throw new NullPointerException("Model not initialized");
        }
    }
}