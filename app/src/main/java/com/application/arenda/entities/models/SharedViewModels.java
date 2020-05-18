package com.application.arenda.entities.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModels extends ViewModel {
    private final MutableLiveData<ModelAnnouncement> selectedAnnouncement = new MutableLiveData<>();
    private final MutableLiveData<List<ModelAnnouncement>> collectionAnnouncements = new MutableLiveData<>();

    private final MutableLiveData<ModelAnnouncement> selectedLandLordAnnouncement = new MutableLiveData<>();
    private final MutableLiveData<List<ModelAnnouncement>> collectionLandLordAnnouncements = new MutableLiveData<>();

    private final MutableLiveData<ModelAnnouncement> selectedSimilarAnnouncement = new MutableLiveData<>();
    private final MutableLiveData<List<ModelAnnouncement>> collectionSimilarAnnouncements = new MutableLiveData<>();

    public void selectAnnouncement(ModelAnnouncement model) {
        selectedAnnouncement.postValue(model);
    }

    public LiveData<ModelAnnouncement> getSelectedAnnouncement() {
        return selectedAnnouncement;
    }

    public void setCollectionAnnouncements(List<ModelAnnouncement> announcements) {
        collectionAnnouncements.postValue(announcements);
    }

    public LiveData<List<ModelAnnouncement>> getLastCollectionAnnouncements() {
        return collectionAnnouncements;
    }


    public void selectLandLordAnnouncement(ModelAnnouncement model) {
        selectedLandLordAnnouncement.postValue(model);
    }

    public LiveData<ModelAnnouncement> getSelectedLandLordAnnouncement() {
        return selectedLandLordAnnouncement;
    }

    public void setLandLordAnnouncements(List<ModelAnnouncement> announcements) {
        collectionLandLordAnnouncements.postValue(announcements);
    }

    public LiveData<List<ModelAnnouncement>> getLastLandLordAnnouncements() {
        return collectionLandLordAnnouncements;
    }


    public void selectSimilarAnnouncement(ModelAnnouncement model) {
        selectedSimilarAnnouncement.postValue(model);
    }

    public LiveData<ModelAnnouncement> getSelectedSimilarAnnouncement() {
        return selectedSimilarAnnouncement;
    }

    public void setSimilarAnnouncements(List<ModelAnnouncement> announcements) {
        collectionSimilarAnnouncements.postValue(announcements);
    }

    public LiveData<List<ModelAnnouncement>> getLastSimilarAnnouncements() {
        return collectionSimilarAnnouncements;
    }
}