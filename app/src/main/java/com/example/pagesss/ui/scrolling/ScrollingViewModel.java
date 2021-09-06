package com.example.pagesss.ui.scrolling;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScrollingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ScrollingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is scrolling fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}