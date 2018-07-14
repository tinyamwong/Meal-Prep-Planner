package edu.wit.mobileapp.mealprepplanner;


import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    private ArrayList<Meal> mSearchList;
    private ArrayList<Meal> mMealsList;
    private  SearchListAdapter adapter;
    private RecyclerView listView;
    private EditText searchField;


    //Preferences for json storage
    public SharedPreferences mPrefs;
    public SharedPreferences.Editor preferenceEditor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        MainActivity main = (MainActivity)(getActivity());
        main.findViewById(R.id.main_nav).setVisibility(View.INVISIBLE);

        mPrefs = getActivity().getPreferences(MODE_PRIVATE);
        preferenceEditor = mPrefs.edit();

        retrieveGlobalDataFromStorage();

        //Generate sample data
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        int n = 2;
        for(int i = 0; i < n; i++){
            ingredients.add(new Ingredient("Produce Ingredient " + i, i, "oz", "Produce"));
            ingredients.add(new Ingredient("Bakery Ingredient " + (i+n), (i+n), "oz", "Bakery"));
            ingredients.add(new Ingredient("Deli Ingredient " + (i+n*2), (i+n*2), "oz", "Deli"));
            ingredients.add(new Ingredient("Meat Ingredient " + (i+n*3), (i+n*3), "oz", "Meat"));
            ingredients.add(new Ingredient("Seafood Ingredient " + (i+n*4), (i+n*4), "oz", "Seafood"));
            ingredients.add(new Ingredient("Grocery Ingredient " + (i+n*5), (i+n*5), "oz", "Grocery"));
            ingredients.add(new Ingredient("Dairy Ingredient " + (i+n*6), (i+n*6), "oz", "Dairy"));
        }


        if(mSearchList == null) {
            mSearchList = new ArrayList<>();
            for(int i = 0; i < 25; i++) {
                int rand = (int) (Math.random()*100);
                mSearchList.add(new Meal(i, R.drawable.food, "Generic Meal #" + Integer.toString(rand), 1, ingredients));
            }
        }

        adapter = new SearchListAdapter(this, mSearchList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchField = (EditText) view.findViewById(R.id.searchInput);
        adapter = new SearchListAdapter(this, mSearchList);

        listView = view.findViewById(R.id.searchListView);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        listView.setAdapter(adapter);

        retrieveGlobalDataFromStorage();


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.searchTopBar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        storeGlobalData();
        Log.v(TAG, "OnPause........finished");
    }

    //array list -> json
    public void storeGlobalData(){
        Gson gson = new Gson();
        //Transform the ArrayLists into JSON Data.
        String mealsJSON = gson.toJson(mMealsList);
        preferenceEditor.putString("mealsJSONData", mealsJSON);
        //Commit the changes.
        preferenceEditor.commit();
    }

    //json -> array list
    public void retrieveGlobalDataFromStorage(){
        Gson gson = new Gson();
        if(mPrefs.contains("mealsJSONData")){
            String mealsJSON = mPrefs.getString("mealsJSONData", "");
            Type mealType = new TypeToken<Collection<Meal>>() {}.getType();
            mMealsList = gson.fromJson(mealsJSON, mealType);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity main = (MainActivity)getActivity();
        main.findViewById(R.id.main_nav).setVisibility(View.VISIBLE);
    }

    void filter(String text) {
        List<Meal> temp = new ArrayList<>();
        for(Meal meal : mSearchList) {
            if(meal.getName().toLowerCase().contains(text)) {
                temp.add(meal);
            }
        }
        adapter.updateList(temp);
    }

    public void addMealToGlobalList(Meal meal){
        mMealsList.add(meal);
    }

    public void onBackPressed() {
        Log.v(TAG,"onBackPressed......Called");
        MainActivity main = (MainActivity)getActivity();
        main.onBackPressed();
    }
}