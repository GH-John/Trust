package com.application.arenda.mainWorkspace.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;
import com.application.arenda.entities.announcements.categories.EventSendID;
import com.application.arenda.entities.models.ModelInsertAnnouncement;
import com.application.arenda.entities.room.LocalCacheManager;
import com.application.arenda.entities.serverApi.announcement.ApiAnnouncement;
import com.application.arenda.entities.serverApi.announcement.IApiAnnouncement;
import com.application.arenda.entities.utils.DecimalDigitsInputFilter;
import com.application.arenda.entities.utils.Utils;
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar;
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments;
import com.application.arenda.ui.widgets.containerImg.ContainerFiller;
import com.application.arenda.ui.widgets.containerImg.ContainerSelectedImages;
import com.application.arenda.ui.widgets.containerImg.galery.AdapterGalery;
import com.application.arenda.ui.widgets.seekBar.CustomSeekBar;
import com.application.arenda.ui.widgets.sideBar.ItemSideBar;
import com.application.arenda.ui.widgets.sideBar.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class FragmentInsertAnnouncement extends Fragment implements ItemSideBar, AdapterActionBar, AdapterGalery {
    @SuppressLint("StaticFieldLeak")
    private static FragmentInsertAnnouncement fragmentInsertAnnouncement;

    private final short SELECTED_IMG_CODE = 1001;
    private final String CHANNEL_LOADING_IMAGES_CODE = "938";
    private final String CHANNEL_LOADING_ANNOUNCEMENT_CODE = "939";

    private final NotificationCompat.Builder notificationCompatLoadImage;

    @Nullable
    @BindView(R.id.groupPhones)
    Group groupPhones;

    @Nullable
    @BindView(R.id.itemBurgerMenu)
    ImageView itemBurgerMenu;

    @Nullable
    @BindView(R.id.categoryTitle)
    TextView categoryTitle;

    @Nullable
    @BindView(R.id.categoryHeaderIndicator)
    ImageView categoryHeaderIndicator;

    @Nullable
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;

    @Nullable
    @BindView(R.id.groupAdditionalFields)
    Group groupAdditionalFields;

    @Nullable
    @BindView(R.id.textLimitAddPhotos)
    TextView textLimitAddPhotos;

    @Nullable
    @BindView(R.id.btnCreateAnnouncement)
    Button btnCreateAnnouncement;

    @Nullable
    @BindView(R.id.btnSelectCategory)
    Button btnSelectCategory;

    @Nullable
    @BindView(R.id.containerImg)
    ContainerSelectedImages containerSelectedImages;

    @Nullable
    @BindView(R.id.fieldProductName)
    EditText fieldProductName;

    @Nullable
    @BindView(R.id.fieldDescription)
    EditText fieldProductDescription;

    @Nullable
    @BindView(R.id.fieldCostProduct)
    EditText fieldCostProduct;

    @BindView(R.id.checkBoxWithSale)
    CheckBox checkBoxWithSale;

    @BindView(R.id.checkPhone_1)
    CheckBox checkPhone_1;

    @BindView(R.id.checkPhone_2)
    CheckBox checkPhone_2;

    @BindView(R.id.checkPhone_3)
    CheckBox checkPhone_3;

    @BindView(R.id.seekbarMinTime)
    CustomSeekBar seekbarMinTime;

    @BindView(R.id.seekbarMinDay)
    CustomSeekBar seekbarMinDay;

    @BindView(R.id.seekbarMaxRentalPeriod)
    CustomSeekBar seekbarMaxRentalPeriod;

    @BindView(R.id.seekbarTimeIssue)
    CustomSeekBar seekbarTimeIssue;

    @BindView(R.id.seekbarTimeReturn)
    CustomSeekBar seekbarTimeReturn;

    private NotificationManagerCompat notificationManager;

    private SideBar sideBar;
    private Unbinder unbinder;
    private ApiAnnouncement api;
    private ContainerFiller containerFiller = new ContainerFiller();

    private String userToken = null;
    private LocalCacheManager cacheManager;
    private ModelInsertAnnouncement model = new ModelInsertAnnouncement();

    private ContainerFragments containerFragments;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int selectedSubcategory = -1;

    private int minTime, minDay, maxRentalPeriod;
    private LocalTime timeOfIssueWith, timeOfIssueBy, returnTimeWith, returnTimeBy;

    private FragmentInsertAnnouncement() {
        notificationCompatLoadImage = new NotificationCompat.Builder(getContext(), CHANNEL_LOADING_IMAGES_CODE);
    }

    public static FragmentInsertAnnouncement getInstance() {
        if (fragmentInsertAnnouncement == null)
            fragmentInsertAnnouncement = new FragmentInsertAnnouncement();

        return fragmentInsertAnnouncement;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_announcement, container, false);

        unbinder = ButterKnife.bind(this, view);

        init();
        initListeners();

        return view;
    }

    @SuppressLint("CheckResult")
    private void init() {
        api = ApiAnnouncement.getInstance();
        cacheManager = LocalCacheManager.getInstance(getContext());

        cacheManager.users()
                .getActiveUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(modelUsers -> {
                    if (modelUsers.size() > 0)
                        userToken = modelUsers.get(0).getToken();
                    else
                        userToken = null;
                });

        containerFragments = ContainerFragments.getInstance(getContext());

        containerSelectedImages.setAdapterGalery(this);
        containerSelectedImages.setInstanceCounter(textLimitAddPhotos);

        containerFiller.setContainer(containerSelectedImages);

        notificationManager = NotificationManagerCompat.from(getContext());

        notificationCompatLoadImage
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(getString(R.string.notification_title_insert_announcement))
                .setContentText(getString(R.string.notification_loading_text_insert_announcement))
                .setOngoing(true)
                .setOnlyAlertOnce(true);
    }

    private void initListeners() {
        seekbarMinTime.setOnSeekbarChangeListener(value -> {
            seekbarMinTime.setTextProgressSeekBar(value.intValue() + " " + getContext().getResources().getString(R.string.text_short_hour));
            minTime = value.intValue();
        });

        seekbarMinDay.setOnSeekbarChangeListener(value -> {
            seekbarMinDay.setTextProgressSeekBar(value.intValue() + " " + getContext().getResources().getString(R.string.text_short_day));
            minDay = value.intValue();
        });

        seekbarMaxRentalPeriod.setOnSeekbarChangeListener(value -> {
            seekbarMaxRentalPeriod.setTextProgressSeekBar(value.intValue() + " " + getContext().getResources().getString(R.string.text_short_month));
            maxRentalPeriod = value.intValue();
        });

        seekbarTimeIssue.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                    seekbarTimeIssue
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

        seekbarTimeReturn.setOnRangeSeekbarChangeListener((minValue, maxValue) -> {
                    seekbarTimeReturn
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

        fieldCostProduct.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

        btnSelectCategory.setOnClickListener(v -> containerFragments.open(FragmentSelectCategory.getInstance()));

        btnCreateAnnouncement.setOnClickListener(v -> {
            if (containerFiller.getSize() > 0 &&
                    !Utils.fieldIsEmpty(getContext(), fieldProductName, fieldProductDescription, fieldCostProduct)) {
                model.setUrisBitmap(new ArrayList<>(containerFiller.getUris()));
                model.setMainUriBitmap(containerFiller.getLastSelected());

                model.setName(fieldProductName.getText().toString());
                model.setIdSubcategory(selectedSubcategory);
                model.setDescription(fieldProductDescription.getText().toString());

                model.setCostToUSD(Float.parseFloat(fieldCostProduct.getText().toString()));

                model.setAddress("адрес");

                model.setPhone_1(checkPhone_1.isChecked() ? checkPhone_1.getText().toString() : "");
                model.setPhone_2(checkPhone_2.isChecked() ? checkPhone_2.getText().toString() : "");
                model.setPhone_3(checkPhone_3.isChecked() ? checkPhone_3.getText().toString() : "");

                model.setMinTime(minTime);
                model.setMinDay(minDay);

                model.setMaxRentalPeriod(maxRentalPeriod);

                model.setTimeOfIssueWith(timeOfIssueWith);
                model.setTimeOfIssueBy(timeOfIssueBy);

                model.setReturnTimeWith(returnTimeWith);
                model.setReturnTimeBy(returnTimeBy);

                model.setWithSale(checkBoxWithSale.isChecked());

                insertAnnouncement(model);
            }
        });
    }

    @SuppressLint("CheckResult")
    public void insertAnnouncement(@NonNull ModelInsertAnnouncement announcement) {
        if (announcement != null && userToken != null) {

            api.insertAnnouncement(getContext(), userToken, announcement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<IApiAnnouncement.AnnouncementCodes>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(IApiAnnouncement.AnnouncementCodes announcementCodes) {
                            switch (announcementCodes) {
                                case SUCCESS_ANNOUNCEMENT_ADDED:
                                case SUCCESS_PICTURES_ADDED:
                                    resetComponents();
                                    Utils.messageOutput(getContext(), getString(R.string.success_announcement_added));
                                    break;

                                case USER_NOT_FOUND:
                                    Utils.messageOutput(getContext(), getString(R.string.error_user_not_found));
                                    break;

                                case NETWORK_ERROR:
                                    Utils.messageOutput(getContext(), getString(R.string.error_check_internet_connect));
                                    break;

                                case PHP_INI_NOT_LOADED:
                                    Timber.tag("Error on server").e(IApiAnnouncement.AnnouncementCodes.PHP_INI_NOT_LOADED.name());

                                case UNSUCCESS_ANNOUNCEMENT_ADDED:
                                case UNKNOW_ERROR:
                                    Utils.messageOutput(getContext(), getString(R.string.unknown_error));
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Timber.e(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void resetComponents() {
        Utils.getHandler().post(() -> {
            containerSelectedImages.clearContainer();

            fieldProductName.getText().clear();
            fieldProductDescription.getText().clear();
            fieldCostProduct.getText().clear();

            btnSelectCategory.setText(R.string.text_select_category);

            setVisibleAdditionalFields(false);
        });
    }

    public void setVisibleAdditionalFields(boolean b) {
        if (!b) groupAdditionalFields.setVisibility(View.GONE);
        else groupAdditionalFields.setVisibility(View.VISIBLE);
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
            btnSelectCategory.setText(sendID.getName());

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
        compositeDisposable.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}