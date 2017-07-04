package com.comunidadeapp.minhacomunidade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.left_go_in, R.anim.left_go_out);
    }

    @Override
    public void onBackPressed() {
        Intent Main = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(Main);
        finish();
        overridePendingTransition(R.anim.left_back_in, R.anim.left_back_out);
    }
}
