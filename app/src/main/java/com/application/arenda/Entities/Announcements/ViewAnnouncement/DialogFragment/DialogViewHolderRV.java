package com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.Entities.Announcements.Models.IModel;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.RecyclerView.OnItemClick;
import com.application.arenda.R;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DialogViewHolderRV extends BaseViewHolder {

    @Nullable
    @BindView(R.id.titlePhoneNumber)
    TextView titlePhoneNumber;

    private ModelPhoneNumber modelPhoneNumber;

    public DialogViewHolderRV(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public static DialogViewHolderRV create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_dialog_call_phone_number, parent, false);
        return new DialogViewHolderRV(view);
    }

    public void setOnItemClick(OnItemClick itemClick) {
        if (itemClick != null && modelPhoneNumber != null) {
            itemView.setOnClickListener(v -> itemClick.onClick(this, modelPhoneNumber));
        }
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_dialog_call_phone_number;
    }

    @Override
    public void onBind(IModel model, int position) {
        if (model != null) {
            modelPhoneNumber = ((ModelPhoneNumber) model);
            titlePhoneNumber.setText(modelPhoneNumber.getNumber());
        }
    }
}