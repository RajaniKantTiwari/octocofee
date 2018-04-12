package me.prateeksaigal.ocotocoffee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        LinearLayout back = (LinearLayout)findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final SettingsDBHelper settingsDBHelper = new SettingsDBHelper(getApplicationContext());
        final EditText mac_pin = (EditText) findViewById(R.id.mac_pin);
        final EditText accounting_id = (EditText) findViewById(R.id.accounting_id);

        ArrayList<String> settings = settingsDBHelper.getSettings();
        if(settings != null){
            mac_pin.setText(settings.get(0));
            accounting_id.setText(settings.get(1));
        }

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsDBHelper.deleteALL();
                settingsDBHelper.newSettings(mac_pin.getText().toString(), accounting_id.getText().toString());
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.settings_saved),Toast.LENGTH_SHORT).show();
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
