package com.example.sharenotes;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterForMainHome extends RecyclerView.Adapter<AdapterForMainHome.ViewHolder> implements Filterable {

    /*public AdapterForMainHome(List<ListItemsHomeHelper> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }*/


    private List<ListItemsHomeHelper> listItems;
    private Context context;
    RecyclerView recyclerView;
    ArrayList<String> items;
    ArrayList<String> urls = new ArrayList<>();
    ArrayList<String> faculties = new ArrayList<>();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> uploaders = new ArrayList<>();

    ArrayList<String> itemsCopy = new ArrayList<>();

    public AdapterForMainHome(Context context, RecyclerView recyclerView, ArrayList<String> items,ArrayList<String> urls) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.items = items;
        this.urls = urls;
        //itemsCopy =
        //itemsCopy.addAll(items);
        System.out.println(itemsCopy+"*******constructor e");
    }


    /**
     * To create views for recycler views item.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //ListItemsHomeHelper listItemsHomeHelper = listItems.get(position);

        holder.textHead.setText(items.get(position));
        holder.textDesc.setText("Course name: "+desc.get(position));
        //holder.textDesc.setText(urls.get(position));
        holder.facultyName.setText("Faculty: "+faculties.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //Runs on background
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<String> filteredList = new ArrayList<>();
            System.out.println(charSequence);
            System.out.println(itemsCopy+"*******");
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(itemsCopy);
            } else {

                for (String movie: itemsCopy) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            System.out.println(filteredList+"/*/*/*/");
            return filterResults;
        }
        //runs at UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            items.clear();
            items.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textHead,textDesc,facultyName;
        public ViewHolder(@NonNull View itemView) { //individual item
            super(itemView);

            textHead = (TextView) itemView.findViewById(R.id.text_view_head);
            textDesc = (TextView) itemView.findViewById(R.id.text_view_desc);
            facultyName = (TextView) itemView.findViewById(R.id.faculty_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildLayoutPosition(view);
                    Intent intent = new Intent(context,DownloadPageActivity.class);
                    //intent.setType(Intent.ACTION_VIEW);
                    //intent.setData(Uri.parse(urls.get(position)));
                    intent.putExtra("courseid",items.get(position));
                    intent.putExtra("coursedesc",desc.get(position));
                    intent.putExtra("coursefac",faculties.get(position));
                    intent.putExtra("url",urls.get(position));
                    intent.putExtra("uploader",uploaders.get(position));
                    context.startActivity(intent);
                    //context.startActivity(intent);
                }
            });
        }
    }
    public void update(String name, String url, String faculty,String description, String uploader){
        items.add(name);
        itemsCopy.add(name);
        urls.add(url);
        faculties.add(faculty);
        desc.add(description);
        uploaders.add(uploader);
        notifyDataSetChanged(); //refreshes the recycler view
    }

}
