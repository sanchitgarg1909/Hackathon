package org.example.androidsdk.hackathon;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MapFragment extends Fragment implements OnMapReadyCallback,LocationListener{

    public GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Socket mSocket;
    private View rootView;
    private Boolean flag = false;
    LocationManager lm;

    {
        try {
//            mSocket = IO.socket("http://ec2-54-179-137-10.ap-southeast-1.compute.amazonaws.com:8888");
            mSocket = IO.socket("http://ec2-54-187-141-92.us-west-2.compute.amazonaws.com/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mSocket.emit("new user", new UserSharedPref(getActivity()).getCurrentUser().getId());
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on("update marker", onNewLocation);
        mSocket.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = new SupportMapFragment() {
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                initialiseMap();
            }
        };
        SwitchCompat swt = (SwitchCompat)rootView.findViewById(R.id.Switch);
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Toast.makeText(getActivity(), "checked off", Toast.LENGTH_SHORT).show();
                    flag = false;
                } else {
                    Toast.makeText(getActivity(), "checked on", Toast.LENGTH_SHORT).show();
                    flag = true;
//                    mSocket.emit("new user", new UserSharedPref(getActivity()).getCurrentUser().getId());
                }
            }
        });
        getChildFragmentManager().beginTransaction().add(R.id.container_map, mapFragment).commit();
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, this);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        lm.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewLocation);
    }

    public void initialiseMap() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        googleMap.setMyLocationEnabled(true);
//        googleMap.setOnMyLocationChangeListener(this);
    }


//    @Override
//    public void onMyLocationChange(Location location) {
//        Toast.makeText(getActivity(),"changed",Toast.LENGTH_SHORT).show();
//        Log.d("longlat", String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude()));
////        if(flag){
//////            Toast.makeText(getActivity(),"changed",Toast.LENGTH_SHORT).show();
////            HashMap<String,String> params = new HashMap<>();
////            params.put("long", String.valueOf(location.getLongitude()));
////            params.put("lat", String.valueOf(location.getLatitude()));
////            mSocket.emit("Location Change", new JSONObject(params));
////            mSocket.connect();
////        }
//    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Connected", Toast.LENGTH_LONG).show();
                    mSocket.emit("new user", new UserSharedPref(getActivity()).getCurrentUser().getId());
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Failed to connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNewLocation = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LatLng latLng = new LatLng(Double.parseDouble(String.valueOf(args[0])), Double.parseDouble(String.valueOf(args[1])));
                    Marker marker = googleMap.addMarker(new MarkerOptions().draggable(false).position(latLng));
                }
            });
        }
    };

    @Override
    public void onLocationChanged(Location location) {
//        Toast.makeText(getActivity(),"location changed",Toast.LENGTH_SHORT).show();
        Log.d("longlat", String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude()));
        if(flag){
            Toast.makeText(getActivity(),"location changed",Toast.LENGTH_SHORT).show();
            HashMap<String,String> params = new HashMap<>();
            params.put("long", String.valueOf(location.getLongitude()));
            params.put("lat", String.valueOf(location.getLatitude()));
            mSocket.emit("Location Change", new JSONObject(params));
//            mSocket.connect();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(getActivity(),"status changed "+provider+","+status,Toast.LENGTH_SHORT).show();
        Log.d("longlat",provider+","+status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(),"provider enabled",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(),"provider disabled",Toast.LENGTH_SHORT).show();
    }
}
