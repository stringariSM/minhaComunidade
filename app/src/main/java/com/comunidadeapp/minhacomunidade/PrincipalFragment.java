package com.comunidadeapp.minhacomunidade;

import android.app.Fragment;
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

import com.comunidadeapp.minhacomunidade.Entities.Apontamento;
import com.comunidadeapp.minhacomunidade.Entities.Usuario;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PrincipalFragment extends Fragment {

    DatabaseReference dref;
    ArrayList<String> apontamentos = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listview;

    public PrincipalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        listview = (ListView)  view.findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(view.getContext(),R.layout.item_list,R.id.Teste,apontamentos);
        listview.setAdapter(adapter);
        dref = FirebaseDatabase.getInstance().getReference();
        Query lstApontamentos = dref.child("Apontamentos").orderByChild("Data");
        lstApontamentos.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Apontamento apontamento = dataSnapshot.getValue(Apontamento.class);
                apontamentos.add(apontamento.Descricao);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Apontamento apontamento = dataSnapshot.getValue(Apontamento.class);
                apontamentos.remove(apontamento.Descricao);
                adapter.notifyDataSetChanged();
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
