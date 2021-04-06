package com.alienjo.sqliteexample.interfaces;

import androidx.fragment.app.Fragment;

public interface MediatorInterface {
    void changeFragmentTo(Fragment fragmentToDisplay, String tag);
}
