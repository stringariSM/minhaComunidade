package com.comunidadeapp.minhacomunidade;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.comunidadeapp.minhacomunidade.Auxiliares.ImageHelper;
import com.comunidadeapp.minhacomunidade.Entities.Apontamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by egasp on 01/07/2017.
 */

public class AdapterApontamentos extends ArrayAdapter<Apontamento> {
    private final Context context;
    private final ArrayList<Apontamento> itemsArrayList;
    private boolean retorna = false;
    private Bitmap bmp;

    public AdapterApontamentos(Context context, ArrayList<Apontamento> itemsArrayList) {

        super(context, R.layout.item_list, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_list, parent, false);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // 3. Get the two text view from the rowView
        TextView Descricao = (TextView) rowView.findViewById(R.id.txtDescricao);
        TextView Usuario = (TextView) rowView.findViewById(R.id.txtUsuario);
        TextView Data = (TextView) rowView.findViewById(R.id.txtData);
        ImageView foto = (ImageView) rowView.findViewById(R.id.imageView3);
        final TextView txtResolvido = (TextView) rowView.findViewById(R.id.txtResolvido);
        final Button btnResolver = (Button) rowView.findViewById(R.id.btnResolver);
        String uid = user.getUid();

        // 4. Set the text for textView
        Descricao.setText(itemsArrayList.get(position).Descricao);
        Usuario.setText(itemsArrayList.get(position).Responsavel.nome);
        if (itemsArrayList.get(position).Data != null) {
            DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
            String sdt = df.format(itemsArrayList.get(position).Data);
            Data.setText(sdt);
        } else {
            Data.setText("");
        }
        if(itemsArrayList.get(position).Foto != null){
            foto.setImageBitmap(itemsArrayList.get(position).Foto);
            foto.setAlpha((float) 1);
        }
        else{
            foto.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.city_background_blur));
        }
        if(itemsArrayList.get(position).resolvido.equals("true")){
            txtResolvido.setText("Resolvido");
        }
        else if(itemsArrayList.get(position).resolvido.equals("false") && itemsArrayList.get(position).Responsavel.Id.equals(uid)) {
        //else if(itemsArrayList.get(position).Responsavel.Id == uid) {
            txtResolvido.setVisibility(View.GONE);
            btnResolver.setVisibility(View.VISIBLE);
            btnResolver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
                    Map<String,Object> taskMap = new HashMap<>();
                    taskMap.put("resolvido","true");
                    dref.child("Apontamentos").child(itemsArrayList.get(position).ID).updateChildren(taskMap);
                    txtResolvido.setVisibility(View.VISIBLE);
                    btnResolver.setVisibility(View.GONE);
                    txtResolvido.setText("Resolvido");
                }
            });
        }
        // 5. retrn rowView
        return rowView;
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
