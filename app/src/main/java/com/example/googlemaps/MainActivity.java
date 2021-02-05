package com.example.googlemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


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
    String[] lugares;
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
        this.mapa.addMarker(new MarkerOptions().position(punto).title("Punto " + this.con++));
        this.optionsPoly.add(punto);
    }

    public void btnLocalizarCamara(View view){

        this.lugares = new String[]{"Quinsaloma", "Quevedo", "Ecuador"};

        new MaterialAlertDialogBuilder(this)
                .setTitle("Ciudades")
                .setItems(lugares, (dialog, which) -> {
                    cambiarLugar(which);
                }).show();
    }
    public void cambiarLugar(int lugar){
        int zoom = 14;
        if(lugar == 2)
            zoom = 6;
        Eliminar(null);
        CameraPosition posiCamara = new CameraPosition.Builder()
                .target(this.puntosLatLng[lugar])
                .zoom(zoom)
                .bearing(45)
                .build();
        CameraUpdate camara = CameraUpdateFactory.newCameraPosition(posiCamara);
        mapa.animateCamera(camara);
        this.mapa.addMarker(new MarkerOptions().position(this.puntosLatLng[lugar]).title(lugares[lugar]));

    }

    public void btndibujarPunto(View view){
        this.optionsPoly.add(this.puntosLatLng[0]);
        this.optionsPoly.color(Color.rgb(150, 150, 150));
        this.mapa.addPolyline(this.optionsPoly);
    }

    public void Eliminar(View view){
        this.con = 0;
        this.mapa.clear();
        this.optionsPoly = new PolylineOptions();
    }
}