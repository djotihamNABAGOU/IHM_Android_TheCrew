package com.example.mybudget.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mybudget.R;
import com.example.mybudget.models.Spending;

import java.util.Date;
import java.util.List;

public class SpendingGridViewAdapter extends ArrayAdapter<Spending> {

    public SpendingGridViewAdapter(Context context, int resource, List<Spending> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.spending_grid_item, null);
        }
        Spending spending = getItem(position);
//        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtCout = (TextView) v.findViewById(R.id.txtCout);
        txtCout.setText("oki");
        TextView txtFrequence = (TextView) v.findViewById(R.id.txtFrequence);
        TextView txtLibelle = (TextView) v.findViewById(R.id.txtLibelle);

//        System.out.println(spending.getCout());
//        img.setImageResource(product.getImageId());
        txtCout.setText(spending.getCout()+"€");
        try {
            Date initDate = new SimpleDateFormat("yyyy-MM-dd").parse(spending.getDate());
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String parsedDate = formatter.format(initDate);
            txtFrequence.setText(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txtLibelle.setText(spending.getLibelle_aliment());
        return v;
    }
}
