package com.nerus.apparquos.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import com.nerus.apparquos.helpers.clsUtilities.LogSNE;

import static android.content.Context.LOCATION_SERVICE;

public final class GpsTracker implements LocationListener {
    private Boolean mIsGPSEnabled=false;

    public Boolean getGPSEnabled() {
        return mIsGPSEnabled;
    }

    Context context;

    public GpsTracker(Context context) {
        super();
        this.context = context;
    }

    public Location getLocation(){
        if (ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            LogSNE.d("NERUS","error checkSelfPermission");
            return null;
        }
        try {
            LocationManager lm = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            mIsGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (mIsGPSEnabled){
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000,5,this);
                Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return loc;
            }else{
                LogSNE.d("NERUS","errpr isGPSEnabled");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
