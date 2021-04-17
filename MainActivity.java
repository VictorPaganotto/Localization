package com.example.tcc2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.media.MediaPlayer;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer media;
    Button btnGps,som;
    TextView txtLatitude, txtLongitude;
    int acabou=1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);

        btnGps = (Button) findViewById(R.id.Localizacao);
        btnGps.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                pedirPermissoes();
            }
        });
    }

    private void pedirPermissoes() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
            configurarServico();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                } else {
                    Toast.makeText(this, "Não vai funcionar!!!", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location)
    {
        int localizacao_valida=0;
        Double latPoint = location.getLatitude();
        Double lngPoint = location.getLongitude();


        txtLatitude.setText(latPoint.toString());
        txtLongitude.setText(lngPoint.toString());
        latPoint=latPoint*-1;
        lngPoint=lngPoint*-1;

        if (latPoint >= 23.4767 && latPoint <= 23.4773 && lngPoint>= 46.6319 && lngPoint<= 46.6324 ) {

            media = MediaPlayer.create(this, R.raw.som);
            localizacao_valida=1;

        }
        if (latPoint >= 22.8205 && latPoint <= 22.8227 && lngPoint>= 47.0647 && lngPoint<= 47.0680 ) {

            media = MediaPlayer.create(this, R.raw.feec);
            localizacao_valida=1;

        }
        if (latPoint >= 23.3717 && latPoint <= 23.3724 && lngPoint>= 46.6352 && lngPoint<= 46.6361 ) {

            media = MediaPlayer.create(this, R.raw.cuca);
            localizacao_valida=1;

        } if(localizacao_valida!=1){

            media = null;

        }

            Button play= (Button) findViewById(R.id.som);
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (media != null) {
                        if (acabou == 1) {
                            media.start();
                            acabou = 0;
                            media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    acabou = 1;
                                }
                            });
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "A sua localidade não possui áudio disponível",Toast.LENGTH_SHORT).show();
                    }

                }
            });

    }
}