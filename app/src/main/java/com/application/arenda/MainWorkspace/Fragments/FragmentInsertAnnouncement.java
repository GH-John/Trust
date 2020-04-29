package com.application.arenda.MainWorkspace.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.application.arenda.Entities.Announcements.ApiAnnouncement;
import com.application.arenda.Entities.Announcements.IApiAnnouncement;
import com.application.arenda.Entities.Announcements.InsertAnnouncement.Categories.EventSendID;
import com.application.arenda.Entities.Models.ModelInsertAnnouncement;
import com.application.arenda.Entities.Room.LocalCacheManager;
import com.application.arenda.Entities.Utils.DecimalDigitsInputFilter;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.Components.ActionBar.AdapterActionBar;
import com.application.arenda.UI.Components.ContainerFragments.ContainerFragments;
import com.application.arenda.UI.Components.SideBar.ItemSideBar;
import com.application.arenda.UI.Components.SideBar.SideBar;
import com.application.arenda.UI.ContainerImg.ContainerFiller;
import com.application.arenda.UI.ContainerImg.ContainerSelectedImages;
import com.application.arenda.UI.ContainerImg.Galery.AdapterGalery;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private NotificationManagerCompat notificationManager;

    private SideBar sideBar;
    private Unbinder unbinder;
    private ApiAnnouncement api;
    private ContainerFiller containerFiller = new ContainerFiller();

    private String userToken = null;
    private LocalCacheManager cacheManager;
    private ModelInsertAnnouncement announcement = new ModelInsertAnnouncement();

    private ContainerFragments containerFragments;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private int selectedSubcategory = -1;

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

        initComponents();
        initStyles();
        initListeners();

        return view;
    }

    @SuppressLint("CheckResult")
    private void initComponents() {
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

    private void initStyles() {
        SetBtnStyle.setStyle(new ComponentBackground(getContext(), R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnCreateAnnouncement);
        SetBtnStyle.setStyle(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnSelectCategory);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldProductName, fieldCostProduct);
        SetFieldStyle.setEditTextBackground(new ComponentBackground(getContext(), R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), fieldProductDescription);
    }

    private void initListeners() {
        fieldCostProduct.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

        btnSelectCategory.setOnClickListener(v -> containerFragments.open(FragmentSelectCategory.getInstance()));

        btnCreateAnnouncement.setOnClickListener(v -> {
            if (containerFiller.getSize() > 0 &&
                    !Utils.fieldIsEmpty(getContext(), fieldProductName, fieldProductDescription, fieldCostProduct)) {
                announcement.setUrisBitmap(new ArrayList<>(containerFiller.getUris()));
                announcement.setMainUriBitmap(containerFiller.getLastSelected());

                announcement.setName(fieldProductName.getText().toString());
                announcement.setIdSubcategory(selectedSubcategory);
                announcement.setDescription(fieldProductDescription.getText().toString());

                announcement.setCostToBYN(Float.parseFloat(fieldCostProduct.getText().toString()));
                announcement.setCostToUSD(0f);
                announcement.setCostToEUR(0f);

                announcement.setAddress("адрес");

                announcement.setPhone_1("+375(29)659-50-73");
                announcement.setPhone_2("+375(29)681-37-83");
                announcement.setPhone_3("+375(44)702-04-50");

                insertAnnouncement(announcement);
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