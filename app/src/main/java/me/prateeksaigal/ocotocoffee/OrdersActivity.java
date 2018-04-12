package me.prateeksaigal.ocotocoffee;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;

public class OrdersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        LinearLayout back_btn = (LinearLayout) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
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
