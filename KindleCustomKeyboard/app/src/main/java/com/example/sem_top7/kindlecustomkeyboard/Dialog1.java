package com.example.sem_top7.kindlecustomkeyboard;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sem_top7 on 29.11.2015.
 */
public class Dialog1 extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid_layout_keyboard_ftv_ru, null);
        return v;
    }
}
