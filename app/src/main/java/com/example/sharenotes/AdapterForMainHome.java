package com.example.sharenotes;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForMainHome extends RecyclerView.Adapter<AdapterForMainHome.ViewHolder> {

    public AdapterForMainHome(List<ListItemsHomeHelper> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    private List<ListItemsHomeHelper> listItems;
    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItemsHomeHelper listItemsHomeHelper = listItems.get(position);

        holder.textHead.setText(listItemsHomeHelper.getHead());
        holder.textDesc.setText(listItemsHomeHelper.getDesc());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textHead,textDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textHead = (TextView) itemView.findViewById(R.id.text_view_head);
            textDesc = (TextView) itemView.findViewById(R.id.text_view_desc);
        }
    }

}
