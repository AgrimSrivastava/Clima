package com.myapp.theagrim.clima;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;
    LocationManager locationManager;
    LocationListener locationListener;

    TextView location,temperature;
    ImageButton change_city;
    ImageView icon_;

    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String APP_ID = "fdf16c57c2ed0ec8702dec2851bd1884";
    final int REQUEST_CODE=69;
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        location=findViewById(R.id.locationTV);
        temperature=findViewById(R.id.tempTV);
        change_city=findViewById(R.id.changeCityButton);
        icon_=findViewById(R.id.weatherSymbolIV);

        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,change_city.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clima", "onResume called");
        Log.d("Clima", "fetching the weather location");
        Intent intent=getIntent();
        String city=intent.getStringExtra("city");
        if(city!=null){
            getWeatherForCity(city);
        }
        else {
            getWeatherForCurrentLocation();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
        }
    }//used to free up the resources, i.e getting location updates , when app is in background


    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged() callback received");
                String latitude=String.valueOf(location.getLatitude());
                String longitude= String.valueOf(location.getLongitude());
                Log.d("Clima","Latitude is"+latitude+"Longitude is"+longitude);

                RequestParams params=new RequestParams();
                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                letsConnect(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Clima", "onProviderDisabled() callback received");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("Clima","Permission granted");
                getWeatherForCurrentLocation();

            }
            else{
                Log.d("Clima","Permission Denied");
            }
        }
    }


    private void letsConnect(RequestParams params){
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
               Log.d("Clima",response.toString());
                DataExtracter dataExtracter=DataExtracter.extracter(response);
                updateUI(dataExtracter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,Throwable e, JSONObject response){
                Log.e("Clima","Fail"+e.toString());
                Log.d("Clima","StatusCode"+statusCode);
                Toast.makeText(MainActivity.this,"Request Failed",Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void updateUI(DataExtracter dataExtracter){
        location.setText(dataExtracter.getCity());
        temperature.setText(dataExtracter.getTemperature());
        int resourceId=getResources().getIdentifier(dataExtracter.getIconname(),"drawable",getPackageName());
        icon_.setImageResource(resourceId);
    }

    private void getWeatherForCity(String city) {
        RequestParams params = new RequestParams();
        params.put("city", city);
        params.put("appid",APP_ID);
        letsConnect(params);
    }
}
