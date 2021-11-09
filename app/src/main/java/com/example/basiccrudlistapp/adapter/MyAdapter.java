package com.example.basiccrudlistapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.basiccrudlistapp.R;
import com.example.basiccrudlistapp.model.Note;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Note> items;
    private LayoutInflater layoutInflater;

    public MyAdapter(Context context, List<Note> items) {
        this.items = items;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return items.size();
    }
    @Override
    public Object getItem(int i) {
        return items.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.myrow, null);
        }
        TextView textView = view.findViewById(R.id.textViewTitle);
        textView.setText(items.get(i).getTitle());
        TextView textViewContent = view.findViewById(R.id.textViewContent);
        textViewContent.setText(items.get(i).getContent());
        return view;
    }
}
