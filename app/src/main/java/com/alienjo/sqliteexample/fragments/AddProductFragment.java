package com.alienjo.sqliteexample.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alienjo.sqliteexample.R;
import com.alienjo.sqliteexample.models.Product;
import com.alienjo.sqliteexample.models.ProductSQLite;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment {

    private static final int REQUEST_CODE_GALLERY = 100;
    byte[] mImgByteArray;
    private ImageView ivProductImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_add_product, container, false);


        ivProductImg = parentView.findViewById(R.id.ivImg);
        final EditText etProductName = parentView.findViewById(R.id.etName);
        final EditText etProductPrice = parentView.findViewById(R.id.etPrice);
        final EditText etProductDesc = parentView.findViewById(R.id.etDesc);
        final NumberPicker npProductCount = parentView.findViewById(R.id.numberPicker);
        Button btnSave = parentView.findViewById(R.id.btnSave);

        npProductCount.setMaxValue(10);
        npProductCount.setMinValue(0);


        final ProductSQLite sqLite = new ProductSQLite(getContext());


        ivProductImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = npProductCount.getValue();
                String productName = etProductName.getText().toString();
                String productPrice = etProductPrice.getText().toString();
                String productDesc = etProductDesc.getText().toString();


                Product p = new Product();

                p.setProductName(productName);
                p.setProductDesc(productDesc);
                p.setProductPrice(Double.parseDouble(productPrice));
                p.setProductCount(count);
                p.setProductImg(getBase64String(mImgByteArray));

                sqLite.addProduct(p);

                Toast.makeText(getContext(), "DONE!", Toast.LENGTH_SHORT).show();

            }
        });


        return parentView;
    }

    //this function will convert byte[] to base64 String
    private String getBase64String(byte[] byteArrayImage) {
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }


    private void openGallery() {
        Intent i = new Intent();

        //get content from another app!
        i.setAction(Intent.ACTION_GET_CONTENT);

        // we need to get image only
        i.setType("image/*");


        //Create chooser
        Intent chooser = Intent.createChooser(i, "Pick an Image");

        if (i.resolveActivity(getContext().getPackageManager()) != null) {  //open an activity and wait for the result
            startActivityForResult(chooser, REQUEST_CODE_GALLERY);
        }
    }


    // to receive the result we can use function Called onActivityResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check if there is result is Ok AND there is data
        if (resultCode == RESULT_OK && data != null) {

            //check request Code
            if (requestCode == REQUEST_CODE_GALLERY) {

                displayImageWithGlide(data);

            }

        }

    }

    private void displayImageWithGlide(Intent data) {
        //get image Uri
        Uri imgUri = data.getData();

        try {
            //#1 read image file
            InputStream imageStream = getContext().getContentResolver().openInputStream(imgUri);

            //#2 Create Bitmap
            mImgByteArray = getBytes(imageStream);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(getContext())
                .load(imgUri)// image to display
                .into(ivProductImg);// imageview
    }

    private byte[] getBytes(InputStream imageStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = imageStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();

    }
}

