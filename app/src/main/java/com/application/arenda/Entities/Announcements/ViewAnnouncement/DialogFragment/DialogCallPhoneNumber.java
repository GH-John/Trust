package com.application.arenda.Entities.Announcements.ViewAnnouncement.DialogFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Utils.PermissionUtils;
import com.application.arenda.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DialogCallPhoneNumber extends DialogFragment {
    public static String TAG = "CallPhoneNumber";
    public static short PERMISSION_CODE = 7365;

    @Nullable
    @BindView(R.id.callPhoneNumberRV)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private Intent intentPhoneNumber;

    private List<ModelPhoneNumber> collection = new ArrayList();

    public DialogCallPhoneNumber(List<ModelPhoneNumber> collection) {
        this.collection = collection;
    }

    public void callPhone(ModelPhoneNumber phoneNumber) {
        intentPhoneNumber = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber.getNumber()));

        if (!PermissionUtils.Check_CALL_PHONE(getActivity()))
            PermissionUtils.Request_CALL_PHONE(getActivity(), PERMISSION_CODE);
        else
            getActivity().startActivity(intentPhoneNumber);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_call_phone_number, container);

        unbinder = ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        DialogAdapterRV adapterRV = new DialogAdapterRV();
        adapterRV.addToCollection(collection);

        adapterRV.setOnItemClick((viewHolder, model) -> callPhone((ModelPhoneNumber) model));

        recyclerView.setAdapter(adapterRV);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
