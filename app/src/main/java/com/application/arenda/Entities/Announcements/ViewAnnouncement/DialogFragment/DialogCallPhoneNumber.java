package com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DialogCallPhoneNumber extends DialogFragment {
    public static String TAG = "CallPhoneNumber";
    @Nullable
    @BindView(R.id.callPhoneNumberRV)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private List<ModelPhoneNumber> collection = new ArrayList();

    public DialogCallPhoneNumber(List<ModelPhoneNumber> collection) {
        this.collection = collection;
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_call_phone_number, null);

        unbinder = ButterKnife.bind(this, view);

        DialogAdapterRV adapterRV = new DialogAdapterRV();
        adapterRV.addToCollection(collection);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        recyclerView.setAdapter(adapterRV);

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
