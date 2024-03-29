package com.comunidadeapp.minhacomunidade;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comunidadeapp.minhacomunidade.Entities.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUser extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private EditText edtSenha;
    private EditText edtEmail;
    private EditText edtNome;
    private Button btnCriar;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);
        overridePendingTransition(R.anim.left_go_in, R.anim.left_go_out);

        Intent Mensagens = new Intent(this,ServicoMensagens.class);
        startService(Mensagens);
        mAuth = FirebaseAuth.getInstance();
        btnCriar = (Button) findViewById(R.id.btnCadastrar);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSenha = (EditText) findViewById(R.id.editTextSenha);
        edtNome = (EditText) findViewById(R.id.editTextNome);
        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarUser(edtEmail.getText().toString(),edtSenha.getText().toString());
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        Intent Main = new Intent(CadastroUser.this,MainActivity.class);
        startActivity(Main);
        finish();
        overridePendingTransition(R.anim.left_back_in, R.anim.left_back_out);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void criarUser(final String email, String senha){
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(CadastroUser.this, "Erro ao Cadastrar",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference();
                            ref.child("Usuarios").child(user.getUid()).setValue(new Usuario(user.getUid(),edtNome.getText().toString(),user.getEmail(),null,"Email"));
                            Intent Principal = new Intent(CadastroUser.this,Drawer.class);
                            Principal.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(Principal);
                            finish();
                        }
                    }
                });
    }
}
