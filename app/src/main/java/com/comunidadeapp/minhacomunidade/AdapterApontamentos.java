package com.comunidadeapp.minhacomunidade;

import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comunidadeapp.minhacomunidade.Entities.Apontamento;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by egasp on 01/07/2017.
 */

public class AdapterApontamentos extends ArrayAdapter<Apontamento> {
    private final Context context;
    private final ArrayList<Apontamento> itemsArrayList;

    public AdapterApontamentos(Context context, ArrayList<Apontamento> itemsArrayList) {

        super(context, R.layout.item_list, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.item_list, parent, false);

        // 3. Get the two text view from the rowView
        TextView Descricao = (TextView) rowView.findViewById(R.id.txtDescricao);
        TextView Usuario = (TextView) rowView.findViewById(R.id.txtUsuario);
        TextView Data = (TextView) rowView.findViewById(R.id.txtData);
        ImageView foto = (ImageView) rowView.findViewById(R.id.imageView4);

        // 4. Set the text for textView
        Descricao.setText(itemsArrayList.get(position).Descricao);
        Usuario.setText(itemsArrayList.get(position).Responsavel.nome);
        if (itemsArrayList.get(position).Data != null) {
            DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
            String sdt = df.format(itemsArrayList.get(position).Data);
            Data.setText(sdt);
        }
        else
            Data.setText("");

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
