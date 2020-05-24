package com.application.arenda.entities.announcements.proposalsAnnouncement.outgoing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.recyclerView.BaseViewHolder;
import com.application.arenda.entities.recyclerView.OnItemClick;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.format.DateFormat.is24HourFormat;
import static com.application.arenda.entities.utils.Utils.DatePattern.dd_MM_yy;
import static com.application.arenda.entities.utils.Utils.convertTimeTo_24H_or_12h;
import static com.application.arenda.entities.utils.Utils.getFormatingDate;
import static com.application.arenda.entities.utils.glide.GlideUtils.loadAvatar;
import static com.application.arenda.entities.utils.glide.GlideUtils.loadImage;

public class OutProposalVH extends BaseViewHolder {

    @BindView(R.id.itemImgProduct)
    ImageView itemImgProduct;

    @BindView(R.id.itemUserAvatar)
    ImageView itemUserAvatar;

    @BindView(R.id.userLogin)
    TextView userLogin;

    @BindView(R.id.itemEditProposal)
    ImageButton itemEditProposal;

    @BindView(R.id.dateRentalStart)
    TextView dateRentalStart;

    @BindView(R.id.dateRentalEnd)
    TextView dateRentalEnd;

    @BindView(R.id.timeRentalStart)
    TextView timeRentalStart;

    @BindView(R.id.timeRentalEnd)
    TextView timeRentalEnd;

    @BindView(R.id.proposalCreated)
    TextView proposalCreated;

    @BindView(R.id.btnRejectProposal)
    Button btnRejectProposal;

    @BindView(R.id.btnSendMessage)
    ImageButton btnSendMessage;

    private ModelProposal proposal;

    public OutProposalVH(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public static OutProposalVH create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.vh_outgoing_proposal, parent, false);
        return new OutProposalVH(view);
    }

    @Override
    public int getResourceLayoutId() {
        return R.layout.vh_outgoing_proposal;
    }

    @Override
    public void onBind(IModel model, int position) {
        proposal = (ModelProposal) model;

        loadImage(itemView.getContext(), proposal.getPicture(), itemImgProduct);
        loadAvatar(itemView.getContext(), proposal.getUserAvatar(), itemUserAvatar);

        userLogin.setText(proposal.getUserLogin());

        dateRentalStart.setText(getFormatingDate(itemView.getContext(), proposal.getRentalStart(), dd_MM_yy));
        dateRentalEnd.setText(getFormatingDate(itemView.getContext(), proposal.getRentalEnd(), dd_MM_yy));


        timeRentalStart.setText(convertTimeTo_24H_or_12h(proposal.getRentalStart().toLocalTime(), is24HourFormat(itemView.getContext())));
        timeRentalEnd.setText(convertTimeTo_24H_or_12h(proposal.getRentalEnd().toLocalTime(), is24HourFormat(itemView.getContext())));

        proposalCreated.setText(getFormatingDate(itemView.getContext(), proposal.getCreated()));
    }

    public void setBtnRejectListener(OnItemClick itemClick) {
        if (itemClick == null)
            return;

        btnRejectProposal.setOnClickListener(v -> itemClick.onClick(this, proposal));
    }

    public void setBtnSendMessageListener(OnItemClick itemClick) {
        if (itemClick == null)
            return;

        btnSendMessage.setOnClickListener(v -> itemClick.onClick(this, proposal));
    }
}