package me.prateeksaigal.ocotocoffee;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectedMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private Double machine_lat, machine_lon, user_lat, user_lon;
    private int PAYMENT_CODE = 1;
    private TextView payment_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        machine_lat = getIntent().getDoubleExtra("machine_lat", -34);
        machine_lon = getIntent().getDoubleExtra("machine_lon", 151);
        user_lat = getIntent().getDoubleExtra("user_lat", -34.0001);
        user_lon = getIntent().getDoubleExtra("user_lon", -151.003);


        mapFragment.getMapAsync(this);


        LinearLayout back = (LinearLayout) findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Button confirm_coffee = (Button) findViewById(R.id.confirm_coffee);
        payment_method = (TextView) findViewById(R.id.payment_method);

        payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getApplicationContext(), PaymentActivity.class);
                startActivityForResult(i, PAYMENT_CODE);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );


            }
        });


        confirm_coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_method.getText().toString().trim().equals("Payment Method")) {
                    Toast.makeText(getApplicationContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
                } else {
                    confirm_coffee.setText("Confirming");
                    confirm_coffee.setBackgroundColor(getResources().getColor(R.color.button_payment_ongoing));
                    AlertDialog.Builder ImageDialog = new AlertDialog.Builder(SelectedMapsActivity.this, R.style.MyDialogTheme);
                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.payment_loading_layout, (ViewGroup) getCurrentFocus());
                    final ImageView payment_status = (ImageView) dialoglayout.findViewById(R.id.payment_status_img);
                    final TextView payment_status_txt = (TextView) dialoglayout.findViewById(R.id.payment_status_txt);
                    ImageDialog.setView(dialoglayout);
                    ImageDialog.setCancelable(false);
                    ImageDialog.show();

                    new CountDownTimer(3000, 1000) {

                        @Override
                        public void onFinish() {
                            payment_status_txt.setText("Connecting Machine");
                            payment_status.setImageResource(R.drawable.payment_status_2);
                            new CountDownTimer(3000, 1000) {

                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {

                                    payment_status_txt.setText("Serving Coffee");
                                    payment_status.setImageResource(R.drawable.payment_status_3);

                                    new CountDownTimer(3000, 1000) {

                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            payment_status_txt.setText("Enjoy your drink");
                                            payment_status.setImageResource(R.drawable.payment_final);
                                            confirm_coffee.setText("Confirmed");
                                            confirm_coffee.setTextColor(getResources().getColor(R.color.green_confirmed));
                                            confirm_coffee.setBackgroundColor(getResources().getColor(R.color.button_payment_ongoing));


                                            new CountDownTimer(3000, 1000) {

                                                @Override
                                                public void onTick(long l) {

                                                }

                                                @Override
                                                public void onFinish() {

                                                    finish();

                                                }
                                            }.start();

                                        }
                                    }.start();
                                }
                            }.start();

                        }

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                    }.start();


                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PAYMENT_CODE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                payment_method.setText(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng machine = new LatLng(machine_lat, machine_lon);
        LatLng user = new LatLng(user_lat, user_lon);
        mMap.addMarker(new MarkerOptions().position(machine).title("Your coffee").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_logo))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(user).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(machine, DEFAULT_ZOOM));

    }
}
