package edu.wit.mobileapp.mealprepplanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{
    //vars
    private List<Meal> searchList;
    private Context mContext;
    private SearchFragment fragment;

    public SearchListAdapter(SearchFragment fragment, List<Meal> searchList) {
        this.searchList = searchList;
        this.mContext = fragment.getContext();
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.search_list, null);
        return new SearchListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, int position) {
        Meal meal = searchList.get(position);
        holder.image.setImageResource(meal.getImage());
        holder.name.setText(meal.getName());

        holder.foreground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Meal meal = searchList.get(holder.getAdapterPosition());
                fragment.addMealToGlobalList(meal);
                Toast.makeText(mContext, "Meal:" + meal.getName() + " added to meal list", Toast.LENGTH_LONG).show();
                holder.foreground.setBackgroundColor(Color.GREEN);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        RelativeLayout foreground;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.meal_picture);
            name = itemView.findViewById(R.id.meal_name);

            foreground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public void updateList(List<Meal> list) {
        searchList = list;
        notifyDataSetChanged();
    }
}