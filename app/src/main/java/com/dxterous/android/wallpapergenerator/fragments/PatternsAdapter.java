package com.dxterous.android.wallpapergenerator.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dxterous.android.wallpapergenerator.R;
import com.dxterous.android.wallpapergenerator.activities.PatternLoaderActivity;

import java.util.ArrayList;

/*
 * Created by dudupoo on 1/3/17.
 */

public class PatternsAdapter extends RecyclerView.Adapter<PatternsAdapter.PatternsViewHolder>
{
    Context context;
    ArrayList<Integer> drawables;
    public PatternsAdapter(ArrayList<Integer> drawables, Context context)
    {
        this.drawables = drawables;
        this.context = context;
    }

    @Override
    public PatternsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_patterns_grid_card, parent, false);
        return new PatternsViewHolder((CardView) v);
    }

    @Override
    public void onBindViewHolder(PatternsViewHolder holder, int position)
    {
        holder.imageView.setImageResource(drawables.get(holder.getAdapterPosition()));
        holder.patternsCard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, PatternLoaderActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return drawables.size();
    }

    class PatternsViewHolder extends RecyclerView.ViewHolder
    {
        CardView patternsCard;
        ImageView imageView;
        public PatternsViewHolder(CardView itemView)
        {
            super(itemView);
            patternsCard = itemView;
            imageView = (ImageView) patternsCard.findViewById(R.id.patternCardImageView);
        }
    }
}
