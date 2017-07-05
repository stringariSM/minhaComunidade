package com.comunidadeapp.minhacomunidade;


import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.comunidadeapp.minhacomunidade.Entities.Apontamento;
import com.comunidadeapp.minhacomunidade.Entities.TipoApontamento;
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NovoApontamento extends Fragment {

    public NovoApontamento(){}

    Spinner sp;
    DatabaseReference dref;
    Button btnSalvar;
    EditText edtDescricao;
    ArrayList<String> lstTipoApontamento = new ArrayList<>();
    ArrayAdapter<String> adapterTipoApontamento;
    String UrlFoto;
    String Cidade;
    double Latitude;
    double Longitude;
    int ID = 0;
    GPSTracker gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novo_apontamento, container, false);

        sp = (Spinner) view.findViewById(R.id.spApontamentoTipo);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvaApontamento);
        edtDescricao = (EditText) view.findViewById(R.id.edtApontamentoDescricao);
        Bundle args = getArguments();
        UrlFoto  = args.getString("URLFOTO");
        Cidade = "";
        Latitude = 0;
        Longitude = 0;
        //Localização
        // create class object
        gps = new GPSTracker(getActivity().getApplicationContext());

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Latitude = latitude;
            Longitude = longitude;

            Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //String cityName = addresses.get(0).getAddressLine(0);
            String stateName = addresses.get(0).getAddressLine(1);
            //String countryName = addresses.get(0).getAddressLine(2);
            // \n is for new line
            //Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nCity: " +cityName+"\nState: " + stateName + "\nCountry: " + countryName + "\nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            Cidade = stateName;
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Usuarios");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.child(user.getUid()).getValue(Usuario.class);
                        SalvaNovoApontamento(usuario);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        adapterTipoApontamento = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_dropdown_item,lstTipoApontamento);
        sp.setAdapter(adapterTipoApontamento);

        dref = FirebaseDatabase.getInstance().getReference();
        Query lstApontamentos = dref.child("Tipo").orderByChild("Descricao");
        lstApontamentos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TipoApontamento tipoApontamento = dataSnapshot.getValue(TipoApontamento.class);
                lstTipoApontamento.add(tipoApontamento.Descricao);
                adapterTipoApontamento.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TipoApontamento tipoApontamento = dataSnapshot.getValue(TipoApontamento.class);
                lstTipoApontamento.remove(tipoApontamento.Descricao);
                adapterTipoApontamento.notifyDataSetChanged();
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
    private void SalvaNovoApontamento(Usuario user){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final Apontamento apontamento = new Apontamento();
        apontamento.Descricao = edtDescricao.getText().toString();
        apontamento.Data = Calendar.getInstance().getTime();
        apontamento.Responsavel = user;
        apontamento.UrlFoto = UrlFoto;
        apontamento.Cidade = Cidade;
        apontamento.Latitude = Latitude;
        apontamento.Longitude = Longitude;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Apontamentos").push().setValue(apontamento, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                    DatabaseReference databaseReference) {
                String uniqueKey = databaseReference.getKey();
                Map<String,Object> taskMap = new HashMap<>();
                taskMap.put("ID",uniqueKey);
                databaseReference.updateChildren(taskMap);
                Fragment fragment = null;
                fragment = new PrincipalFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();
            }
        });
    }
}
