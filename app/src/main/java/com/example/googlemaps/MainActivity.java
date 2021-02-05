package com.example.googlemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    LatLng[] puntosLatLng;
    PolylineOptions optionsPoly;
    GoogleMap mapa;
    int con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.contenedor);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //--- Inicializando las variables globales
        this.optionsPoly = new PolylineOptions();
        this.mapa = googleMap;
        this.con = 1;

        this.puntosLatLng = new LatLng[]{
                new LatLng(-1.2006058,-79.3240221), //Quinsaloma
                new LatLng(-1.0286300, -79.4635200), //Quevedo
                new LatLng(-1.831239, -78.183406)};  //Ecuador

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marcarPunto(latLng);
            }
        });
        CameraUpdate camara = CameraUpdateFactory.newLatLngZoom(this.puntosLatLng[2], 6);
        googleMap.moveCamera(camara);
    }

    public void marcarPunto(LatLng punto){
        this.optionsPoly.add(punto);
        this.mapa.addMarker(new MarkerOptions().position(punto).title("Punto " + this.con++));
    }

    public void btnLocalizarCamara(View view){

        String[] item = {"Quinsaloma", "Quevedo", "Ecuador"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("Ciudades")
                .setItems(item, (dialog, which) -> {
                    cambiarLugar(which);
                }).show();
    }
    public void cambiarLugar(int lugar){
        int zoom = 14;
        if(lugar == 2)
            zoom = 6;
        CameraPosition posiCamara = new CameraPosition.Builder()
                .target(this.puntosLatLng[lugar])
                .zoom(zoom)
                .bearing(45)
                .build();
        CameraUpdate camara = CameraUpdateFactory.newCameraPosition(posiCamara);
        mapa.animateCamera(camara);
    }

    public void btndibujarPunto(View view){
        this.optionsPoly.add( this.puntosLatLng[0]);
        this.optionsPoly.width(6);
        this.optionsPoly.color(Color.rgb(150, 150, 150));
        this.mapa.addPolyline(this.optionsPoly);
    }

    public void Eliminar(View view){
        this.mapa.clear();
        this.optionsPoly = new PolylineOptions();
    }
}