package com.comunidadeapp.minhacomunidade;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comunidadeapp.minhacomunidade.Entities.Apontamento;

import java.util.ArrayList;

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
        TextView labelView = (TextView) rowView.findViewById(R.id.txtDescricao);
        TextView valueView = (TextView) rowView.findViewById(R.id.txtUsuario);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).Descricao);
        valueView.setText(itemsArrayList.get(position).Responsavel.nome);

        // 5. retrn rowView
        return rowView;
    }
}
