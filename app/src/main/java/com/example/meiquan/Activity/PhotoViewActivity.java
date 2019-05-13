package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.meiquan.R;
import com.github.chrisbanes.photoview.PhotoView;

public class PhotoViewActivity extends AppCompatActivity {
    @BindView(R.id.ph_showbigimage) PhotoView ph_showbigimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        ButterKnife.bind(this);


        String image_url = getIntent().getStringExtra("image_url");
        Glide.with(this).load(image_url).into(ph_showbigimage);
    }
}
