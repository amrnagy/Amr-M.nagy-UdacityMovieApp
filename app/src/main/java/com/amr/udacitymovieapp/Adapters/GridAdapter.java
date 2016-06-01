package com.amr.udacitymovieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amr on 5/28/2016.
 */
public class GridAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int layoutResourceId;

    private ArrayList<String > imgUrls;

    public GridAdapter(Context context, int layoutResourceId, ArrayList<String> imageUrls) {
        super(context, layoutResourceId, imageUrls);
        this.context = context;
        this.imgUrls = imageUrls;
        this.layoutResourceId = layoutResourceId;
        inflater = LayoutInflater.from(context);
    }

    // cView convertView
    @Override
    public View getView(int position, View cView, ViewGroup parent) {
        if (null == cView) {
            cView = inflater.inflate(layoutResourceId, parent, false);
        }

        Picasso.with(context)
                .load(imgUrls.get(position))
                .fit()
                .into((ImageView) cView);
        return cView;
    }
    }

