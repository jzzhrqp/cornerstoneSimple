package com.aoshuotec.iron.simpleDemo;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.aoshuotec.iron.simpleDemo.BR;

import org.jetbrains.annotations.NotNull;

/**
 * Created by iron on 17-12-28.
 */

public class data extends BaseObservable {
    private String name;
    private String title;


    public data(String name, String title) {
        setName(name);
        setTitle(title);

    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);

    }

}
