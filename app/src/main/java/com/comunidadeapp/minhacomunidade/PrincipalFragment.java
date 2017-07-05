package com.comunidadeapp.minhacomunidade;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.comunidadeapp.minhacomunidade.Entities.Apontamento;
import com.comunidadeapp.minhacomunidade.Entities.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;


public class PrincipalFragment extends Fragment {

    DatabaseReference dref;
    ArrayList<Apontamento> apontamentos = new ArrayList<>();
    AdapterApontamentos adapter;
    ListView listview;
    private Bitmap bmp;

    public PrincipalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        FloatingActionButton btnAddItem = (FloatingActionButton) view.findViewById(R.id.btnAddImage);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent novoApontamento = new Intent(getActivity(),SplashActivity.class);
                startActivity(novoApontamento);
            }
        });

        listview = (ListView)  view.findViewById(R.id.listview);
        adapter = new AdapterApontamentos(view.getContext(),apontamentos);
        listview.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query lstApontamentos = dref.child("Apontamentos").orderByChild("Data/time");
        lstApontamentos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Apontamento apontamento = dataSnapshot.getValue(Apontamento.class);
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            InputStream in = new URL(apontamento.UrlFoto).openStream();
                            bmp = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            // log error
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        if (bmp != null) {
                           apontamento.Foto = bmp;
                            adapter.notifyDataSetChanged();
                        }
                    }
                }.execute();
                apontamentos.add(apontamento);
                Collections.reverse(apontamentos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        setupToolbar();
        return view;
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        final ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setShowHideAnimationEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.ic_navigation_menu);
        }
    }
}
