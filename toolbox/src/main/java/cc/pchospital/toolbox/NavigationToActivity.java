package cc.pchospital.toolbox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

public class NavigationToActivity extends AppCompatActivity {

    public static void actionStart(Context context, Double LocationLa, Double LocationLo){
        Intent intent = new Intent(context, NavigationToActivity.class);
        intent.putExtra("Latitude", LocationLa);
        intent.putExtra("Longitude", LocationLo);
        context.startActivity(intent);
    }

    MapView map = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_to);
        map = findViewById(R.id.map);
        map.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_navigation_to));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent it = getIntent();
        LatLng target = new LatLng(it.getDoubleExtra("Latitude", 0.0),
                it.getDoubleExtra("Longitude", 0.0));

        initMap(target);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    void initMap(LatLng target){
        AMap aMap = map.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(1000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(target, 15, 0, 0)
        );
        aMap.animateCamera(update);

        aMap.addMarker(new MarkerOptions().position(target));
    }
}
