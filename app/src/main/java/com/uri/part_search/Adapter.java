package com.uri.part_search;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList matches;
    private ArrayList result;
    private Context context;


    public Adapter(Context ctx, ArrayList matches, ArrayList result){
        context = ctx;
        inflater = LayoutInflater.from(ctx);
        this.matches = matches;
        this.result = result;
    }

    public void removeItem(int position) {
        matches.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, matches.size());
    }
    public void restoreItem(LauncherActivity.ListItem model, int position) {
        matches.add(position, model);
        // notify item added by position
        notifyItemInserted(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.textView32.setText((CharSequence) matches.get(position));
        final String res = (String) result.get(position);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(res));
                context.startActivity(browserIntent);
                }
        });
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView32;
        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            textView32 = (TextView) itemView.findViewById(R.id.textView32);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }

    }
}