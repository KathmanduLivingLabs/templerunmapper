package np.com.nirab.templerunmapper;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class RecordDetailsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    DatabaseHelper myDb;
    Spinner spinnerReligion, spinnerWater, spinnerToilet, spinnerWheelchair, spinnerOpeningHours;
    EditText editTextName, editTextNumBuildings, editTextEstablished;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_details);

        //Instantiate a new database helper
        myDb = new DatabaseHelper(this);

        //Get Refrence to the layout widgets
        editTextName = (EditText) findViewById(R.id.name_edit_text);
        editTextNumBuildings = (EditText) findViewById(R.id.number_of_buildings_edit_text);
        editTextEstablished = (EditText) findViewById(R.id.established_edit_text);
        spinnerReligion = (Spinner) findViewById(R.id.religion_spinner);
        spinnerWater = (Spinner) findViewById(R.id.water_spinner);
        spinnerToilet = (Spinner) findViewById(R.id.toilet_spinner);
        spinnerWheelchair = (Spinner) findViewById(R.id.wheelchair_spinner);
        spinnerOpeningHours = (Spinner) findViewById(R.id.opening_hour_spinner);

        // Create a GoogleApiClient instance
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    public void saveData(View view) {
        if (checkCompleted()) {
            addToDataBase();
        }
    }

    public boolean checkCompleted() {
        if(spinnerReligion.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please select a value for religion", Toast.LENGTH_LONG).show();
            return false;
        }
        if(editTextName.getText().toString().equals("")){
            Toast.makeText(this, "Please enter the name", Toast.LENGTH_LONG).show();
            return false;
        }
        if(editTextNumBuildings.getText().toString().equals("")){
            Toast.makeText(this, "Please enter the number of buildings", Toast.LENGTH_LONG).show();
            return false;
        }
        if(spinnerWater.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please specify if drinking water is available", Toast.LENGTH_LONG).show();
            return false;
        }
        if(spinnerToilet.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please specify if toilet facility is available", Toast.LENGTH_LONG).show();
            return false;
        }
        if(spinnerWheelchair.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please specify if facility has wheelchair access", Toast.LENGTH_LONG).show();
            return false;
        }
        if(spinnerOpeningHours.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Please select an opening hour", Toast.LENGTH_LONG).show();
            return false;
        }
        if(mLastLocation == null){
            Toast.makeText(this, "Location unavailable, please check you GPS and try after some time", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public void addToDataBase() {

        boolean success = myDb.insertData(spinnerReligion.getSelectedItem().toString(), editTextName.getText().toString(),
                editTextNumBuildings.getText().toString(), spinnerWater.getSelectedItem().toString(),
                spinnerToilet.getSelectedItem().toString(), spinnerWheelchair.getSelectedItem().toString(),
                editTextEstablished.getText().toString(), spinnerOpeningHours.getSelectedItem().toString(),
                getLatitude(), getLongitude(), getAccuracy(), "unsent");
        if (success) {
            Toast.makeText(this, "Voilla!! Successfully Saved !!!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Oh Oh!!! Save Failed", Toast.LENGTH_LONG).show();
        }
    }

    public String getLatitude() {
        if (mLastLocation == null){
            return null;
        }
        return String.valueOf(mLastLocation.getLatitude());
    }

    public String getLongitude() {
        if (mLastLocation == null){
            return null;
        }
        return String.valueOf(mLastLocation.getLongitude());
    }

    public String getAccuracy() {
        if (mLastLocation == null){
            return null;
        }
        return String.valueOf(mLastLocation.getAccuracy());
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    Toast.makeText(this, "Permission was denied cannot access location", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
}
