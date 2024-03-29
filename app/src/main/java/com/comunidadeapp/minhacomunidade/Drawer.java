package com.comunidadeapp.minhacomunidade;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.comunidadeapp.minhacomunidade.Auxiliares.ImageHelper;
import com.comunidadeapp.minhacomunidade.Entities.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.comunidadeapp.minhacomunidade.R.id.imageView;

public class Drawer extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView foto;
    private TextView username;
    private TextView email;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);



        initNavigationDrawer();
        initNavigationDrawerHeader();
        initDrawerListener(savedInstanceState);
        String UrlFoto;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                UrlFoto= null;
            } else {
                UrlFoto= extras.getString("URLFOTO");
            }
        } else {
            UrlFoto= (String) savedInstanceState.getSerializable("URLFOTO");
        }
        if (UrlFoto!=null){
            Fragment fragment;
            fragment = new NovoApontamento();
            Bundle args = new Bundle();
            args.putString("URLFOTO", UrlFoto);
            fragment.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
        }
    }

    private void initNavigationDrawer()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null)
        {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void initNavigationDrawerHeader()
    {
        View header = navigationView.getHeaderView(0);
        username = (TextView) header.findViewById(R.id.username);
        email = (TextView) header.findViewById(R.id.email);
        foto = (ImageView) header.findViewById(R.id.user_photo);

        setupUserInformations();
    }

    private void setupUserInformations()
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Usuarios");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Usuario usuario = dataSnapshot.child(user.getUid()).getValue(Usuario.class);
                username.setText(usuario.nome);
                email.setText(usuario.email);

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            InputStream in = new URL(usuario.foto).openStream();
                            bmp = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            // log error
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (bmp != null)
                            foto.setImageBitmap(ImageHelper.CortaImg(bmp,360));
                    }

                }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initDrawerListener(Bundle savedInstanceState)
    {
        if (savedInstanceState == null)
        {
            MenuItem item = navigationView.getMenu().getItem(0);
            onNavigationItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (isNavigationDrawerOpen())
        {
            closeNavigationDrawer();
        }
        else
        {
            super.onBackPressed();
        }
    }

    protected boolean isNavigationDrawerOpen()
    {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavigationDrawer()
    {
        if (drawerLayout != null)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        item.setChecked(true);
        drawerLayout.closeDrawers();
        selectDrawerItem(item);

        return true;
    }

    public void selectDrawerItem(MenuItem menuItem)
    {
        Fragment fragment = null;

        switch (menuItem.getItemId())
        {
            case R.id.mnPrincipal:
                fragment = new PrincipalFragment();
                setTitle("Home");
                break;

            case R.id.mnMeuApontamento:
                fragment = new MeusApontamentosFragment();
                break;

            case R.id.mnNovoApontamento:
                Intent SplashActvity = new Intent(Drawer.this,SplashActivity.class);
                startActivity(SplashActvity);
                break;

            case R.id.mnSair:
                FirebaseAuth.getInstance().signOut();
                Intent Login = new Intent(Drawer.this,MainActivity.class);
                startActivity(Login);
                finish();
                break;

            default:
                break;
        }

        if(fragment != null)
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

            setTitle(menuItem.getTitle());
        }
    }

    public static Drawable CarregaFoto(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "Foto");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
