package com.petarzoric.fitogether;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Alex on 19.01.2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    Context context;
    UserProfile[] matches;
    SearchAdapter(Context context, UserProfile[] matches){
        this.context = context;
        this.matches = matches;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.listviewitems, parent, false);
        return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.setName(matches[position].getName());
        holder.setAge(String.valueOf(matches[position].getAge()));
        holder.setLevel(Level.parseToString(matches[position].getLevel()));
        holder.setGender(Gender.parseToString(matches[position].getGender()));
        holder.setImage(matches[position].getThumbnail(), context);

    }

    @Override
    public int getItemCount() {
        return matches.length;
    }
}
