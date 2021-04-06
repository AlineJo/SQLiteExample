package com.alienjo.sqliteexample.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alienjo.sqliteexample.R;
import com.alienjo.sqliteexample.models.Product;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {

    private ArrayList<Product> mProducts;
    private OnProductClickedListener mListener;

    public ProductsAdapter() {
        mProducts = new ArrayList<>();
    }

    public void update(ArrayList<Product> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    public void update(Product product) {
        mProducts.add(product);
        notifyItemInserted(mProducts.indexOf(product));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Product p = mProducts.get(position);

        Bitmap oldImgBitMap = getImageBitmapFromBase64(p.getProductImg());
        Glide.with(holder.ivImg.getContext()).load(oldImgBitMap).into(holder.ivImg);


        holder.tvProductName.setText(p.getProductName());
        holder.tvProductCount.setText(p.getProductCount() + "");
        holder.tvProductPrice.setText(p.getProductPrice() + "");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onProductClicked(p);
                }
            }
        });

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {

                    mProducts.remove(p);
                    notifyItemRemoved(holder.getAdapterPosition());
                    mListener.onDeleteClicked(p.getProductId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    // convert base64 String to Bitmap, so that we can display it
    private Bitmap getImageBitmapFromBase64(String base64String) {

        // Decode base64 string to image
        byte[] imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        return decodedImage;
    }


    public void setOnProductClickedListener(OnProductClickedListener listener) {
        mListener = listener;
    }

    public interface OnProductClickedListener {
        void onProductClicked(Product p);

        void onDeleteClicked(int productId);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImg;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductCount;
        ImageButton ibDelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.ivImg);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductCount = itemView.findViewById(R.id.tvProductCount);
            ibDelete = itemView.findViewById(R.id.ibDelete);

        }
    }
}
