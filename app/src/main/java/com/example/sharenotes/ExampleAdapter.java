package com.example.sharenotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> implements Filterable {
    private List<ExampleItem> exampleList;
    private List<ExampleItem> exampleListFull;
    Context context;
    RecyclerView recyclerView;

    class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView textHead,textDesc,facultyName,textNo;

        ExampleViewHolder(View itemView) {
            super(itemView);
            textNo = (TextView) itemView.findViewById(R.id.number);
            textHead = (TextView) itemView.findViewById(R.id.text_view_head);
            textDesc = (TextView) itemView.findViewById(R.id.text_view_desc);
            facultyName = (TextView) itemView.findViewById(R.id.faculty_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = recyclerView.getChildLayoutPosition(view);
                    String title = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.number)).getText().toString();
                    //int positionExample = Integer.parseInt(title);
                    //System.out.println(positionExample);
                    ExampleItem currentItem = exampleList.get(position);
                    //int position = recyclerView.getChildLayoutPosition(view);
                    Intent intent = new Intent(context,DownloadPageActivity.class);
                    //intent.setType(Intent.ACTION_VIEW);
                    //intent.setData(Uri.parse(urls.get(position)));
                    intent.putExtra("courseid",currentItem.getCourseId());
                    intent.putExtra("coursedesc",currentItem.getDesc());
                    intent.putExtra("coursefac",currentItem.getFac());
                    intent.putExtra("url",currentItem.getUrl());
                    intent.putExtra("uploader",currentItem.getUploader());
                    context.startActivity(intent);
                    //context.startActivity(intent);
                }
            });
        }
    }

    ExampleAdapter(List<ExampleItem> exampleList, Context cont, RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = cont;
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item, parent,false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = exampleList.get(position);

        /*holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView1.setText(currentItem.getText1());
        holder.textView2.setText(currentItem.getText2());*/
        holder.textNo.setText(Integer.toString(currentItem.getNo()+1));
        holder.textHead.setText(currentItem.getCourseId());
        holder.textDesc.setText("Course name: "+currentItem.getDesc());
        //holder.textDesc.setText(urls.get(position));
        holder.facultyName.setText("Faculty: "+currentItem.getFac());
    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExampleItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ExampleItem item : exampleListFull) {
                    if (item.getCourseId().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    /*public void update(String name, String url, String faculty,String description, String uploader){
        ExampleItem exampleItem = new ExampleItem(name,url,faculty,description,uploader);
        exampleList.add(exampleItem);
        notifyDataSetChanged(); //refreshes the recycler view
    }*/
}
