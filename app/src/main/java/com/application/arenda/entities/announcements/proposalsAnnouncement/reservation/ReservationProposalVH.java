package com.application.arenda.entities.announcements.proposalsAnnouncement.reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.R;
import com.application.arenda.entities.models.IModel;
import com.application.arenda.entities.models.ModelProposal;
import com.application.arenda.entities.models.TypeProposalAnnouncement;
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

public class ReservationProposalVH extends BaseViewHolder {

    @BindView(R.id.itemImgProduct)
    ImageView itemImgProduct;

    @BindView(R.id.itemUserAvatar)
    ImageView itemUserAvatar;

    @BindView(R.id.itemTypeProposal)
    ImageView itemTypeProposal;

    @BindView(R.id.userLogin)
    TextView userLogin;

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

    @Nullable
    @BindView(R.id.btnSetActiveProposal)
    Button btnSetActiveProposal;

    @Nullable
    @BindView(R.id.btnRescheduleReservation)
    Button btnRescheduleReservation;

    @Nullable
    @BindView(R.id.btnCancleReservation)
    Button btnCancleReservation;

    @BindView(R.id.btnSendMessage)
    ImageButton btnSendMessage;

    private ModelProposal proposal;
    private int position;
    private TypeProposalAnnouncement type;

    public ReservationProposalVH(@NonNull View itemView, TypeProposalAnnouncement type) {
        super(itemView);

        this.type = type;

        ButterKnife.bind(this, itemView);
    }

    public static ReservationProposalVH create(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        final TypeProposalAnnouncement type = TypeProposalAnnouncement.get(viewType);
        View view = null;

        if (type == TypeProposalAnnouncement.INCOMING)
            view = layoutInflater.inflate(R.layout.vh_reservation_incoming_proposal, parent, false);
        else if (type == TypeProposalAnnouncement.OUTGOING)
            view = layoutInflater.inflate(R.layout.vh_reservation_outgoing_proposal, parent, false);

        return new ReservationProposalVH(view, type);
    }

    @Override
    public int getResourceLayoutId() {
        return type == TypeProposalAnnouncement.INCOMING ? R.layout.vh_reservation_incoming_proposal : R.layout.vh_reservation_outgoing_proposal;
    }

    @Override
    public void onBind(IModel model, int position) {
        if (model == null)
            return;

        proposal = (ModelProposal) model;
        this.position = position;


        if (type == TypeProposalAnnouncement.INCOMING) {
            bindIncoming(proposal);
            itemTypeProposal.setImageResource(R.drawable.indicator_incoming_proposal);
        } else if (type == TypeProposalAnnouncement.OUTGOING) {
            bindIncoming(proposal);
            itemTypeProposal.setImageResource(R.drawable.indicator_outgoing_proposal);
        }
    }

    private void bindIncoming(ModelProposal model) {
        loadImage(itemView.getContext(), model.getPicture(), itemImgProduct);
        loadAvatar(itemView.getContext(), model.getUserAvatar(), itemUserAvatar);

        userLogin.setText(model.getUserLogin());

        dateRentalStart.setText(getFormatingDate(itemView.getContext(), model.getRentalStart(), dd_MM_yy));
        dateRentalEnd.setText(getFormatingDate(itemView.getContext(), model.getRentalEnd(), dd_MM_yy));


        timeRentalStart.setText(convertTimeTo_24H_or_12h(model.getRentalStart().toLocalTime(), is24HourFormat(itemView.getContext())));
        timeRentalEnd.setText(convertTimeTo_24H_or_12h(model.getRentalEnd().toLocalTime(), is24HourFormat(itemView.getContext())));

        proposalCreated.setText(getFormatingDate(itemView.getContext(), model.getCreated()));
    }

    public void setItemViewListener(OnItemClick itemClick) {
        if (itemClick == null)
            return;

        itemImgProduct.setOnClickListener(v -> itemClick.onClick(this, proposal, position));
    }

    public void setBtnStartListener(OnItemClick itemClick) {
        if (itemClick == null || btnSetActiveProposal == null)
            return;

        btnSetActiveProposal.setOnClickListener(v -> itemClick.onClick(this, proposal, position));
    }

    public void setBtnRescheduleReservation(OnItemClick itemClick) {
        if (itemClick == null || btnRescheduleReservation == null)
            return;

        btnRescheduleReservation.setOnClickListener(v -> itemClick.onClick(this, proposal, position));
    }

    public void setBtnCancleReservation(OnItemClick itemClick) {
        if (itemClick == null || btnCancleReservation == null)
            return;

        btnCancleReservation.setOnClickListener(v -> itemClick.onClick(this, proposal, position));
    }

    public void setBtnSendMessageListener(OnItemClick itemClick) {
        if (itemClick == null)
            return;

        btnSendMessage.setOnClickListener(v -> itemClick.onClick(this, proposal, position));
    }

    public void setItemUserAvatarListener(OnItemClick itemClick) {
        if (itemClick == null)
            return;

        itemUserAvatar.setOnClickListener(v -> itemClick.onClick(this, proposal, position));
    }
}