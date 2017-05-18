package com.comunidadeapp.minhacomunidade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Button btnEmail = (Button) findViewById(R.id.btnSignup);
        //Ao clicar no botão de cadastro
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, CadastroUser.class);
                startActivity(it);
            }
        });

        Button btnFacebook = (Button) findViewById(R.id.btnFacebookSignup);
        //Ao clicar no botão de cadastro om Facebook
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO HORSE
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        //Ao clicar no botão de login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, LoginActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(it);
            }
        });
    }
}
