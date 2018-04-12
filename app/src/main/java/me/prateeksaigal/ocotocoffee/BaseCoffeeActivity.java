package me.prateeksaigal.ocotocoffee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;
import me.prateeksaigal.ocotocoffee.adapter.BaseCoffeeAdapter;

public class BaseCoffeeActivity extends BaseActivity {

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

    private ListView my_recipe_ll, base_recipe_ll;

    private boolean forwardtomaps2;

    private TextView my_recipe_heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_coffee);
        my_recipe_heading = (TextView) findViewById(R.id.my_recipe_heading);
        mHandler = new Handler();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.bringToFront();
        toolbar.setTitle("Select Drink");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        forwardtomaps2 = getIntent().getBooleanExtra("forwardtomaps2", false);

        machine_lat = getIntent().getDoubleExtra("machine_lat", -34);
        machine_lon = getIntent().getDoubleExtra("machine_lon", 151);
        user_lat = getIntent().getDoubleExtra("user_lat", -34.0001);
        user_lon = getIntent().getDoubleExtra("user_lon", -151.003);

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

        my_recipe_ll = (ListView) findViewById(R.id.my_recipes_list_view);
        base_recipe_ll = (ListView) findViewById(R.id.base_recipes_list_view);



        ArrayList<String> items = new ArrayList<String>();

        ArrayList<Integer> drawables = new ArrayList<>();

        ArrayList<Integer> coffee_ids = new ArrayList<>();

        ArrayList<Integer> type = new ArrayList<>();


        CoffeeDBHelper coffeeDBHelper = new CoffeeDBHelper(getApplicationContext());
        ArrayList<Coffee> coffeeArrayList = new ArrayList<>();
        coffeeArrayList = coffeeDBHelper.getAll();
        if (coffeeArrayList != null) {
            if (coffeeArrayList.size() > 4) {
                for (int i = 4; i < coffeeArrayList.size(); i++) {
                    coffee_ids.add(coffeeArrayList.get(i).getId());
                    items.add(coffeeArrayList.get(i).getName());
                    int t_type = coffeeArrayList.get(i).get_type();
                    if (t_type == 1) {
                        drawables.add(R.drawable.coffee1);
                    } else if (t_type == 2) {
                        drawables.add(R.drawable.coffee2);
                    } else if (t_type == 3) {
                        drawables.add(R.drawable.coffee3);
                    } else {
                        drawables.add(R.drawable.coffee4);
                    }
                    type.add(t_type);
                }
            }
            else {
                my_recipe_heading.setVisibility(View.GONE);
            }
        }
        BaseCoffeeAdapter adapterBaseCoffeeMy = new BaseCoffeeAdapter(BaseCoffeeActivity.this, items, drawables, coffee_ids, type);
        my_recipe_ll.setAdapter(adapterBaseCoffeeMy);


        items = new ArrayList<>();
        drawables = new ArrayList<>();
        coffee_ids = new ArrayList<>();
        type = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            coffee_ids.add(coffeeArrayList.get(i).getId());
            items.add(coffeeArrayList.get(i).getName());
            type.add(coffeeArrayList.get(i).get_type());
        }
        drawables.add(R.drawable.coffee1);
        drawables.add(R.drawable.coffee2);
        drawables.add(R.drawable.coffee3);
        drawables.add(R.drawable.coffee4);

        BaseCoffeeAdapter adapterBaseCoffee = new BaseCoffeeAdapter(BaseCoffeeActivity.this, items, drawables, coffee_ids, type);

        base_recipe_ll.setAdapter(adapterBaseCoffee);

        base_recipe_ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CoffeeActivity.class);
                TextView cof_id = (TextView) view.findViewById(R.id.cof_id);
                TextView type = (TextView) view.findViewById(R.id.type);
                intent.putExtra("coffee_id", Integer.parseInt(cof_id.getText().toString()));
                intent.putExtra("base_activity", true);
                intent.putExtra("forwardtomaps2", forwardtomaps2);
                intent.putExtra("type", Integer.parseInt(type.getText().toString()));
                intent.putExtra("machine_lat", machine_lat);
                intent.putExtra("machine_lon", machine_lon);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_lon", user_lon);

                startActivity(intent);

                finish();
            }
        });

        my_recipe_ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), CoffeeActivity.class);
                TextView cof_id = (TextView) view.findViewById(R.id.cof_id);
                TextView type = (TextView) view.findViewById(R.id.type);
                intent.putExtra("coffee_id", Integer.parseInt(cof_id.getText().toString()));
                intent.putExtra("type", Integer.parseInt(type.getText().toString()));
                intent.putExtra("forwardtomaps2", forwardtomaps2);
                intent.putExtra("machine_lat", machine_lat);
                intent.putExtra("machine_lon", machine_lon);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_lon", user_lon);

                startActivity(intent);

                finish();

            }
        });

        Helper.getListViewSize(my_recipe_ll);
        Helper.getListViewSize(base_recipe_ll);

    }

    @Override
    public void attachView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> items = new ArrayList<String>();

        ArrayList<Integer> drawables = new ArrayList<>();

        ArrayList<Integer> coffee_ids = new ArrayList<>();

        ArrayList<Integer> type = new ArrayList<>();

        CoffeeDBHelper coffeeDBHelper = new CoffeeDBHelper(getApplicationContext());
        ArrayList<Coffee> coffeeArrayList = new ArrayList<>();
        coffeeArrayList = coffeeDBHelper.getAll();
        if (coffeeArrayList != null) {
            if (coffeeArrayList.size() > 4) {
                my_recipe_heading.setVisibility(View.VISIBLE);
                for (int i = 4; i < coffeeArrayList.size(); i++) {
                    coffee_ids.add(coffeeArrayList.get(i).getId());
                    items.add(coffeeArrayList.get(i).getName());
                    int t_type = coffeeArrayList.get(i).get_type();
                    if (t_type == 1) {
                        drawables.add(R.drawable.coffee1);
                    } else if (t_type == 2) {
                        drawables.add(R.drawable.coffee2);
                    } else if (t_type == 3) {
                        drawables.add(R.drawable.coffee3);
                    } else {
                        drawables.add(R.drawable.coffee4);
                    }
                    type.add(t_type);

                }
            }
            else {
                my_recipe_heading.setVisibility(View.GONE);
            }
        }
        BaseCoffeeAdapter adapterBaseCoffeeMy = new BaseCoffeeAdapter(BaseCoffeeActivity.this, items, drawables, coffee_ids, type);
        my_recipe_ll.setAdapter(adapterBaseCoffeeMy);

        Helper.getListViewSize(my_recipe_ll);
        Helper.getListViewSize(base_recipe_ll);


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
    public void onClick(View view) {

    }

    @Override
    public void onSuccess(BaseResponse response, int requestCode) {

    }
}
