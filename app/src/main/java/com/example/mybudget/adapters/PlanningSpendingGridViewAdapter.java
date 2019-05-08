package com.example.mybudget.adapters;

import android.widget.ArrayAdapter;

import com.example.mybudget.models.PlannedSpending;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.mybudget.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlanningSpendingGridViewAdapter extends ArrayAdapter<PlannedSpending> {

    public PlanningSpendingGridViewAdapter(Context context, int resource, List<PlannedSpending> objects) {
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
        PlannedSpending spending = getItem(position);
//        ImageView img = (ImageView) v.findViewById(R.id.imageView);
        TextView txtCout = (TextView) v.findViewById(R.id.txtCout);
        txtCout.setText("oki");
        TextView txtFrequence = (TextView) v.findViewById(R.id.txtFrequence);
        TextView txtLibelle = (TextView) v.findViewById(R.id.txtLibelle);

        System.out.println(spending.getCout());
//        img.setImageResource(product.getImageId());
        txtCout.setText(spending.getCout());
        txtFrequence.setText(spending.getFrequence());
        txtLibelle.setText(spending.getLibelle_aliment());
        return v;
    }
}
