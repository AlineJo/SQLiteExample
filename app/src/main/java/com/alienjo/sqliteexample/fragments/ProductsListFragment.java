package com.alienjo.sqliteexample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alienjo.sqliteexample.R;
import com.alienjo.sqliteexample.adapters.ProductsAdapter;
import com.alienjo.sqliteexample.interfaces.MediatorInterface;
import com.alienjo.sqliteexample.models.Product;
import com.alienjo.sqliteexample.models.ProductSQLite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductsListFragment extends Fragment {

    private ProductsAdapter mAdapter;
    private MediatorInterface mMediatorCallback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMediatorCallback = (MediatorInterface) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView =
                inflater.inflate(R.layout.fragment_product_list, container, false);

        final ProductSQLite sqLite = new ProductSQLite(getContext());

        mAdapter = new ProductsAdapter();
        mAdapter.setOnProductClickedListener(new ProductsAdapter.OnProductClickedListener() {
            @Override
            public void onProductClicked(Product p) {
                UpdateProductFragment fragment = new UpdateProductFragment();
                fragment.setProduct(p);

                mMediatorCallback.changeFragmentTo(fragment, UpdateProductFragment.class.getSimpleName());

            }

            @Override
            public void onDeleteClicked(int productId) {
                sqLite.deleteProduct(productId);
            }
        });


        RecyclerView recyclerView = parentView.findViewById(R.id.recyclerView);
        FloatingActionButton fabAdd = parentView.findViewById(R.id.fabAdd);
        setUpRecyclerView(recyclerView);


        mAdapter.update(sqLite.getProductsArrayList());

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediatorCallback.changeFragmentTo(new AddProductFragment(), AddProductFragment.class.getSimpleName());
            }
        });

        return parentView;
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
}
