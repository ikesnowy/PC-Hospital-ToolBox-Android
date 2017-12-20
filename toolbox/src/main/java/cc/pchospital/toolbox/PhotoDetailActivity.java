package cc.pchospital.toolbox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PhotoDetailActivity extends AppCompatActivity {

    public static void actionStart(Context context, String pictureUrl) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra("url", pictureUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_picture_detail);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView imageView = findViewById(R.id.picture_detail);
        Glide.with(this)
                .load(getIntent().getStringExtra("url"))
                .into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
