package me.prateeksaigal.ocotocoffee;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.prateeksaigal.base.BaseActivity;
import me.prateeksaigal.base.BaseResponse;

public class PaymentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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

        TextView payment_cash = (TextView) findViewById(R.id.payment_cash);
        TextView payment_card = (TextView) findViewById(R.id.payment_card);

        payment_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","  Card");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        payment_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","  Cash");
                setResult(Activity.RESULT_OK,returnIntent);
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
