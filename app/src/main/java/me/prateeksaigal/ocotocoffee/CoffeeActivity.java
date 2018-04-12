package me.prateeksaigal.ocotocoffee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;

public class CoffeeActivity extends BaseActivity {


    private Handler mHandler;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private SeekBar foam_seekbar, coffee_seekbar, milk_seekbar;
    private TextView foam_txt, coffee_txt, milk_txt;

    private EditText coffee_name;
    private Button save, cancel;

    private boolean isUpdate = false;

    private Double machine_lat, machine_lon, user_lat, user_lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        foam_seekbar = (SeekBar) findViewById(R.id.foam_seekbar);
        coffee_seekbar = (SeekBar) findViewById(R.id.coffee_seekbar);
        milk_seekbar = (SeekBar) findViewById(R.id.milk_seekbar);
        foam_txt = (TextView) findViewById(R.id.foam_txt);
        coffee_txt = (TextView) findViewById(R.id.coffee_txt);
        milk_txt = (TextView) findViewById(R.id.milk_txt);

        LinearLayout coffee_container = (LinearLayout) findViewById(R.id.coffee_container);
        LinearLayout foam_container = (LinearLayout) findViewById(R.id.foam_container);
        LinearLayout milk_container = (LinearLayout) findViewById(R.id.milk_container);

        ImageView coffe_img = (ImageView) findViewById(R.id.coffee_img);

        AttributeSet attr = new AttributeSet() {
            @Override
            public int getAttributeCount() {
                return 0;
            }

            @Override
            public String getAttributeName(int i) {
                return null;
            }

            @Override
            public String getAttributeValue(int i) {
                return null;
            }

            @Override
            public String getAttributeValue(String s, String s1) {
                return null;
            }

            @Override
            public String getPositionDescription() {
                return null;
            }

            @Override
            public int getAttributeNameResource(int i) {
                return 0;
            }

            @Override
            public int getAttributeListValue(String s, String s1, String[] strings, int i) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(String s, String s1, boolean b) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(String s, String s1, int i) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(String s, String s1, int i) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(String s, String s1, int i) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(String s, String s1, float v) {
                return 0;
            }

            @Override
            public int getAttributeListValue(int i, String[] strings, int i1) {
                return 0;
            }

            @Override
            public boolean getAttributeBooleanValue(int i, boolean b) {
                return false;
            }

            @Override
            public int getAttributeResourceValue(int i, int i1) {
                return 0;
            }

            @Override
            public int getAttributeIntValue(int i, int i1) {
                return 0;
            }

            @Override
            public int getAttributeUnsignedIntValue(int i, int i1) {
                return 0;
            }

            @Override
            public float getAttributeFloatValue(int i, float v) {
                return 0;
            }

            @Override
            public String getIdAttribute() {
                return null;
            }

            @Override
            public String getClassAttribute() {
                return null;
            }

            @Override
            public int getIdAttributeResourceValue(int i) {
                return 0;
            }

            @Override
            public int getStyleAttribute() {
                return 0;
            }
        };

        TrapezoidView t = new TrapezoidView(getApplicationContext());
        coffe_img.setBackground(t.getDrawable());

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);

        coffee_name = (EditText) findViewById(R.id.coffee_name);

        foam_seekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        foam_seekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        coffee_seekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        coffee_seekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


        milk_seekbar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        milk_seekbar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        foam_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                foam_txt.setText(i + " ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        coffee_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                coffee_txt.setText(i + " ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        milk_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                milk_txt.setText(i + " ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final CoffeeDBHelper coffeeDBHelper = new CoffeeDBHelper(getApplicationContext());


        final int coffee_id = getIntent().getIntExtra("coffee_id", -1);

        machine_lat = getIntent().getDoubleExtra("machine_lat", -34);
        machine_lon = getIntent().getDoubleExtra("machine_lon", 151);
        user_lat = getIntent().getDoubleExtra("user_lat", -34.0001);
        user_lon = getIntent().getDoubleExtra("user_lon", -151.003);

        final boolean isForward = getIntent().getBooleanExtra("forwardtomaps2", false);

        if (coffee_id != -1) {
            isUpdate = true;


            Coffee coffee = coffeeDBHelper.getCoffee(coffee_id);
            if (coffee != null) {
                foam_seekbar.setProgress(Integer.parseInt(coffee.get_foam()));
                coffee_seekbar.setProgress(Integer.parseInt(coffee.get_coffee()));
                milk_seekbar.setProgress(Integer.parseInt(coffee.get_milk()));
                coffee_name.setText(coffee.getName());
                coffee_name.setSelection(coffee_name.getText().length());

            }

        } else {
            isUpdate = false;
        }

        boolean isBaseActivity = getIntent().getBooleanExtra("base_activity", false);

        final int type = getIntent().getIntExtra("type", 4);


        if (type == 1) {

            milk_container.setVisibility(View.VISIBLE);
            coffee_container.setVisibility(View.VISIBLE);
            foam_container.setVisibility(View.GONE);
            coffe_img.setImageResource(R.drawable.milk_coffee);

        } else if (type == 2) {

            foam_container.setVisibility(View.VISIBLE);
            coffee_container.setVisibility(View.VISIBLE);
            milk_container.setVisibility(View.GONE);
            coffe_img.setImageResource(R.drawable.foam_coffee);

        } else if (type == 3) {

            foam_container.setVisibility(View.VISIBLE);
            milk_container.setVisibility(View.VISIBLE);
            coffee_container.setVisibility(View.GONE);
            coffe_img.setImageResource(R.drawable.foam_milk);

        } else {
            coffee_container.setVisibility(View.VISIBLE);
            foam_container.setVisibility(View.VISIBLE);
            milk_container.setVisibility(View.VISIBLE);
            coffe_img.setImageResource(R.drawable.milk_coffee_foam);
        }


        if (isBaseActivity)
            isUpdate = false;

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Coffee coffee = new Coffee(coffee_name.getText().toString(),
                        foam_seekbar.getProgress() + "", coffee_seekbar.getProgress() + "",
                        milk_seekbar.getProgress() + "",
                        "1", "2", type);


                if (coffee_name.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name for your coffee", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (isUpdate) {
                    save.setText("Update");
                    coffee.setId(coffee_id);
                    coffeeDBHelper.updateContact(coffee);
                } else {
                    coffeeDBHelper.addNew(coffee);
                }

                if (isForward) {
                    Intent intent = new Intent(getApplicationContext(), SelectedMapsActivity.class);
                    intent.putExtra("machine_lat", machine_lat);
                    intent.putExtra("machine_lon", machine_lon);
                    intent.putExtra("user_lat", user_lat);
                    intent.putExtra("user_lon", user_lon);

                    startActivity(intent);
                    finish();

                } else {
                    finish();
                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mHandler = new Handler();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();
        toolbar.setTitle("Custom it");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        //actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        //actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_hamburger);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        actionBarDrawerToggle.syncState();

        handleNavItemClick();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.coffee_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleNavItemClick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.nav_about_us:
                        startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.nav_orders:
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.nav_contact:
                        startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                        break;
                    case R.id.nav_payments:
                        startActivity(new Intent(getApplicationContext(), PaymentActivity.class));
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void attachView() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(BaseResponse response, int requestCode) {

    }
}
