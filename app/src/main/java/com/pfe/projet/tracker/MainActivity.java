package dz.finalprojectchp_1.appandroid.location_4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{

    //final attribute
        private final static String REQUESTING_LOCATION_UPDATE_KEY="RLU_KEY";
        private final static String LOCATION_KEY="LOCATION_KEY";
        private final static String LAST_UPDATED_TIME_STRING_KEY="LUTS_KEY";
    //view attribute
        private TextView latitude;
        private TextView longitude;
        private TextView time;
        private TextView imeiID;
   //location attribute
        private GoogleApiClient mGoogleApiClient;
        private Location mLastLocation;
        private Location mCurrentLocation;
        private LocationRequest mLocationRequest;
        private String mLastUpdateTime;
        private boolean mRequestingLocationUpdates;
    //database attribute
        private DBAdapter database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get reference view
            latitude = (TextView) findViewById(R.id.txtLatitudeValue);
            longitude = (TextView) findViewById(R.id.txtLongitudeValue);
            time = (TextView) findViewById(R.id.TimeValue);
            imeiID = (TextView) findViewById(R.id.ImeiValue);
        //call function getIMEI Device Id
            imeiID.setText(getIMEI(this));

        //call build googleApiClient
            buildGoogleApiClient();
        //call update value save Instance state get
            updateValuesFromBundle( savedInstanceState);

        //database instance
            database = new DBAdapter(this);
    }

    //function use for save Instance state
        public void updateValuesFromBundle(Bundle savedInstanceState){
            if( savedInstanceState != null){
                if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATE_KEY)){
                    mRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATE_KEY);
                }
                if(savedInstanceState.keySet().contains(LOCATION_KEY)){
                    mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
                }
                if(savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)){
                    mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
                }

            }
        }
        //save instance state
        @Override
        public void onSaveInstanceState(Bundle outState) {

            outState.putBoolean(REQUESTING_LOCATION_UPDATE_KEY,mRequestingLocationUpdates);
            outState.putParcelable(LOCATION_KEY,mCurrentLocation);
            outState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);

            super.onSaveInstanceState(outState);

        }
    //function get imei id
        private String getIMEI(Context context){
            TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            return  manager.getDeviceId();
        }
    //functions for location tools
        //function
            private void buildGoogleApiClient(){
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();

            }

        //function
            private void createLocationRequest(){
                mLocationRequest = new LocationRequest()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(5000)
                        .setFastestInterval(2000);
            }
        //function
            private void startLocationUpdate(){
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                mRequestingLocationUpdates=true;

            }
        //function
            private void stopLocationUpdate(){
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }

        @Override
        protected void onStart() {
            super.onStart();
            if( !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();

            }
        }


        @Override
        protected void onStop() {
            if( mGoogleApiClient.isConnected())
                mGoogleApiClient.disconnect();
            super.onStop();
        }



    @Override
    public void onConnected(Bundle bundle) {
        /* mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if( mLastLocation != null){
            latitude.setText(String.valueOf(mLastLocation.getLatitude()));
            longitude.setText(String.valueOf(mLastLocation.getLongitude()));
        }*/

        createLocationRequest();
        startLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("azz_b", "connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("azz_b", "connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("azz_b :","Location Changed");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getDateTimeInstance().format(new Date());

        updateUI();
        long id = database.insert( getIMEI(this) , mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),mLastUpdateTime);
        Msg.toastMsgShort(this,""+id);
        if(id > 0){
            Msg.toastMsgShort(this,"seccessfule");
        }else {
            Msg.toastMsgShort(this,"inseccessfule");


        }
    }

    //function update user interface when request latitude and longitude  , time
        private void updateUI(){
            latitude.setText(String.valueOf(mCurrentLocation.getLatitude()));
            longitude.setText(String.valueOf(mCurrentLocation.getLongitude()));
            time.setText(mLastUpdateTime);
        }


    //----------------------------------
    //button click
    public void startBtnClick(View view){
        startLocationUpdate();

    }

    public void stopBtnClick(View view){
        stopLocationUpdate();
    }
}
