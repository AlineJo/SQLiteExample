package com.alienjo.sqliteexample.activites;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alienjo.sqliteexample.R;
import com.alienjo.sqliteexample.fragments.ProductsListFragment;
import com.alienjo.sqliteexample.interfaces.MediatorInterface;

public class MainActivity extends AppCompatActivity implements MediatorInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeFragmentTo(new ProductsListFragment(), ProductsListFragment.class.getSimpleName());
    }

    @Override
    public void changeFragmentTo(Fragment fragmentToDisplay, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_host, fragmentToDisplay, tag);
        if (fm.findFragmentByTag(tag) == null) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }
}
