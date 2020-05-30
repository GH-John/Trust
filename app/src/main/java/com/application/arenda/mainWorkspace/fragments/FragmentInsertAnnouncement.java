package com.application.arenda.mainWorkspace.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.application.arenda.R;
import com.application.arenda.databinding.FragmentInsertAnnouncementBinding;
import com.application.arenda.entities.announcements.categories.EventSendID;
import com.application.arenda.entities.models.Currency;
import com.application.arenda.entities.models.ModelInsertAnnouncement;
import com.application.arenda.entities.models.ModelUser;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.serverApi.announcement.ApiAnnouncement;
import com.application.arenda.entities.utils.DecimalDigitsInputFilter;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;
import com.application.arenda.ui.widgets.containerImg.ContainerFiller;
import com.application.arenda.ui.widgets.containerImg.galery.AdapterGalery;
import com.application.arenda.ui.widgets.sideBar.ItemSideBar;
import com.application.arenda.ui.widgets.sideBar.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class FragmentInsertAnnouncement extends Fragment implements ItemSideBar, AdapterActionBar, AdapterGalery {
    @SuppressLint("StaticFieldLeak")
    private static FragmentInsertAnnouncement fragmentInsertAnnouncement;

    private final short SELECTED_IMG_CODE = 1001;
    private final String CHANNEL_LOADING_IMAGES_CODE = "938";
    private final String CHANNEL_LOADING_ANNOUNCEMENT_CODE = "939";

    private final NotificationCompat.Builder notificationCompatLoadImage;
    private ImageView itemBurgerMenu;

    private FragmentInsertAnnouncementBinding bind;

    private NotificationManagerCompat notificationManager;

    private SideBar sideBar;
    private ApiAnnouncement api;
    private ContainerFiller containerFiller = new ContainerFiller();

    private String userToken = null;
    private LocalCacheManager cacheManager;
    private ModelInsertAnnouncement model = new ModelInsertAnnouncement();

    private ContainerFragments containerFragments;

    private CompositeDisposable disposable = new CompositeDisposable();

    private int selectedSubcategory = -1;

    private int minTime, minDay, maxRentalPeriod;
    private LocalTime timeOfIssueWith, timeOfIssueBy, returnTimeWith, returnTimeBy;
    private String selectedAddress = "";

    private FragmentInsertAnnouncement() {
        notificationCompatLoadImage = new NotificationCompat.Builder(getContext(), CHANNEL_LOADING_IMAGES_CODE);
    }

    public static FragmentInsertAnnouncement getInstance() {
        if (fragmentInsertAnnouncement == null)
            fragmentInsertAnnouncement = new FragmentInsertAnnouncement();

        return fragmentInsertAnnouncement;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentInsertAnnouncementBinding.inflate(inflater);

        init();
        initListeners();
        return bind.getRoot();
    }

    @SuppressLint("CheckResult")
    private void init() {
        api = ApiAnnouncement.getInstance(getContext());
        cacheManager = LocalCacheManager.getInstance(getContext());

        disposable.add(cacheManager.users()
                .getActiveUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(modelUsers -> {
                    if (modelUsers.size() > 0) {
                        userToken = modelUsers.get(0).getToken();

                        inflateAddresses(modelUsers.get(0));

                        inflatePhones(modelUsers.get(0));

                    } else {
                        userToken = null;

                        inflateAddresses(null);

                        inflatePhones(null);
                    }
                }));

        containerFragments = ContainerFragments.getInstance(getContext());

        bind.containerImg.setAdapterGalery(this);
        bind.containerImg.setInstanceCounter(bind.textLimitAddPhotos);

        containerFiller.setContainer(bind.containerImg);

        notificationManager = NotificationManagerCompat.from(getContext());

        notificationCompatLoadImage
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(getString(R.string.notification_title_insert_announcement))
                .setContentText(getString(R.string.notification_loading_text_insert_announcement))
                .setOngoing(true)
                .setOnlyAlertOnce(true);
    }

    private void inflatePhones(ModelUser user) {
        if (user == null) {
            bind.checkPhone1.setVisibility(View.GONE);
            bind.checkPhone2.setVisibility(View.GONE);
            bind.checkPhone3.setVisibility(View.GONE);

            bind.btnInsertPhone.setVisibility(View.VISIBLE);

            bind.btnInsertPhone.setOnClickListener(v -> Utils.messageOutput(getContext(), getContext()
                    .getResources().getString(R.string.warning_login_required)));

            return;
        }

        if (user.getPhone_1() != null && !user.getPhone_1().isEmpty()) {
            bind.checkPhone1.setText(user.getPhone_1());
            bind.checkPhone1.setVisibility(View.VISIBLE);
        } else {
            bind.checkPhone1.setVisibility(View.GONE);
        }

        if (user.getPhone_2() != null && !user.getPhone_2().isEmpty()) {
            bind.checkPhone2.setText(user.getPhone_2());
            bind.checkPhone2.setVisibility(View.VISIBLE);
        } else {
            bind.checkPhone2.setVisibility(View.GONE);
        }

        if (user.getPhone_3() != null && !user.getPhone_3().isEmpty()) {
            bind.checkPhone3.setText(user.getPhone_3());
            bind.checkPhone3.setVisibility(View.VISIBLE);
        } else {
            bind.checkPhone3.setVisibility(View.GONE);
        }

        if (bind.checkPhone1.getVisibility() == View.VISIBLE &&
                bind.checkPhone2.getVisibility() == View.VISIBLE &&
                bind.checkPhone3.getVisibility() == View.VISIBLE) {
            bind.btnInsertPhone.setVisibility(View.GONE);
        } else {
            bind.btnInsertPhone.setVisibility(View.VISIBLE);
        }

        bind.btnInsertPhone.setOnClickListener(v -> containerFragments
                .open(FragmentUserProfile.Companion.getInstance()));
    }

    private void inflateAddresses(ModelUser user) {
        bind.withoutAddress.setChecked(true);

        if (user == null) {
            bind.address1.setVisibility(View.GONE);
            bind.address2.setVisibility(View.GONE);
            bind.address3.setVisibility(View.GONE);

            bind.btnInsertAddress.setVisibility(View.VISIBLE);

            bind.btnInsertAddress.setOnClickListener(v -> Utils.messageOutput(getContext(), getContext()
                    .getResources().getString(R.string.warning_login_required)));

            return;
        }

        if (user.getAddress_1() != null && !user.getAddress_1().isEmpty()) {
            bind.address1.setText(user.getAddress_1());
            bind.address1.setVisibility(View.VISIBLE);
        } else {
            bind.address1.setVisibility(View.GONE);
        }

        if (user.getAddress_2() != null && !user.getAddress_2().isEmpty()) {
            bind.address2.setText(user.getAddress_2());
            bind.address2.setVisibility(View.VISIBLE);
        } else {
            bind.address2.setVisibility(View.GONE);
        }

        if (user.getAddress_3() != null && !user.getAddress_3().isEmpty()) {
            bind.address3.setText(user.getAddress_3());
            bind.address3.setVisibility(View.VISIBLE);
        } else {
            bind.address3.setVisibility(View.GONE);
        }

        if (bind.address1.getVisibility() == View.VISIBLE &&
                bind.address2.getVisibility() == View.VISIBLE &&
                bind.address3.getVisibility() == View.VISIBLE) {
            bind.btnInsertAddress.setVisibility(View.GONE);
        } else {
            bind.btnInsertAddress.setVisibility(View.VISIBLE);
        }

        bind.btnInsertAddress.setOnClickListener(v -> containerFragments
                .open(FragmentUserProfile.Companion.getInstance()));
    }

    private void initListeners() {
        bind.groupAdress.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != R.id.withoutAddress)
                selectedAddress = ((RadioButton) getView().findViewById(checkedId)).getText().toString();
            else {
                selectedAddress = "";
            }
        });

        bind.layoutSeekBars.seekbarMinTime.setOnSeekbarChangeListener(value -> {
            bind.layoutSeekBars.seekbarMinTime.setTextProgressSeekBar(value.intValue() + " " + getContext().getResources().getString(R.string.text_short_hour));
            minTime = value.intValue();
        });

        bind.layoutSeekBars.seekbarMinDay.setOnSeekbarChangeListener(value -> {
            bind.layoutSeekBars.seekbarMinDay.setTextProgressSeekBar(value.intValue() + " " + getContext().getResources().getString(R.string.text_short_day));
            minDay = value.intValue();
        });

        bind.layoutSeekBars.seekbarMaxRentalPeriod.setOnSeekbarChangeListener(value -> {
            bind.layoutSeekBars.seekbarMaxRentalPeriod.setTextProgressSeekBar(value.intValue() + " " + getContext().getResources().getString(R.string.text_short_month));
            maxRentalPeriod = value.intValue();
        });

        bind.layoutSeekBars.seekbarTimeIssue.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                    bind.layoutSeekBars.seekbarTimeIssue
                            .setTextProgressSeekBar(
                                    minValue.intValue() +
                                            getContext().getResources().getString(R.string.text_time_inflater) + " - " +
                                            maxValue.intValue() +
                                            getContext().getResources().getString(R.string.text_time_inflater)
                            );

                    timeOfIssueWith = LocalTime.of(minValue.intValue(), 0);
                    timeOfIssueBy = LocalTime.of(maxValue.intValue(), 0);
                }
        );

        bind.layoutSeekBars.seekbarTimeReturn.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                    bind.layoutSeekBars.seekbarTimeReturn
                            .setTextProgressSeekBar(
                                    minValue.intValue() +
                                            getContext().getResources().getString(R.string.text_time_inflater) + " - " +
                                            maxValue.intValue() +
                                            getContext().getResources().getString(R.string.text_time_inflater)
                            );

                    returnTimeWith = LocalTime.of(minValue.intValue(), 0);
                    returnTimeBy = LocalTime.of(maxValue.intValue(), 0);
                }
        );

        bind.fieldHourlyCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});
        bind.fieldDailyCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

        bind.btnSelectCategory.setOnClickListener(v -> containerFragments.open(FragmentSelectCategory.getInstance()));

        bind.btnCreateAnnouncement.setOnClickListener(v -> {
            if (containerFiller.getSize() > 0 &&
                    !Utils.fieldIsEmpty(getContext(), bind.fieldProductName, bind.fieldDescription, bind.fieldHourlyCost, bind.fieldDailyCost)) {
                model.setUrisBitmap(new ArrayList<>(containerFiller.getUris()));
                model.setMainUriBitmap(containerFiller.getLastSelected());

                model.setName(bind.fieldProductName.getText().toString());
                model.setIdSubcategory(selectedSubcategory);
                model.setDescription(bind.fieldDescription.getText().toString());

                model.setHourlyCost(Float.parseFloat(bind.fieldHourlyCost.getText().toString()));
                model.setHourlyCurrency(Currency.USD);

                model.setDailyCost(Float.parseFloat(bind.fieldDailyCost.getText().toString()));
                model.setDailyCurrency(Currency.USD);

                model.setAddress(selectedAddress);

                model.setPhone_1(bind.checkPhone1.isChecked() ? bind.checkPhone1.getText().toString() : "");
                model.setPhone_2(bind.checkPhone2.isChecked() ? bind.checkPhone2.getText().toString() : "");
                model.setPhone_3(bind.checkPhone3.isChecked() ? bind.checkPhone3.getText().toString() : "");

                model.setMinTime(minTime);
                model.setMinDay(minDay);

                model.setMaxRentalPeriod(maxRentalPeriod);

                model.setTimeOfIssueWith(timeOfIssueWith);
                model.setTimeOfIssueBy(timeOfIssueBy);

                model.setReturnTimeWith(returnTimeWith);
                model.setReturnTimeBy(returnTimeBy);

                model.setWithSale(bind.checkBoxWithSale.isChecked());

                insertAnnouncement(model);
            }
        });
    }

    @SuppressLint("CheckResult")
    public void insertAnnouncement(@NonNull ModelInsertAnnouncement announcement) {
        if (userToken == null) {
            Utils.messageOutput(getContext(), getResources().getString(R.string.warning_login_required));
            return;
        }

        bind.progressBarInsert.setVisibility(View.VISIBLE);

        if (announcement != null) {
            disposable.add(api.insertAnnouncement(getContext(), userToken, announcement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(handler -> {
                        switch (handler.getHandler()) {
                            case SUCCESS:
                                resetComponents();
                                Utils.messageOutput(getContext(), getString(R.string.success_announcement_added));

                                bind.progressBarInsert.setVisibility(View.GONE);
                                break;

                            case USER_NOT_FOUND:
                                Utils.messageOutput(getContext(), getString(R.string.error_user_not_found));
                                bind.progressBarInsert.setVisibility(View.GONE);
                                break;

                            case NETWORK_ERROR:
                                Utils.messageOutput(getContext(), getString(R.string.error_check_internet_connect));
                                bind.progressBarInsert.setVisibility(View.GONE);
                                break;

                            case PHP_INI_NOT_LOADED:
                                Timber.tag("Error on server").e("php ini not loaded");
                                bind.progressBarInsert.setVisibility(View.GONE);
                                break;

                            case UNSUCCESS:
                            case UNKNOW_ERROR:
                                Utils.messageOutput(getContext(), getString(R.string.unknown_error));
                                bind.progressBarInsert.setVisibility(View.GONE);
                                break;
                        }
                    }, Timber::e));
        }
    }

    private void resetComponents() {
        Utils.getHandler().post(() -> {
            bind.containerImg.clearContainer();

            bind.fieldProductName.getText().clear();
            bind.fieldDescription.getText().clear();
            bind.fieldHourlyCost.getText().clear();
            bind.fieldDailyCost.getText().clear();

            bind.btnSelectCategory.setText(R.string.text_select_category);

            setVisibleAdditionalFields(false);
        });
    }

    public void setVisibleAdditionalFields(boolean b) {
        if (!b) bind.groupAdditionalFields.setVisibility(View.GONE);
        else bind.groupAdditionalFields.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SELECTED_IMG_CODE && resultCode == getActivity().RESULT_OK) {
            if (resultCode == getActivity().RESULT_OK) {
                containerFiller.setActivityResult(data, getContext());
                containerFiller.inflateContainer();
            }
        }
    }

    @Override
    public int getIdPatternResource() {
        return R.layout.ab_pattern_insert_announcement;
    }

    @Override
    public void initComponentsActionBar(ViewGroup viewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu);
    }

    @Override
    public void initListenersActionBar(ViewGroup viewGroup) {
        itemBurgerMenu.setOnClickListener(v -> sideBar.openLeftMenu());
    }

    @Override
    public int getRequestCode() {
        return SELECTED_IMG_CODE;
    }

    @Override
    public AdapterGalery getSelfInstance() {
        return this;
    }

    @Override
    public void setSideBar(SideBar sideBar) {
        this.sideBar = sideBar;
    }

    @Subscribe
    public void setSelectedSubcategory(EventSendID sendID) {
        if (sendID != null) {
            selectedSubcategory = sendID.getIdSubcategory();
            bind.btnSelectCategory.setText(sendID.getName());

            setVisibleAdditionalFields(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
        disposable.clear();
    }
}