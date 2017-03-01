package com.dxterous.android.wallpapergenerator.fragments;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dxterous.android.wallpapergenerator.R;

import java.util.ArrayList;
import java.util.Arrays;

public class PatternsFragment extends Fragment
{
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Integer> drawables;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_patterns, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recyclerView = (RecyclerView) view.findViewById(R.id.patternsRecycler);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        drawables = new ArrayList<>(Arrays.asList(R.drawable.nav_color_24dp,R.drawable.nav_pattern_24dp));
        adapter = new PatternsAdapter(drawables, getActivity());
        recyclerView.setAdapter(adapter);

    }
}
